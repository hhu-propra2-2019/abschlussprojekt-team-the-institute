package mops.portfolios.Domain.Portfolio;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {
    
    List<Portfolio> findByUserId(UUID userId);

    List<Portfolio> findAllByGroupId(Long groupId);

}
