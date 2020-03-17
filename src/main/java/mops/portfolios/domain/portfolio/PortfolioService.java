package mops.portfolios.domain.portfolio;

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

    public Portfolio findById(Long id) {
        return repository.findById(id).get();
    }

    public List<Portfolio> findFirstFew() {
        List<Portfolio> portfolioList = new ArrayList<>();
        Iterator iterator = repository.findAll().iterator();
        for(int i = 0; i < 8; i++) {
            portfolioList.add((Portfolio) iterator.next());
        }
        return portfolioList;
    }
}
