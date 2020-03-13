package mops.portfolios;

import mops.portfolios.domain.portfolio.Portfolio;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

  private transient Portfolio portfolio;

  public boolean hasGroupId (String userId) {

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

  public boolean hasUserId (String userId) {

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
