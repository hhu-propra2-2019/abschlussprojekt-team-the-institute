package mops.portfolios.tools;

public enum UrlScheme {
  HTTP("http"), HTTPS("https");

  private final String value;

  private UrlScheme(String urlScheme) {
    this.value = urlScheme;
  }

  @Override
  public String toString() {
    return this.value;
  }

}
