package mops.portfolios.domain.portfolio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.group.Group;
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
    return repository.findAll();
  }

  public List<Portfolio> findAllByGroupList(List<Group> groups) {
    List<Long> ids = groups.stream().map(Group::getId).collect(Collectors.toList());

    return repository.findAllByGroupIdIn(ids);
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
      if (entry.getId().equals(id)) {
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

  public void update(Portfolio portfolio) {
    repository.save(portfolio);
  }
}
