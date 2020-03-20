package mops.portfolios.security;

import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupService;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.user.User;
import mops.portfolios.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSecurity {

  private transient Portfolio portfolio;

  @Autowired
  private transient UserService userService;
  private transient GroupService groupService;

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

  public boolean isAllowedToViewOrEditPortfolio (String userName, Portfolio portfolio) {
    if (null != portfolio.getGroupId()) {
      Group group = groupService.getGroup(portfolio.getGroupId());
      return userService.isUserNameInGroup(userName, group);
    }

    return portfolio.getUserId().equals(userName);
  }

}
