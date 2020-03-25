package mops.portfolios.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
  final String serviceName = "gruppen2";

  transient String url;

  final @NonNull GroupRepository groupRepository;

  final @NonNull UserRepository userRepository;

  final @NonNull StateService stateService;


  /**
   * Use this method to get the updates from Gruppenbildung regarding groups.
   */
  public void getUpdatesFromJsonObject() {
    IHttpClient httpClient = new HttpClient();
    long updateStatus = stateService.getState(this.serviceName);
    String requestUrl = this.url; // + updateStatus;
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
      logger.warn("The service Gruppenbildung is not reachable: " + clientErr.getRawStatusCode()
              + " " + clientErr.getStatusText());
      responseBody = null;
    } catch (IllegalArgumentException argException) {
      logger.error(argException.getMessage()); // Most likely URL formatted wrong
     // throw new RuntimeException(argException);
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

    // check for possible errors
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(jsonUpdate);
    } catch (JSONException jsonErr) {
      logger.error("An error occured while parsing the JSON data "
              + "received by the service Gruppenbildung ", jsonErr);
      // FIXME: keep this only while in development
      // throw new RuntimeException("Error while trying to parse HTTP response to JSON object: "
          //    + jsonErr.getMessage());
    }
    if (jsonObject == null) {
      logger.error("An error occured while parsing the JSON data "
              + "received by the service Gruppenbildung");
      // FIXME: Keep this only while in development
     // throw new RuntimeException("JSON Object is null");
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

  void processGroupUpdates(JSONObject jsonUpdate) {


    JSONArray groupList = jsonUpdate.getJSONArray("groupList");

    for (Object groupElement : groupList) {

      JSONObject group = (JSONObject) groupElement;
      Long groupId = group.getBigInteger("id").longValue();
      String title = group.getString("title");
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
        for (User user: userList) {
          if (userRepository.findOneByName(user.getName()) == null) {
            userRepository.save(user);
            logger.info("Couldn't find user. Added " + user.getName());
          }
          groupRepository.save(new Group(groupId, title, userList));
          logger.info("Saved group '" + title + "' with id " + groupId + " containing " + userList.size() + " members");
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

}