package mops.portfolios.tools;

import javax.annotation.PostConstruct;
import ch.qos.logback.classic.Logger;
import mops.portfolios.PortfoliosApplication;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.state.StateService;
import mops.portfolios.domain.user.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledDatabaseUpdater {
  private transient String url = "https://raw.githubusercontent.com/Azure/azure-docs-json-samples/master/cosmos-db/cosmos-db-table.json";

  transient GroupRepository groupRepository;

  transient UserRepository userRepository;

  transient StateService stateService;

  transient DatabaseUpdater databaseUpdater;

  @Autowired
  public ScheduledDatabaseUpdater(GroupRepository groupRepository, UserRepository userRepository,
                                  StateService stateService) {
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.stateService = stateService;
    this.databaseUpdater = new DatabaseUpdater(groupRepository, userRepository, stateService);
    this.databaseUpdater.url = this.url;
  }

  /**
   * Runs the database updater with a fixed timeout.
   */
  @PostConstruct
  @Scheduled(fixedDelay = 10_000)
  public void updateDatabase() {
    this.url = "200"; // FIXME: Only call updateDatabase(long timeout) later here
    databaseUpdater.getGroupUpdatesFromUrl(new FakeHttpClient(), this.url);
  }

}
