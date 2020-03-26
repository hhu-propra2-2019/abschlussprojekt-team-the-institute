package mops.portfolios.tools;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class FakeHttpClient implements IHttpClient {

  /**
   * Feel free to set this differently.
   */
  public transient String response = "{\"status\":4,\"groupList\":[]}";

  @Override
  public String get(String url) throws HttpClientErrorException {
    return fakeResponse(url);
  }

  @Override
  public String post(String url, String requestBody, HttpHeaders headers)
          throws HttpClientErrorException {
    return fakeResponse(url);
  }

  /**
   * This method fakes a response for get and postRequest to return.
   * @param fakeStatusCode The HTTP status code to fake. \
   *                       Anything other than 4xx or 5xx will be dealt as 200
   * @return The response
   * @throws HttpClientErrorException if the fakeStatusCode
   *                                  is set to an error status code (4xx or 5xx)
   */
  private String fakeResponse(String fakeStatusCode) throws  HttpClientErrorException {
    // possible 4xx Client Errors
    if (fakeStatusCode.contains("400")) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
    }
    if (fakeStatusCode.contains("401")) {
      throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
    }
    if (fakeStatusCode.contains("403")) {
      throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
    }
    if (fakeStatusCode.contains("404")) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
    if (fakeStatusCode.contains("415")) {
      throw new HttpClientErrorException(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // possible 5xx Server Errors
    if (fakeStatusCode.contains("500")) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if (fakeStatusCode.contains("501")) {
      throw new HttpClientErrorException(HttpStatus.NOT_IMPLEMENTED);
    }
    if (fakeStatusCode.contains("502")) {
      throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY);
    }
    if (fakeStatusCode.contains("503")) {
      throw new HttpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE);
    }
    if (fakeStatusCode.contains("504")) {
      throw new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT);
    }
    if (fakeStatusCode.contains("505")) {
      throw new HttpClientErrorException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
    }
    if (fakeStatusCode.contains("506")) {
      throw new HttpClientErrorException(HttpStatus.VARIANT_ALSO_NEGOTIATES);
    }
    if (fakeStatusCode.contains("507")) {
      throw new HttpClientErrorException(HttpStatus.INSUFFICIENT_STORAGE);
    }
    if (fakeStatusCode.contains("508")) {
      throw new HttpClientErrorException(HttpStatus.LOOP_DETECTED);
    }
    if (fakeStatusCode.contains("511")) {
      throw new HttpClientErrorException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    return response;
  }
}
