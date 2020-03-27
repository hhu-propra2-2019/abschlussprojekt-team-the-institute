package mops.portfolios.tools;

import javax.annotation.PostConstruct;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.state.StateService;
import mops.portfolios.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledDatabaseUpdater {

  /**
   * The URL String to generate the Url Object from. It must be formatted as in following:<br>
   *   <code>[scheme]://[domain]/[path]/</code><br>
   *   The String must start with the scheme (e.g. "http", or "https") and end with a slash.
   */
  private transient String url = "https://raw.githubusercontent.com/Azure/azure-docs-json-samples/master/cosmos-db/cosmos-db-table.json/";

  transient GroupRepository groupRepository;

  transient UserRepository userRepository;

  transient StateService stateService;

  transient DatabaseUpdater databaseUpdater;

  /**
   * Constructor.
   */
  @Autowired
  public ScheduledDatabaseUpdater(GroupRepository groupRepository, UserRepository userRepository,
                                  StateService stateService) {
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.stateService = stateService;
    this.databaseUpdater = new DatabaseUpdater(groupRepository, userRepository, stateService);
    this.databaseUpdater.url = new Url(this.url);
  }

  /**
   * Runs the database updater with a fixed timeout.
   */
  @PostConstruct
  @Scheduled(fixedRate = 10_000)
  public void updateDatabase() {
    // this.url = "200"; // FIXME: Only call getUpdatesFromJsonObject later here
    databaseUpdater.getGroupUpdatesFromUrl(new HttpClient(), this.url);
  }

}
