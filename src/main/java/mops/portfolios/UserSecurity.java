package mops.portfolios;

import mops.portfolios.Domain.Portfolio.Portfolio;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

  private transient Portfolio portfolio;

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
