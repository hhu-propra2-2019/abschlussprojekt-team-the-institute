package mops.portfolios.tools;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;

public interface IHttpClient {

  /**
   * HTTP GET url and return body as String.
   * @param url The url to send the HTTP GET request to
   * @return String - The body of the HTTP response as plain String
   * @throws HttpClientErrorException if an HTTP error occured (status code 4xx or 5xx)
   * @author mkasimd & hanic101
   */
  public String get(String url) throws HttpClientErrorException;

  /**
   * HTTP POST requestBody to specified url with specified requestBody body.
   * @param url The url to send the requestBody to
   * @param requestBody The requestBody body
   * @param headers The HTTP headers to send with the requestBody
   * @return String - The body of the HTTP response as plain String
   * @throws HttpClientErrorException if an HTTP error occured
   * @author mkasimd & hanic101
   */
  public String post(String url, String requestBody, HttpHeaders headers)
          throws HttpClientErrorException;
}
