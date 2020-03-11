package mops.portfolios;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class DatabaseUpdater implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

  @Override
  public void run() {
    updateDatabaseEvents();
  }

  void updateDatabase() throws InterruptedException {
    Thread.sleep(10_000);
    run();
  }


  /**
   * Use this method to get the updates from Gruppenbildung regarding groups.
   */
  private void updateDatabaseEvents() {
    HttpClient httpClient = new HttpClient();
    String responseBody = null;

    // try to receive data from service Gruppenbildung
    try {
      // TODO: genaues URI mit gruppen2 absprechen
      responseBody = httpClient.get("/gruppen2/groupmembers");
    } catch (HttpClientErrorException clientErr) { // if status 4xx or 5xx returned
      logger.warn("The service Gruppenbildung is not reachable ", clientErr);
      // keep responseBody null or empty here
    }

    // if couldn't retrieve data or not modified, keep the current state
    if (responseBody == null || responseBody.isEmpty()) {
      // TODO: use data from local database
      logger.info("Database not modified");
      return; // no need to update local database
    }

    // check for possible errors
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(responseBody);
    } catch (JSONException jsonErr) {
      logger.error("An error occured while parsing the JSON data "
              + "received by the service Gruppenbildung ", jsonErr);
      // FIXME: keep this only while in development
      throw new RuntimeException("Error while trying to parse HTTP response to JSON object");
    }
    if (jsonObject == null) {
      // FIXME: Keep this only while in development
      logger.error("An error occured while parsing the JSON data "
              + "received by the service Gruppenbildung");
      throw new RuntimeException("JSON Object is null");
    }

    // TODO: Process the received data
  }

}
