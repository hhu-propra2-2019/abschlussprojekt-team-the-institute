package mops.portfolios;

import java.util.List;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.state.StateService;
import mops.portfolios.domain.user.UserRepository;
import mops.portfolios.tools.FakeHttpClient;
import mops.portfolios.tools.IHttpClient;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DatabaseUpdaterTest {

  private transient DatabaseUpdater databaseUpdater;

  /** The url to retrieve the data from */
  private transient String url = "/gruppen2/groupmembers";
  private transient static final Logger logger = (Logger) LoggerFactory.getLogger(PortfoliosApplication.class);

  @Autowired
  transient GroupRepository groupRepository;

  @Autowired
  transient UserRepository userRepository;

  @Autowired
  transient StateService stateService;

  @BeforeEach
  public void init() {
    databaseUpdater = new DatabaseUpdater(groupRepository, userRepository, stateService);
  }

  @Test
  public void testClientError() {
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    assertThrows(RuntimeException.class, () -> {
      listAppender.start();
      logger.addAppender(listAppender);
      IHttpClient httpClient = new FakeHttpClient();
      databaseUpdater.getGroupUpdatesFromUrl(httpClient, "400");
      listAppender.stop();

      List<ILoggingEvent> logsList = listAppender.list;
      int logSize = logsList.size();

      assertEquals("The service Gruppenbildung is not reachable: 400 BAD_REQUEST",
              logsList.get(logSize - 2).getMessage());
      assertEquals("An error occured while parsing the JSON data received by the service Gruppenbildung", logsList.get(logSize - 1).getMessage());
    });
  }

  @Test
  public void testEmptyJson() {
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    listAppender.start();
    logger.addAppender(listAppender);
    databaseUpdater.updateDatabaseEvents("{\"status\":4,\"groupList\":[]}");
    listAppender.stop();

    List<ILoggingEvent> logsList = listAppender.list;
    int logSize = logsList.size();

    assertEquals("Database not modified", logsList.get(logSize - 1).getMessage());
  }

  @Test
  public void objectNotJson() {
    assertThrows(RuntimeException.class, () -> databaseUpdater.updateDatabaseEvents("blabla"));
  }

  @Test
  public void testUpdateDatabaseEventsIllegalArgument() {
    databaseUpdater.url = "/bla/bla";
    assertThrows(RuntimeException.class, () -> databaseUpdater.getUpdatesFromJsonObject());
  }
  @Test
  public void testSuccessfulRequest() {
    IHttpClient httpClient = new FakeHttpClient();
    databaseUpdater.getGroupUpdatesFromUrl(httpClient, this.url); // takes mocked JSON from the FakeHttpClient
  }

  @Test
  public void GroupListNotModified() {
    // as talked with gruppen2, this is how the response will look if not modified
    String response = "{\"status\":4,\"groupList\":[]}";
    JSONObject jsonObject = new JSONObject(response);

    boolean result = databaseUpdater.isNotModified(jsonObject);
    assertEquals(true, result);
  }

  @Test
  public void GroupListIsModified() {
    String response = "{\n" +
            "  \"status\": 4,\n" +
            "  \"groupList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"title\": null,\n" +
            "      \"description\": null,\n" +
            "      \"members\": [\n" +
            "        {\n" +
            "          \"user_id\": \"studentin\",\n" +
            "          \"givenname\": \"studentin\",\n" +
            "          \"familyname\": \"studentin\",\n" +
            "          \"email\": \"studentin@student.in\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"roles\": {\n" +
            "\"studentin\": \"ADMIN\"" +
            "},\n"+
            "      \"type\": \"LECTURE\",\n" +
            "      \"visibility\": \"PUBLIC\",\n" +
            "      \"parent\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    JSONObject jsonObject = new JSONObject(response);

    boolean result = databaseUpdater.isNotModified(jsonObject);
    assertEquals(false, result);
  }

  @Test
  public void extractJsonObject() {
    String response = "{\n" +
            "  \"status\": 4,\n" +
            "  \"groupList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"title\": \"Lorem\",\n" +
            "      \"description\": null,\n" +
            "      \"members\": [\n" +
            "        {\n" +
            "          \"user_id\": \"studentin\",\n" +
            "          \"givenname\": \"studentin\",\n" +
            "          \"familyname\": \"studentin\",\n" +
            "          \"email\": \"studentin@student.in\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"roles\": {\n" +
            "\"studentin\": \"ADMIN\"" +
            "},\n"+
            "      \"type\": \"LECTURE\",\n" +
            "      \"visibility\": \"PUBLIC\",\n" +
            "      \"parent\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    databaseUpdater.updateDatabaseEvents(response);
  }

}
