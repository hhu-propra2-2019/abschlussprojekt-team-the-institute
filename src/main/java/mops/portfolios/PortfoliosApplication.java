package mops.portfolios;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.Domain.Portfolio.Portfolio;
import mops.portfolios.Domain.Portfolio.PortfolioRepository;
import mops.portfolios.Domain.UserGroup.User;
import mops.portfolios.Domain.UserGroup.Group;
import mops.portfolios.Domain.UserGroup.UserGroup;
import mops.portfolios.Domain.UserGroup.UserGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class PortfoliosApplication {

    private static final Logger log = LoggerFactory.getLogger(PortfoliosApplication.class);

    final @NonNull EntityManager entityManager;
    final @NonNull UserGroupRepository repository;

    public static void main(String[] args) {

        SpringApplication.run(PortfoliosApplication.class, args);

    }

    @Bean
    public CommandLineRunner demo(UserGroupRepository userGroupRepository, PortfolioRepository portfolioRepository) {
        return (args) -> {
            Set<String> roles = new HashSet<>(Arrays.asList("student"));

            User user1 = new User("User1", "mail1@example.com", null, roles, "UUID-1234-5678");
            User user2 = new User("User2", "mail2@example.com", null, roles, "UUID-4321-9876");

            Group group1 = new Group(123L, "Group1");

            UserGroup userGroup1 = new UserGroup(user1.getId(), group1.getId(), "title11111111111");
            UserGroup userGroup2 = new UserGroup(user2.getId(), group1.getId(), "title22222222222");

            Portfolio portfolio1 = new Portfolio("User 1 Portfolio 1", user1);
            Portfolio portfolio2 = new Portfolio("User 2 Portfolio 1", user2);
            Portfolio portfolio3 = new Portfolio("Group Portfolio 1", group1);

            userGroupRepository.save(userGroup1);
            userGroupRepository.save(userGroup2);

            portfolioRepository.save(portfolio1);
            portfolioRepository.save(portfolio2);
            portfolioRepository.save(portfolio3);

            log.info(" 1 ==================================================================================");
            log.info(" 2 ==================================================================================");
            log.info(" 3 ==================================================================================");
            log.info(" 4 ==================================================================================");
            log.info(" 5 ==================================================================================");
            log.info(" 6 ==================================================================================");
            log.info(" 7 ==================================================================================");


            for (UserGroup userGroup : userGroupRepository.findAll()) {
                log.info(userGroup.toString());
            }

            for (Portfolio portfolio : portfolioRepository.findAll()) {
                log.info(portfolio.toString());
            }

            for (Portfolio portfolio : portfolioRepository.findAllByUserId(user1.getId())) {
                log.info("portfolioRepository.findAllByUserId");
                log.info(portfolio.toString());
            }

            for (Portfolio portfolio : portfolioRepository.findAllByGroupId(group1.getId())) {
                log.info("portfolioRepository.findAllByGroupId");
                log.info(portfolio.toString());
            }

        };
    }
}
