package mops.portfolios;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class Group {

  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

  Group get(int id) {
    return this;
  } //TODO

  /**
   * Use this method to get the updates from Gruppenbildung regarding groups.
   * @param userId The user ID of the student to get the group members of
   * @return List of the Account of each group member
   */
  List<String> getGroupmembers(String userId) {
    HttpClient httpClient = new HttpClient();

    String responseBody = null;
    try {
      // TODO: genaues URI mit gruppen2 absprechen
      responseBody = httpClient.get("/gruppen2/groupmembers");
    } catch (HttpClientErrorException clientErr) { // if status 4xx or 5xx returned
      logger.warn("The service Gruppenbildung is not reachable ", clientErr);
      // keep responseBody null or empty here
    }

    if (responseBody == null || responseBody.isEmpty()) {
      // TODO: use data from local database
      logger.info("Database not modified");
      return getFromLocalDatabase(userId);
    }

    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(responseBody);
    } catch (JSONException jsonErr) {
      logger.error("An error occured while parsing the JSON data "
              + "received by the service Gruppenbildung ", jsonErr);
      // FIXME: keep this only while in development
      throw new RuntimeException("Error while trying to parse HTTP response to JSON object");
    } // TODO once we get data from gruppen2


    List<String> accountList = new ArrayList<>();
    return accountList;
  }

  private List<String> getFromLocalDatabase(String userId) {
    // TODO List<Group> groups = findGroupsByUserId(userId);
    return null; // FIXME this is a filler
  }

}
