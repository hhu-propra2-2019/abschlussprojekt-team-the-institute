package mops.portfolios;


import javax.persistence.EntityManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.demodata.DemoDataGenerator;
import mops.portfolios.domain.entry.EntryFieldRepository;
import mops.portfolios.domain.entry.EntryRepository;
import mops.portfolios.domain.portfolio.Portfolio;
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

  final @NonNull EntityManager entityManager;
  final @NonNull PortfolioRepository repository;

  /** Starts the application.
   * @param args - command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(PortfoliosApplication.class, args);
  }

  /**
   * Set the commandLine inputs.
   * @param portfolioRepository - the portfolioRepo to inject
   * @param entryRepository - the entryRepo to inject
   * @param entryFieldRepository - the entryFieldRepo to inject
   * @return - the command line args.
   */
  @Bean
  public CommandLineRunner demo(PortfolioRepository portfolioRepository,
                                EntryRepository entryRepository,
                                EntryFieldRepository entryFieldRepository) {
    return (args) -> {
      /*
          Set<String> roles = new HashSet<>(Arrays.asList("student"));
          User user1 = new User("User1", "mail1@example.com", null, roles, "UUID-1234-5678");
          User user2 = new User("User2", "mail2@example.com", null, roles, "UUID-4321-9876");

          Group group1 = new Group(123L, "Group1");

          UserGroup userGroup1 = new UserGroup(user1.getId(), group1.getId(), "title11111111111");
          UserGroup userGroup2 = new UserGroup(user2.getId(), group1.getId(), "title22222222222");

          repository.save(userGroup1);
          repository.save(userGroup2); */
      String line = "============================================================"
          + "======================";
      log.info("1" + line);
      log.info("2" + line);
      log.info("3" + line);
      log.info("4" + line);
      log.info("5" + line);
      log.info("6" + line);
      log.info("7" + line);

      DemoDataGenerator demo = new DemoDataGenerator();

      for (int i = 0; i < 10; i++) {
        repository.save(demo.generateUserPortfolio());
        repository.save(demo.generateGroupPortfolio());
      }



      for (Portfolio portfolio : repository.findAll()) {
        log.info(portfolio.toString());
      }

    };
  }
}
