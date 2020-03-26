package mops.portfolios;

import java.util.Random;
import javax.persistence.EntityManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.demodata.DemoDataGenerator;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioRepository;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.state.StateService;
import mops.portfolios.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class PortfoliosApplication {

  private static final Logger log = LoggerFactory.getLogger(PortfoliosApplication.class);

  final @NonNull EntityManager entityManager;
  final @NonNull PortfolioRepository repository;
  final @NonNull FileRepository fileRepository;
  final @NonNull EntryFieldRepository entryFieldRepository;

  /**
   * Starts the application.
   *
   * @param args - command-line arguments
   */
  public static void main(String[] args){
    SpringApplication.run(PortfoliosApplication.class, args);
  }

  @Bean
  public CommandLineRunner demo(StateService stateService, PortfolioService portfolioService) {
    return (args) -> {

      DemoDataGenerator demo = new DemoDataGenerator();
      for (int i = 0; i < 4; i++) {
        repository.save(demo.generateUserPortfolio());
        repository.save(demo.generateGroupPortfolio());
        repository.save(demo.generateTemplate());
      }

    };
  }
}
