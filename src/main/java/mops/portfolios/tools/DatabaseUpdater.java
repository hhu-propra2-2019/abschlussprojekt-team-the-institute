package mops.portfolios.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import mops.portfolios.PortfoliosApplication;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.state.StateService;
import mops.portfolios.domain.user.User;
import mops.portfolios.domain.user.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
@SuppressWarnings("PMD")
public class DatabaseUpdater {
  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);
  private final String serviceName = "gruppen2";

  /**
   * The url to request the updates from. The formatting is highly important.
   */ // Don't make this part of the Constructor as it's not feeded by Spring, but entered manually
  transient Url url;

  final @NonNull GroupRepository groupRepository;

  final @NonNull UserRepository userRepository;

  final @NonNull StateService stateService;

  /**
   * Use this method to get the updates from Gruppenbildung regarding groups.
   */
  // TODO: use a better method name Do this later to avoid merge conflicts
  public void execute() {
    IHttpClient httpClient = new HttpClient();
    long updateStatus = stateService.getState(this.serviceName);
    String requestUrl = this.url.toString() + updateStatus;
    getGroupUpdatesFromUrl(httpClient, requestUrl);
  }

  /**
   * This method updates the database using an injected HttpClient and url, easing up testing.
   *
   * @param httpClient The IHttpClient to use
   */
  @SuppressWarnings("PMD")
  void getGroupUpdatesFromUrl(IHttpClient httpClient, String url) {
    String responseBody = "";

    // try to receive data from service Gruppenbildung
    try {
      responseBody = httpClient.get(url);
    } catch (HttpClientErrorException clientErr) { // if status 4xx or 5xx returned
      logger.warn("The service " + this.serviceName + " is not reachable: "
              + clientErr.getRawStatusCode()
              + " " + clientErr.getStatusText());
      return;
    } catch (IllegalArgumentException argException) {
      logger.error(argException.getMessage());
      // Most likely URL formatted wrong, read logs from Url generation
    }

    updateDatabaseEvents(responseBody);
  }

  /**
   * This method updates the database using the injected JSON String, easing up testing.
   *
   * @param jsonUpdate The String containing the JSON data to update the database
   */
  @SuppressWarnings("PMD")
  public void updateDatabaseEvents(String jsonUpdate) {

    if (jsonUpdate == null) {
      logger.error("Nothing received. The received String is null");
      return;
    }

    // check for possible errors
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(jsonUpdate);
    } catch (JSONException jsonErr) {
      logger.warn("An error occured while parsing the JSON data "
              + "received by the service " + this.serviceName, jsonErr);
      return; // cannot update anyways
    }
    if (jsonObject == null) {
      logger.warn("An error occured while parsing the JSON data "
              + "received by the service " + this.serviceName + ": jsonObject is null");
      return; // cannot update anyways
    }

    if (isNotModified(jsonObject)) {
      logger.info("Database not modified");
      return; // no need to update local database
    }

    processStatusUpdate(jsonObject);

    processGroupUpdates(jsonObject);

  }

  private void processStatusUpdate(JSONObject jsonObject) {
    Long newStatus;

    newStatus = jsonObject.getBigInteger("status").longValue();
    stateService.setState(this.serviceName, newStatus);

  }

  /**
   * Checks if there are any modifications.
   *
   * @param jsonUpdate The JSONObject to check
   * @return <b>true</b> if not modified, <b>false</b> if modified
   */
  public boolean isNotModified(JSONObject jsonUpdate) {
    JSONArray groupList = jsonUpdate.getJSONArray("groupList");
    return groupList.isEmpty();
  }

  private void processGroupUpdates(JSONObject jsonUpdate) {
    JSONArray groupList = jsonUpdate.getJSONArray("groupList");

    for (Object groupElement : groupList) {

      JSONObject group = (JSONObject) groupElement;
      Long groupId;

      try {
        String groupUUIdString = group.getString("id");
        UUID groupUUID = UUID.fromString(groupUUIdString);
        groupId = this.UUIDtoLong(groupUUID);
      } catch (Exception exc) {
        logger.warn("An error occured while getting the UUID or converting it into Long", exc);
        return;
      }

      String title = null;

      if (!group.isNull("title")) {
        title = group.getString("title");

      }

      JSONArray members = group.getJSONArray("members");

      List<User> userList = new ArrayList<>();

      if (members != null) {
        for (Object member : members) {
          User user = new User();
          JSONObject users = (JSONObject) member;
          user.setName(users.getString("user_id"));
          userList.add(user);
        }

        if (title == null || title.isEmpty()) {
          groupRepository.deleteById(groupId);
          logger.info("Group deleted: " + groupId);
        } else if (groupExists(groupId)) {
          groupRepository.deleteById(groupId);
          logger.info("Group deleted: " + groupId);
        }
        for (User user : userList) {
          if (userRepository.findOneByName(user.getName()) == null) {
            userRepository.save(user);
            logger.info("Couldn't find user. Added " + user.getName());
          }
          groupRepository.save(new Group(groupId, title, userList));
          logger.info("Saved group '" + title + "' with id " + groupId
                  + " containing " + userList.size() + " members");
        }
      }
    }

  }

  private boolean groupExists(Long groupId) {
    Objects.requireNonNull(groupId);
    List<Long> groupIds = new ArrayList<>();
    groupIds.add(groupId);

    return !groupRepository.findAllById(groupIds).equals(new ArrayList<>());
  }

  Long UUIDtoLong(UUID uuid) {
    return uuid.getMostSignificantBits() & Long.MAX_VALUE;
  }
}
