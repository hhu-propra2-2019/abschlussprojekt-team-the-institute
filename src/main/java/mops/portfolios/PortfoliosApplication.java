package mops.portfolios;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
    public CommandLineRunner demo(UserGroupRepository repository) {
        return (args) -> {
            Set<String> roles = new HashSet<>(Arrays.asList("student"));

            User user1 = new User("User1", "mail1@example.com", null, roles, "UUID-1234-5678");
            User user2 = new User("User2", "mail2@example.com", null, roles, "UUID-4321-9876");

            Group group1 = new Group(123L, "Group1");

            UserGroup userGroup1 = new UserGroup(user1.getId(), group1.getId(), "title11111111111");
            UserGroup userGroup2 = new UserGroup(user2.getId(), group1.getId(), "title22222222222");

            repository.save(userGroup1);
            repository.save(userGroup2);

            log.info("1==================================================================================");
            log.info("RUN ALL THE THINGS!!!!11");
            log.info("2==================================================================================");
            log.info("3==================================================================================");
            log.info("4==================================================================================");
            log.info("5==================================================================================");
            log.info("6==================================================================================");
            log.info("7 - pmd, oh boi======================================================================");


            for (UserGroup userGroup : repository.findAll()) {
                log.info(userGroup.toString());
            }

        };
    }
}
