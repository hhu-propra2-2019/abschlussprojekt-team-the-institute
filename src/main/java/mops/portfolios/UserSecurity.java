package mops.portfolios;

import mops.portfolios.domain.portfolio.Portfolio;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

  private transient Portfolio portfolio;

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
