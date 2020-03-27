package mops.portfolios;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.demodata.DemoDataGenerator;
import mops.portfolios.domain.portfolio.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class PortfoliosApplication {

  private static final Logger log = LoggerFactory.getLogger(PortfoliosApplication.class);

  final @NonNull PortfolioRepository repository;

  /**
   * Starts the application.
   *
   * @param args - command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(PortfoliosApplication.class, args);
  }

  /**
   * Provides demodata.
   */
  @Bean
  public CommandLineRunner demo() {
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
