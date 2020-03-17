package mops.portfolios.domain.portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository repository;

    public List<Portfolio> findAllByUserId(String userId) {
        return repository.findAllByUserId(userId);
    }
}
