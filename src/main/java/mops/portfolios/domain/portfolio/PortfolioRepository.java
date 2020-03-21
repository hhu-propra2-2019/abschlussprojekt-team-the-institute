package mops.portfolios.domain.portfolio;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {

  @Override
  List<Portfolio> findAll();

  List<Portfolio> findAllByUserId(String userId);

  List<Portfolio> findAllByGroupId(Long groupId);

  List<Portfolio> findAllByGroupIdIn(List<Long> groupIdList);

}
