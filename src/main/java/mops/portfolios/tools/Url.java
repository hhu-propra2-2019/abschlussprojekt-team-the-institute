package mops.portfolios.tools;

import mops.portfolios.PortfoliosApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Url {
  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);
  private String url;

  /**
   * Creates an Url object.
   * @param url The URL String to generate the Url Object from. It must be formatted as in following:<br>
   *            <code>[scheme]://[domain]/[path]/</code><br>
   *            The String must start with the scheme (e.g. "http", or "https") and end with a slash.
   * @throws IllegalArgumentException If the entered url String is not formatted correctly.
   */
  public Url(String url) throws IllegalArgumentException {
    logger.info("Generating an Url object from entered String: " +url);
    checkUrl(url); // this may throw an IllegalArgumentException
    this.url = url;
    logger.info("Url object generated succesfully from entered String: " +url);
  }

  private void checkUrl(String url) throws IllegalArgumentException {
    if(url == null) {
      logger.error("Entered url String is null");
      throw new IllegalArgumentException("Entered url String is null");
    }

    try { // while checking, Exceptions may be thrown due to split operations. Catch them
      if (startsWithValidScheme(url) && containsDomain(url) && endsWithSlash(url)) {
        return; // all checks passed. just return without any Exceptions
      }
    } catch (Throwable t) {
      ; // do nothing and throw exception below
    }
    throw new IllegalArgumentException("Entered url String is not formatted correctly. It was: " + url);
  }

  private boolean startsWithValidScheme(String url) {
    String scheme = url.split("/")[0].toLowerCase();
    if (scheme.charAt(scheme.length() - 1) != ':') {
      logger.error("The used scheme is not followed by ':'. The url begins with: " + scheme);
      return false;
    }

    String schemeCopy = new String(scheme);
    scheme = "";
    for (char c : schemeCopy.toCharArray()) {
      if (c == ':') {
        break;
      }
      scheme += c;
    }

    if (!isValidScheme(scheme)) {
      logger.error("An invalid scheme is used: " + scheme);
      return false;
    }

    if (!url.split(":")[1].startsWith("//")) {
      logger.error("The scheme is not followed by '://'");
      return false;
    }

    return true;
  }

  private boolean isValidScheme(String scheme) {
    for (UrlScheme supportedScheme : UrlScheme.values()) {
      if (scheme.equals(supportedScheme.toString())) {
        return true;
      }
    }

    return false;
  }

  // This is logged in startsWithValidScheme
  private boolean containsDomain(String url) {
    String scheme = url.split("/")[0];
    url.replaceFirst(scheme, "");
    url.replaceFirst("//", "/");
    String domain = url.split("/")[0];

    if (domain == null || domain.isEmpty()) {
      return false;
    }

    return true;
  }

  private boolean endsWithSlash(String url) {
    if(url.charAt(url.length()-1) == '/') {
      return true;
    }

    logger.error("The url does not end with a slash");
    return false;
  }

  @Override
  public String toString() {
    return this.url;
  }
}
