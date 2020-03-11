package mops.portfolios;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class DatabaseUpdater {
  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);
  private DatabaseUpdaterThread databaseUpdaterThread = new DatabaseUpdaterThread();

  /**
   * The thread to run the updates.
   */
  private class DatabaseUpdaterThread implements Runnable {
    @Override
    public void run() {
      updateDatabaseEvents();
    }
  }

  /**
   * Runs the database updater.
   * @param timeout The timeout between each update
   * @throws InterruptedException if another thread has interrupted the current thread. \
   The interrupted status of the current thread is cleared when this exception is thrown.
   */
  public void updateDatabase(long timeout) throws InterruptedException {
    while (true) {
      databaseUpdaterThread.wait(timeout);
      databaseUpdaterThread.run();
    }
  }

  /**
   * Use this method to get the updates from Gruppenbildung regarding groups.
   */
  private void updateDatabaseEvents() {
    HttpClient httpClient = new HttpClient();
    String url = "/gruppen2/groupmembers";
    updateDatabaseEvents(httpClient, url);
  }

  /**
   * This method updates the database using an injected HttpClient and url, easing up testing.
   * @param httpClient The HttpClient to use
   * @param url The URL to retrieve the data from
   */
  private void updateDatabaseEvents(HttpClient httpClient, String url) {
    String responseBody = null;

    // try to receive data from service Gruppenbildung
    try {
      // TODO: genaues URI mit gruppen2 absprechen
      responseBody = httpClient.get(url);
    } catch (HttpClientErrorException clientErr) { // if status 4xx or 5xx returned
      logger.warn("The service Gruppenbildung is not reachable ", clientErr);
      // keep responseBody null or empty here
    }

    updateDatabaseEvents(responseBody);
  }

  /**
   * This method updates the database using the injected JSON String, easing up testing.
   * @param jsonUpdate The String containing the JSON data to update the database
   */
  private void updateDatabaseEvents(String jsonUpdate) {
    // if couldn't retrieve data or not modified, keep the current state
    if (jsonUpdate == null || jsonUpdate.isEmpty()) {
      // TODO: use data from local database
      logger.info("Database not modified");
      return; // no need to update local database
    }

    // check for possible errors
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(jsonUpdate);
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
