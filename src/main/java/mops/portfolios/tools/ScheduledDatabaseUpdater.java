package mops.portfolios.tools;

import javax.persistence.PostLoad;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.state.StateService;
import mops.portfolios.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true)
@Component
@EnableScheduling
public class ScheduledDatabaseUpdater {

  /**
   * The URL String to generate the Url Object from. It must be formatted as in following:<br>
   *   <code>[scheme]://[domain]/[path]/</code><br>
   *   The String must start with the scheme (e.g. "http", or "https") and end with a slash.
   */
  private transient String url = "http://localhost:8081/gruppen2/api/updateGroups/";
  // url retrieved from it-bois APIController commit c844f5d (24.03.2020)

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
  @PostLoad
  @Scheduled(fixedRate = 10_000)
  public void updateDatabase() {
    databaseUpdater.execute();
  }

}
