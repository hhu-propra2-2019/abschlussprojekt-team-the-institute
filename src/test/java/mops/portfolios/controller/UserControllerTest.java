package mops.portfolios.controller;

import mops.portfolios.AccountService;
import mops.portfolios.domain.entry.EntryService;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.user.User;
import mops.portfolios.domain.user.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

  private transient PortfolioService portfolioService = mock(PortfolioService.class);
  private transient AccountService accountService = mock(AccountService.class);
  private transient UserService userService = mock(UserService.class);
  private transient EntryService entryService = mock(EntryService.class);

  private transient UserController userController;

  @BeforeEach
  void init() {
    userController = new UserController(accountService,userService,portfolioService,entryService);
  }

  @Test
  void getPortfolioWithNewEntryTest() {
    User user = new User();
    user.setName("studentin");
    Portfolio portfolio = new Portfolio("Lorem", user);
    when(portfolioService.findPortfolioById(1L)).thenReturn(portfolio);

    Portfolio newPortfolio = userController.getPortfolioWithNewEntry(1L, "Lorem");

    Assert.assertEquals(portfolio, newPortfolio);

  }
}
