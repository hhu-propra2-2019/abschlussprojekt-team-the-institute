package mops.portfolios.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest
public class HttpClientTest {

  @Test
  void testExistentUriLoads() {
    HttpClient httpClient = new HttpClient();
    httpClient.get("https://www.google.com");
  }

  @Test
  void testNoneExistentUriLoads() {
    Assertions.assertThrows(HttpClientErrorException.class, () -> {
      HttpClient httpClient = new HttpClient();
      httpClient.get("http://hc.apache.org/haha");
    });
  }

  @Test
  void testResponseBody() {
    HttpClient httpClient = new HttpClient();
    String body = httpClient.get("https://raw.githubusercontent.com/leachim6/hello-world/master/t/plain-text.txt");
    Assertions.assertEquals("Hello World!\n", body);
  }

  @Test
  void testKeycloakRequest() {
    HttpClient httpClient = new HttpClient();
    String url = "https://postman-echo.com/post";

    // create and set headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    String requestBody = "{\"somekey\":" + "\"somevalue\"}";

    httpClient.post(url, requestBody, headers); // as long as no Exception, it worked
  }

}
