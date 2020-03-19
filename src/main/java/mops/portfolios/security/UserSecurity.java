package mops.portfolios.security;

import mops.portfolios.domain.portfolio.Portfolio;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

  private transient Portfolio portfolio;

  /**
   * Checks if the user is allowed to view this portfolio.
   * @param userName - the name of the user opening the portfolio
   * @return - if portfolio.userName equals userName
   */
  public boolean hasUserName (String userName) {

    String userNameOfPortfolio;

    if (portfolio == null) {
      return false;
    }

    userNameOfPortfolio = portfolio.getUserId();

    if (userNameOfPortfolio != null && userNameOfPortfolio.equals(userName)) {
      return true;
    }

    return false;
  }

}
