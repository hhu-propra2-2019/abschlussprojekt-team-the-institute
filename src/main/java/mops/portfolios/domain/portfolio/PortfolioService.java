package mops.portfolios.domain.portfolio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.usergroup.UserGroup;
import mops.portfolios.domain.usergroup.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PortfolioService {

  @Autowired
  transient PortfolioRepository repository;

  public List<Portfolio> findAllByUserId(String userId) {
    return repository.findAllByUserId(userId);
  }

  public List<Portfolio> findAllByGroupId(Long groupId) {
    return repository.findAllByGroupId(groupId);
  }

  /**
   * Returns all portfolios in the repository.
   * @return - all Portfolios
   */
  public List<Portfolio> findAll() {
    List<Portfolio> portfolioList = new ArrayList<>();
    Iterator iterator = repository.findAll().iterator();
    while (iterator.hasNext()) {
      portfolioList.add((Portfolio) iterator.next());
    }
    return portfolioList;
  }

  /**
   * Returns the first ten portfolios.
   * @return - the first ten.
   */
  public List<Portfolio> findFirstFew() {
    return findAll().subList(0, 10);
  }

  /**
   * Returns all group-portfolios for a user.
   * @param userGroupService - injects the userGroupService that will be used
   * @param userId - the Id of the user we are checking for
   * @return - the group portfolios
   */
  @SuppressWarnings("PMD")
  public List<Portfolio> getGroupPortfolios(UserGroupService userGroupService, String userId) {
    List<UserGroup> userGroups = userGroupService.findAllByUserId(userId);
    List<Long> groups = new ArrayList<>();

    for (UserGroup u: userGroups) {
      groups.add(u.getGroupId());
    }

    List<Portfolio> q = new ArrayList<>();

    for (Long l : groups) {
      q.addAll(findAllByGroupId(l));
    }

    return q;
  }

  public Portfolio findPortfolioById(Long id) {
    return repository.findById(id).get();
  }

  /**
   * Finds the entry of the portfolio with the corresponding id.
   * @param portfolio - the portfolio
   * @param id - the id of the entry
   * @return - the entry.
   */
  @SuppressWarnings("PMD")
  public Entry findEntryById(Portfolio portfolio, Long id) {
    for (Entry entry : portfolio.getEntries()) {
      if (entry.getId() == id) {
        return entry;
      }
    }
    return null;
  }

  /**
   *  Finds all Portfolios that are templates
   * @return - list of portfolios
   */
  @SuppressWarnings("PMD")
  public List<Portfolio> getAllTemplates() {
    List<Portfolio> templates = new ArrayList<>();
    List<Portfolio> allPortfolios = findAll();
    for(Portfolio p : allPortfolios) {
      if (p.isTemplate()) {
        templates.add(p);
      }
    }
    return templates;
  }

  /**
   * Finds all actual non-template portfolios
   * @return - list of portfolios
   */
  @SuppressWarnings("PMD")
  public List<Portfolio> getAllPortfolios() {
    List<Portfolio> templates = new ArrayList<>();
    List<Portfolio> allPortfolios = findAll();
    for(Portfolio p : allPortfolios) {
      if (!p.isTemplate()) {
        templates.add(p);
      }
    }
    return templates;
  }
}
