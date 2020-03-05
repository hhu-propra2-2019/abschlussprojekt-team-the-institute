package mops.portfolios;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class HttpClient {

  private final CloseableHttpClient httpClient = HttpClients.createDefault();

  /**
   * HTTP GET url and return body as String.
   * @param url The url to send the HTTP GET request to
   * @return String - The body of the HTTP response as plain String
   * @throws HttpClientErrorException if an HTTP error occured
   * @author mkasimd & hanic101
   */
  String getBody(String url) throws HttpClientErrorException {

    HttpGet request = new HttpGet(url);

    RestTemplate template = new RestTemplate();
    ResponseEntity<String> entity = template.getForEntity(url, String.class);
    if(entity.getStatusCode().isError())
      throw new HttpClientErrorException(entity.getStatusCode());
    return  entity.getBody();
  }

}
