package mops.portfolios;


import javax.persistence.EntityManager;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.demodata.DemoDataGenerator;
import mops.portfolios.domain.FileRepository;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryFieldRepository;
import mops.portfolios.domain.entry.EntryRepository;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioRepository;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.state.State;
import mops.portfolios.domain.state.StateService;
import mops.portfolios.domain.user.User;
import org.jruby.RubyProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;


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
    public static void main(String[] args) {
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

            /*
             * This is an example of using FileRepository
             * If you want to test it this way, provide a valid
             * path to any file in the right format for your OS.
             * This example uses a file placed in /tmp/pic.png
             * (Linux)
             */

//            Path path = Paths.get("/tmp/pic.png");
//            String name = "pic.png";
//            String originalFileName = "/tmp/pic.png";
//            String contentType = "image/png";
//
//            byte[] content = null;
//
//            try {
//                content = Files.readAllBytes(path);
//            } catch (final IOException e) {
//                System.out.println(e.toString());
//            }
//
//            MultipartFile result = new MockMultipartFile(name,
//                    originalFileName, contentType, content);
//
//            EntryField entryField = new EntryField();
//
//            fileRepository.saveFile(result, entryField);
//
//            if (entryField.getAttachment() != null)
//                System.out.println(
//                        fileRepository.getFileUrl(entryField.getAttachment())
//                );

        };
    }
}
