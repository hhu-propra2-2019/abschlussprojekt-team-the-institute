package mops.portfolios.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;


@Configuration
public class KeycloakConfig {
  @Bean
  public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  @Value("${keycloak.resource}")
  private transient String clientId;
  @Value("${keycloak.credentials.secret}")
  private transient String clientSecret;
  @Value("${hhu_keycloak.token-uri}")
  private transient String tokenUri;

  /**
   * The RestTemplate for Keycloak.
   * @return the RestTemplate
   */
  @Bean public RestTemplate serviceAccountRestTemplate() {
    ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
    resourceDetails.setGrantType(OAuth2Constants.CLIENT_CREDENTIALS);
    resourceDetails.setAccessTokenUri(tokenUri);
    resourceDetails.setClientId(clientId);
    resourceDetails.setClientSecret(clientSecret);

    return new OAuth2RestTemplate(resourceDetails);
  }
}