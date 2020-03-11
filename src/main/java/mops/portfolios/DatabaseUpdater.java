package mops.portfolios;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@AllArgsConstructor
public class DatabaseUpdater {
  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

  /** The URL to retrieve the data from. */
  private String url;

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
        updateDatabaseEvents();
        Thread.sleep(10_000); // 10 seconds interval
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
    DatabaseUpdaterThread databaseUpdaterThread = new DatabaseUpdaterThread(timeout);
    databaseUpdaterThread.run();
  }

  /**
   * Use this method to get the updates from Gruppenbildung regarding groups.
   */
  void updateDatabaseEvents() {
    HttpClient httpClient = new HttpClient();
    updateDatabaseEvents(httpClient);
  }

  /**
   * This method updates the database using an injected HttpClient and url, easing up testing.
   * @param httpClient The HttpClient to use
   */
  void updateDatabaseEvents(IHttpClient httpClient) {
    String responseBody = null;

    // try to receive data from service Gruppenbildung
    try {
      // TODO: genaues URI mit gruppen2 absprechen
      responseBody = httpClient.get(this.url);
    } catch (HttpClientErrorException clientErr) { // if status 4xx or 5xx returned
      logger.warn("The service Gruppenbildung is not reachable: " + clientErr.getRawStatusCode()
              + " " + clientErr.getStatusText());
    // keep responseBody null or empty here
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
  void updateDatabaseEvents(String jsonUpdate) {
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
      throw new RuntimeException("Error while trying to parse HTTP response to JSON object: "
              + jsonErr.getMessage());
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
