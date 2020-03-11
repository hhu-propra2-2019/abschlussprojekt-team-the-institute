package mops.portfolios;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class HttpClient implements IHttpClient {

  @Override
  public String get(String url) throws HttpClientErrorException {
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

  @Override
  public String postRequest(String url, String requestBody, HttpHeaders headers)
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
