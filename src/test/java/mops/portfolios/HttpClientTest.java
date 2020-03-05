package mops.portfolios;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest
public class HttpClientTest {

  @Test
  void testExistentUriLoads() {
    HttpClient httpClient = new HttpClient();
    httpClient.getBody("https://www.google.com");
  }

  @Test
  void testNoneExistentUriLoads() {
    HttpClientErrorException exception = Assertions.assertThrows(HttpClientErrorException.class, () -> {
      HttpClient httpClient = new HttpClient();
      httpClient.getBody("http://hc.apache.org/haha");
    });
  }

  @Test
  void testResponseBody() {
    HttpClient httpClient = new HttpClient();
    String body = httpClient.getBody("https://raw.githubusercontent.com/leachim6/hello-world/master/t/plain-text.txt");
    Assertions.assertEquals("Hello World!\n", body);
  }

}
