package mops.portfolios.Portfolio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {
    
    List<Portfolio> findByUserId(Long userId);

    List<Portfolio> findByGroupId(Long groupId);

}
