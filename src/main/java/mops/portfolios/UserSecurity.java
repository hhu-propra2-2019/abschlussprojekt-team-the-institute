package mops.portfolios;

import mops.portfolios.domain.portfolio.Portfolio;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

  private transient Portfolio portfolio;

  /** Checks if KeycloakUserId equals Portfolio-userId.
   * @param userId - usedId to check
   * @return - returns true or false
   */
  public boolean hasGroupId(String userId) {

    String userIdOfPortfolio;

    if (portfolio == null) {
      return false;
    }

    userIdOfPortfolio = portfolio.getUserId();

    if (userIdOfPortfolio != null && userIdOfPortfolio.equals(userId)) {
      return true;
    }

    return false;
  }

  /** Checks if KeycloakUserId equals Portfolio-userId.
   * @param userId - usedId to check
   * @return - returns true or false
   */
  public boolean hasUserId(String userId) {

    String userIdOfPortfolio;

    if (portfolio == null) {
      return false;
    }

    userIdOfPortfolio = portfolio.getUserId();

    if (userIdOfPortfolio != null && userIdOfPortfolio.equals(userId)) {
      return true;
    }

    return false;
  }

}
