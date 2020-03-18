package mops.portfolios;

import mops.portfolios.domain.portfolio.Portfolio;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

  private transient Portfolio portfolio;

  /**
   * Checks if the user is allowed to view this portfolio.
   * @param userId - the id of the user opening the portfolio
   * @return - if portfolio.userId equals userId
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
