package mops.portfolios;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;

public class FakeHttpClient implements IHttpClient {

  @Override
  public String get(String url) throws HttpClientErrorException {
    return null;
  }

  @Override
  public String postRequest(String url, String requestBody, HttpHeaders headers) throws HttpClientErrorException {
    return null;
  }
}
