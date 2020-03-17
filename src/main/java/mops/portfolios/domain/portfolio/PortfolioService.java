package mops.portfolios.domain.portfolio;

import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.usergroup.UserGroup;
import mops.portfolios.domain.usergroup.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository repository;

    public List<Portfolio> findAllByUserId(String userId) {
        return repository.findAllByUserId(userId);
    }

    public List<Portfolio> findAllByGroupId(Long groupId) {
        return repository.findAllByGroupId(groupId);
    }

    public List<Portfolio> findAll() {
        List<Portfolio> portfolioList = new ArrayList<>();
        Iterator iterator = repository.findAll().iterator();
        while(iterator.hasNext()) {
            portfolioList.add((Portfolio) iterator.next());
        }
        return portfolioList;
    }

    public List<Portfolio> findFirstFew() {
        return findAll().subList(0, 10);
    }

    public List<Portfolio> getGroupPortfolios(UserGroupService userGroupService, String userId) {
        List<UserGroup> userGroups = userGroupService.findAllByUserId(userId);
        List<Long> groups = new ArrayList<>();

        for (UserGroup u: userGroups){
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

    public Entry findEntryById(Portfolio portfolio, Long id) {
        for(Entry entry : portfolio.getEntries()) {
            if (entry.getId() == id) {
                return entry;
            }
        }
        return null;
    }
}
