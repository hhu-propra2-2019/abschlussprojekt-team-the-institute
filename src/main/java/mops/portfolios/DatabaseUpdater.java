package mops.portfolios;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.user.User;
import mops.portfolios.tools.HttpClient;
import mops.portfolios.tools.IHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@SuppressWarnings("PMD")
public class DatabaseUpdater {
  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);
  transient String url;

  @Autowired
  GroupRepository groupRepository;

  //  @Autowired
  //  StateService stateService;


  /**
   * The thread to run the updates.
   */
  @AllArgsConstructor
  private class DatabaseUpdaterThread implements Runnable {
    private long timeout;

    @SneakyThrows
    @Override
    public void run() {
      while (true) {
        getUpdatesFromJsonObject();
        Thread.sleep(timeout);
      }
    }
  }

  /**
   * Runs the database updater.
   * @param timeout The timeout between each update
   * @throws InterruptedException if another thread has interrupted the current thread. \
   The interrupted status of the current thread is cleared when this exception is thrown.
   */
  public void updateDatabase(long timeout) throws InterruptedException {
    long updateStatus = 0; // TODO: insert "stateService.getState("gruppen2");"
    this.url = "/gruppen2/api/updateGroups/" + updateStatus;
    DatabaseUpdaterThread databaseUpdaterThread = new DatabaseUpdaterThread(timeout);
    databaseUpdaterThread.run();
  }

  /**
   * Use this method to get the updates from Gruppenbildung regarding groups.
   */

  void getUpdatesFromJsonObject() {
    HttpClient httpClient = new HttpClient();
    getGroupUpdatesFromUrl(httpClient, this.url);
  }

  /**
   * This method updates the database using an injected HttpClient and url, easing up testing.
   * @param httpClient The IHttpClient to use
   */
  @SuppressWarnings("PMD")
  void getGroupUpdatesFromUrl(IHttpClient httpClient, String url) {
    String responseBody;

    // try to receive data from service Gruppenbildung
    try {
      responseBody = httpClient.get(url);
    } catch (HttpClientErrorException clientErr) { // if status 4xx or 5xx returned
      logger.warn("The service Gruppenbildung is not reachable: " + clientErr.getRawStatusCode()
              + " " + clientErr.getStatusText());
      responseBody = null;
    } catch (IllegalArgumentException argException) {
      logger.error(argException.getMessage()); // Most likely URL formatted wrong
      throw new RuntimeException(argException);
    }

    updateDatabaseEvents(responseBody);
  }

  /**
   * This method updates the database using the injected JSON String, easing up testing.
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
      throw new RuntimeException("Error while trying to parse HTTP response to JSON object: "
              + jsonErr.getMessage());
    }
    if (jsonObject == null) {
      logger.error("An error occured while parsing the JSON data "
              + "received by the service Gruppenbildung");
      // FIXME: Keep this only while in development
      throw new RuntimeException("JSON Object is null");
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

    try {
      newStatus = jsonObject.getBigInteger("status").longValue();
      // stateService.setState("gruppen2", newStatus);

    } catch (Exception e) {
      logger.error("Couldn't parse JSONObject:" + e.getMessage());
      throw e;
    }
  }

  /**
   * Checks if there are any modifications.
   * @param jsonUpdate The JSONObject to check
   * @return <b>true</b> if not modified, <b>false</b> if modified
   */
  public boolean isNotModified(JSONObject jsonUpdate) {
    JSONArray groupList = jsonUpdate.getJSONArray("groupList");
    return groupList.isEmpty();
  }

  void processGroupUpdates(JSONObject jsonUpdate) {

    try {
      JSONArray groupList = jsonUpdate.getJSONArray("groupList");

      for (Object groupElement : groupList) {

        JSONObject group = (JSONObject) groupElement;
        long groupId = group.getBigInteger("id").longValue();
        String title = group.getString("title");
        JSONArray members = group.getJSONArray("members");

        List<User> userList = new ArrayList<>();

        for (Object member: members) {
          userList.add((User) member);
        }

        if (title == null || title.isEmpty()) {
          groupRepository.deleteById(groupId);
        } else if (groupExists(groupId)) {
          groupRepository.deleteById(groupId);
        }
        // TODO: Add once the own method is implemented
        //groupRepository.save(new Group(groupId, title, userList));
      }
    } catch (Exception e) {
      logger.error("Couldn't parse JSONObject:" + e.getMessage());
      throw e;
    }
  }

  boolean groupExists(Long groupId) {
    List<Long> groupIds = new ArrayList<>();
    groupIds.add(groupId);
    return groupRepository.findAllById(groupIds) != null;
  }

}
