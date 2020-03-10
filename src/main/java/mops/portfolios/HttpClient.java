package mops.portfolios;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class HttpClient {

  /**
   * HTTP GET url and return body as String.
   * @param url The url to send the HTTP GET request to
   * @return String - The body of the HTTP response as plain String
   * @throws HttpClientErrorException if an HTTP error occured (status code 4xx or 5xx)
   * @author mkasimd & hanic101
   */
  String get(String url) throws HttpClientErrorException {
    RestTemplate template = new RestTemplate();
    ResponseEntity<String> response = template.getForEntity(url, String.class);
    if (response.getStatusCode().isError()) { // Error means 4xx or 5xx
      throw new HttpClientErrorException(response.getStatusCode());
    }

    String body = response.getBody();
    if (response.getStatusCode().equals(HttpStatus.NOT_MODIFIED)) {
      body = "";
    }

    return  body;
  }

  /**
   * HTTP POST requestBody to specified url with specified requestBody body.
   * @param url The url to send the requestBody to
   * @param requestBody The requestBody body
   * @param headers The HTTP headers to send with the requestBody
   * @return String - The body of the HTTP response as plain String
   * @throws HttpClientErrorException if an HTTP error occured
   * @author mkasimd & hanic101
   */
  String postRequest(String url, String requestBody, HttpHeaders headers)
          throws HttpClientErrorException {
    RestTemplate template = new RestTemplate();

    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    System.out.println("Request:\n\n" + request.toString());
    ResponseEntity<String> response = template.postForEntity(url, request, String.class);

    if (response.getStatusCode().isError()) {
      throw new HttpClientErrorException(response.getStatusCode());
    }
    return  response.getBody();
  }

}
