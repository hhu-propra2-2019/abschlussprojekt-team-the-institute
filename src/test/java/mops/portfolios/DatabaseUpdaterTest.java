package mops.portfolios;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import manifold.ext.api.Jailbreak;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DatabaseUpdaterTest {

  @Jailbreak DatabaseUpdater databaseUpdater;

  /** The url to retrieve the data from */
  private String url = "/gruppen2/groupmembers";

  @Test
  public void testClientError() {
    databaseUpdater = new DatabaseUpdater("400");
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    Logger logger = (Logger) LoggerFactory.getLogger(PortfoliosApplication.class);

    listAppender.start();
    logger.addAppender(listAppender);
    IHttpClient httpClient = new FakeHttpClient();
    databaseUpdater.updateDatabaseEvents(httpClient);
    listAppender.stop();

    List<ILoggingEvent> logsList = listAppender.list;
    int logSize = logsList.size();

    assertEquals("The service Gruppenbildung is not reachable: 400 BAD_REQUEST",
            logsList.get(logSize - 2).getMessage());
    assertEquals("Database not modified", logsList.get(logSize - 1).getMessage());
  }

  @Test
  public void testEmptyJson() {
    databaseUpdater = new DatabaseUpdater(this.url);
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    Logger logger = (Logger) LoggerFactory.getLogger(PortfoliosApplication.class);

    listAppender.start();
    logger.addAppender(listAppender);
    IHttpClient httpClient = new FakeHttpClient();
    databaseUpdater.updateDatabaseEvents("");
    listAppender.stop();

    List<ILoggingEvent> logsList = listAppender.list;
    int logSize = logsList.size();

    assertEquals("Database not modified", logsList.get(logSize - 1).getMessage());
  }

  @Test
  public void objectNotJson() {
    databaseUpdater = new DatabaseUpdater(this.url);
    assertThrows(RuntimeException.class, () -> databaseUpdater.updateDatabaseEvents("blabla"));
  }

  @Test
  public void testUpdateDatabaseEventsIllegalArgument() {
    databaseUpdater = new DatabaseUpdater("/bla/bla");
    assertThrows(RuntimeException.class, () -> databaseUpdater.updateDatabaseEvents());
  }


}
