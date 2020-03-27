package mops.portfolios.tools;

import java.util.List;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import mops.portfolios.PortfoliosApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UrlTest {
  private transient static final Logger logger = (Logger) LoggerFactory.getLogger(PortfoliosApplication.class);

  private List<ILoggingEvent> generateUrlAndReturnLogs(String urlString) {
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    listAppender.start();
    logger.addAppender(listAppender);
    new Url(urlString);
    listAppender.stop();

    List<ILoggingEvent> logsList = listAppender.list;
    return logsList;
  }


  @Test
  public void testSchemes() {
    Assertions.assertEquals("http", UrlScheme.HTTP.toString());
    Assertions.assertEquals("https", UrlScheme.HTTPS.toString());
  }

  @Test
  public void testUrlWithoutValidScheme() {
    String urlString = "ftp://mock.hhu.de/";
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      List<ILoggingEvent> logsList = generateUrlAndReturnLogs(urlString);
      int logSize = logsList.size();

      assertEquals("An invalid scheme is used: ftp", logsList.get(logSize - 1).getMessage());
    });
  }

  @Test
  public void testUrlWithoutFinalSlash() {
    String urlString = "ftp://mock.hhu.de";
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      List<ILoggingEvent> logsList = generateUrlAndReturnLogs(urlString);
      int logSize = logsList.size();

      assertEquals("The url does not end with a slash", logsList.get(logSize - 1).getMessage());
    });
  }

  @Test
  public void testUrlWithoutColonAfterScheme() {
    String urlString = "http//mock.hhu.de";
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      List<ILoggingEvent> logsList = generateUrlAndReturnLogs(urlString);
      int logSize = logsList.size();

      assertEquals("The scheme is not followed by '://'", logsList.get(logSize - 1).getMessage());
    });
  }

  @Test
  public void testUrlWithoutSlashesAfterScheme() {
    String urlString = "http:mock.hhu.de";
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      List<ILoggingEvent> logsList = generateUrlAndReturnLogs(urlString);
      int logSize = logsList.size();

      assertEquals("The scheme is not followed by '://'", logsList.get(logSize - 1).getMessage());
    });
  }

  @Test
  public void testUrlWithoutDomain() {
    String urlString = "http://";
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      List<ILoggingEvent> logsList = generateUrlAndReturnLogs(urlString);
      int logSize = logsList.size();

      assertEquals("The url doesn't have a domain or IP", logsList.get(logSize - 1).getMessage());
    });
  }

  @Test
  public void testUrlWithIP() {
    String urlString = "http://127.0.0.1/";
    Url url = new Url(urlString);
    Assertions.assertEquals(urlString, url.toString());
  }

  @Test
  public void testUrlWithPort() {
    String urlString = "http://localhost:8080/";
    Url url = new Url(urlString);
    Assertions.assertEquals(urlString, url.toString());
  }

  @Test
  public void testUrlWithFQDN() {
    String urlString = "http://hhu.de/";
    Url url = new Url(urlString);
    Assertions.assertEquals(urlString, url.toString());
  }

  @Test
  public void testUrlWithPath() {
    String urlString = "http://mock.hhu.de/portfolios/";
    Url url = new Url(urlString);
    Assertions.assertEquals(urlString, url.toString());
  }

  @Test
  public void testUrlWithHttps() {
    String urlString = "https://mock.hhu.de/";
    Url url = new Url(urlString);
    Assertions.assertEquals(urlString, url.toString());
  }

}
