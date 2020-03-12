package mops.portfolios;

import java.util.List;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import manifold.ext.api.Jailbreak;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DatabaseUpdaterTest {

  @Jailbreak private transient DatabaseUpdater databaseUpdater;

  /** The url to retrieve the data from */
  private transient String url = "/gruppen2/groupmembers";
  private transient static final Logger logger = (Logger) LoggerFactory.getLogger(PortfoliosApplication.class);

  @BeforeEach
  public void init() {
    databaseUpdater = new DatabaseUpdater();
  }

  @Test
  public void testClientError() {
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    listAppender.start();
    logger.addAppender(listAppender);
    IHttpClient httpClient = new FakeHttpClient();
    databaseUpdater.updateDatabaseEvents(httpClient, "400");
    listAppender.stop();

    List<ILoggingEvent> logsList = listAppender.list;
    int logSize = logsList.size();

    assertEquals("The service Gruppenbildung is not reachable: 400 BAD_REQUEST",
            logsList.get(logSize - 2).getMessage());
    assertEquals("Database not modified", logsList.get(logSize - 1).getMessage());
  }

  @Test
  public void testEmptyJson() {
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    listAppender.start();
    logger.addAppender(listAppender);
    databaseUpdater.updateDatabaseEvents("");
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
    assertThrows(RuntimeException.class, () -> databaseUpdater.updateDatabaseEvents());
  }
  @Test
  public void testSuccessfulRequest() {
    IHttpClient httpClient = new FakeHttpClient();
    databaseUpdater.updateDatabaseEvents(httpClient, this.url); // takes mocked JSON from the FakeHttpClient
  }


}
