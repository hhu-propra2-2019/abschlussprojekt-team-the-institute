package mops.portfolios;

import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioRepository;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PortfolioServiceTest {

    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    PortfolioService portfolioService;

    //test findAllByGroupList
    @Test
    public void findMultiplePortfoliosPerGroupTest() {
        List<Group> groupList = new ArrayList<>();

        Group group1 = new Group(0L, "Group 1", new ArrayList<>());
        Group group2 = new Group(1L, "Group 2", new ArrayList<>());
        groupList.add(group1);
        groupList.add(group2);

        Portfolio portfolio1 = new Portfolio("Portfolio 1", group1);
        Portfolio portfolio2 = new Portfolio("Portfolio 2", group1);
        Portfolio portfolio3 = new Portfolio("Portfolio 3", group2);
        Portfolio portfolio4 = new Portfolio("Portfolio 4", group2);

        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);
        portfolioRepository.save(portfolio3);
        portfolioRepository.save(portfolio4);

        List<Portfolio> listOfGroupPortfolios = portfolioService.findAllByGroupList(groupList);

        assert(listOfGroupPortfolios.contains(portfolio1));
        assert(listOfGroupPortfolios.contains(portfolio2));
        assert(listOfGroupPortfolios.contains(portfolio3));
        assert(listOfGroupPortfolios.contains(portfolio4));

    }

    @Test
    public void findNoEntryByIdInEmptyPortfolioTest() {
        Portfolio testPortfolio = new Portfolio();

        Entry testEntry = portfolioService.findEntryById(testPortfolio, 0L);

        assert(testEntry == null);
    }

    @Test
    public void findEntryByIdTest() {
        Portfolio testPortfolio = new Portfolio();
        HashSet<Entry> entrySet = new HashSet<>();
        Entry testEntry = new Entry("test");
        testEntry.setId(7357L);
        entrySet.add(testEntry);
        testPortfolio.setEntries(entrySet);

        Entry testEntry2 = portfolioService.findEntryById(testPortfolio, 7357L);

        assert(testEntry2.equals(testEntry));
    }

    @Test
    public void getTemplatesOutOfMixedRepositoryTest() {
        Portfolio nonTemplate = new Portfolio("Portfolio 1", new User());
        Portfolio template = new Portfolio("Portfolio 2", new Group());

        template.setTemplate(true);

        portfolioRepository.save(nonTemplate);
        portfolioRepository.save(template);

        List<Portfolio> templates = portfolioService.getAllTemplates();

        assert(templates.contains(template));
        assertFalse(templates.contains(nonTemplate));
    }


    @Test
    public void getNonTemplatesOutOfMixedRepositoryTest() {
        Portfolio nonTemplate = new Portfolio("Portfolio 1", new User());
        Portfolio template = new Portfolio("Portfolio 2", new Group());

        template.setTemplate(true);

        portfolioRepository.save(nonTemplate);
        portfolioRepository.save(template);

        List<Portfolio> templates = portfolioService.getAllPortfolios();

        assert(templates.contains(nonTemplate));
        assertFalse(templates.contains(template));
    }

}
