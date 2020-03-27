package mops.portfolios.tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

public class HttpClient implements IHttpClient {

  @Override
  public String get(String url) throws HttpClientErrorException, ConnectException {
    RestTemplate template = new RestTemplate();
    ResponseEntity<String> response = template.getForEntity(url, String.class);
    if (response.getStatusCode().isError()) { // Error means 4xx or 5xx
      throw new HttpClientErrorException(response.getStatusCode());
    }

    String body;
    if (response.getStatusCode().equals(HttpStatus.NOT_MODIFIED)) {
      body = new String();  // empty String
    } else {
      body = response.getBody();
    }

    return  body;
  }

  @Override
  public String post(String url, String requestBody, HttpHeaders headers)
          throws HttpClientErrorException, ConnectException {
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
