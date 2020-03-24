package mops.portfolios;

import java.util.ArrayList;
import java.util.List;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.state.StateService;
import mops.portfolios.domain.user.User;
import mops.portfolios.domain.user.UserRepository;
import mops.portfolios.tools.FakeHttpClient;
import mops.portfolios.tools.IHttpClient;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DatabaseUpdaterTest {

  private transient DatabaseUpdater databaseUpdater;

  /** The url to retrieve the data from */
  private transient String url = "/gruppen2/groupmembers";
  private transient static final Logger logger = (Logger) LoggerFactory.getLogger(PortfoliosApplication.class);


  transient GroupRepository groupRepository = mock(GroupRepository.class);

  transient UserRepository userRepository = mock(UserRepository.class);

  transient StateService stateService = mock(StateService.class);

  @BeforeEach
  public void init() {

    databaseUpdater = new DatabaseUpdater(groupRepository, userRepository, stateService);

  }

  @Test
  public void testClientError() {
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    assertThrows(RuntimeException.class, () -> {
      listAppender.start();
      logger.addAppender(listAppender);
      IHttpClient httpClient = new FakeHttpClient();
      databaseUpdater.getGroupUpdatesFromUrl(httpClient, "400");
      listAppender.stop();

      List<ILoggingEvent> logsList = listAppender.list;
      int logSize = logsList.size();

      assertEquals("The service Gruppenbildung is not reachable: 400 BAD_REQUEST",
              logsList.get(logSize - 2).getMessage());
      assertEquals("An error occured while parsing the JSON data received by the service Gruppenbildung", logsList.get(logSize - 1).getMessage());
    });
  }

  @Test
  public void testEmptyJson() {
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    listAppender.start();
    logger.addAppender(listAppender);
    databaseUpdater.updateDatabaseEvents("{\"status\":4,\"groupList\":[]}");
    listAppender.stop();

    List<ILoggingEvent> logsList = listAppender.list;
    int logSize = logsList.size();

    assertEquals("Database not modified", logsList.get(logSize - 1).getMessage());
  }

  @Test
  public void objectNotJson() {
    assertThrows(RuntimeException.class, () -> databaseUpdater.updateDatabaseEvents("blabla"));
  }

  @Test
  public void testUpdateDatabaseEventsIllegalArgument() {
    databaseUpdater.url = "/bla/bla";
    assertThrows(RuntimeException.class, () -> databaseUpdater.getUpdatesFromJsonObject());
  }
  @Test
  public void testSuccessfulRequest() {
    IHttpClient httpClient = new FakeHttpClient();
    databaseUpdater.getGroupUpdatesFromUrl(httpClient, this.url); // takes mocked JSON from the FakeHttpClient
  }

  @Test
  public void GroupListNotModified() {
    // as talked with gruppen2, this is how the response will look if not modified
    String response = "{\"status\":4,\"groupList\":[]}";
    JSONObject jsonObject = new JSONObject(response);

    boolean result = databaseUpdater.isNotModified(jsonObject);
    assertEquals(true, result);
  }

  @SuppressWarnings("PMD")
  @Test
  public void GroupListIsModified() {
    String response = "{\n" +
            "  \"status\": 4,\n" +
            "  \"groupList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"title\": null,\n" +
            "      \"description\": null,\n" +
            "      \"members\": [\n" +
            "        {\n" +
            "          \"user_id\": \"studentin\",\n" +
            "          \"givenname\": \"studentin\",\n" +
            "          \"familyname\": \"studentin\",\n" +
            "          \"email\": \"studentin@student.in\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"roles\": {\n" +
            "\"studentin\": \"ADMIN\"" +
            "},\n"+
            "      \"type\": \"LECTURE\",\n" +
            "      \"visibility\": \"PUBLIC\",\n" +
            "      \"parent\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    JSONObject jsonObject = new JSONObject(response);

    boolean result = databaseUpdater.isNotModified(jsonObject);
    assertEquals(false, result);
  }

  @SuppressWarnings("PMD")
  @Test
  public void extractJsonObject() {
    String response = "{\n" +
            "  \"status\": 4,\n" +
            "  \"groupList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"title\": \"Lorem\",\n" +
            "      \"description\": null,\n" +
            "      \"members\": [\n" +
            "        {\n" +
            "          \"user_id\": \"studentin\",\n" +
            "          \"givenname\": \"studentin\",\n" +
            "          \"familyname\": \"studentin\",\n" +
            "          \"email\": \"studentin@student.in\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"roles\": {\n" +
            "\"studentin\": \"ADMIN\"" +
            "},\n"+
            "      \"type\": \"LECTURE\",\n" +
            "      \"visibility\": \"PUBLIC\",\n" +
            "      \"parent\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    databaseUpdater.updateDatabaseEvents(response);
  }

  @SuppressWarnings("PMD")
  @Test
  public void deletedGroupTest() {
    List<User> userList = new ArrayList<>();
    User user = new User();
    user.setName("studentin");
    userList.add(user);

    when(groupRepository.save(any(Group.class))).thenReturn(new Group(2L, "Lorem", userList));

    groupRepository.save(new Group(2L, "Lorem", userList));

    String response = "{\n" +
            "  \"status\": 4,\n" +
            "  \"groupList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"title\": null,\n" +
            "      \"description\": null,\n" +
            "      \"members\": [\n" +
            "        {\n" +
            "          \"user_id\": \"studentin\",\n" +
            "          \"givenname\": \"studentin\",\n" +
            "          \"familyname\": \"studentin\",\n" +
            "          \"email\": \"studentin@student.in\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"roles\": {\n" +
            "\"studentin\": \"ADMIN\"" +
            "},\n"+
            "      \"type\": \"LECTURE\",\n" +
            "      \"visibility\": \"PUBLIC\",\n" +
            "      \"parent\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    databaseUpdater.updateDatabaseEvents(response);

    List<Long> groupIds = new ArrayList<>();
    groupIds.add(2L);

    when(groupRepository.findAllById(groupIds)).thenReturn(new ArrayList<>());
    verify(groupRepository, times(1)).save(any(Group.class));

    Assert.assertEquals(new ArrayList<>(), groupRepository.findAllById(groupIds));

    verify(groupRepository, times(1)).findAllById(groupIds);

  }

  @SuppressWarnings("PMD")
  @Test
  public void updateGroupTest() {

    List<User> userList = new ArrayList<>();
    User user = new User();
    user.setName("studentin");
    userList.add(user);

    when(userRepository.save(any(User.class))).thenReturn(user);

    userRepository.save(user);

    Group group = new Group(2L, "Lorem", userList);
    when(groupRepository.save(any(Group.class))).thenReturn(group);

    groupRepository.save(group);



    String response = "{\n" +
            "  \"status\": 4,\n" +
            "  \"groupList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"title\": null,\n" +
            "      \"description\": null,\n" +
            "      \"members\": [\n" +
            "        {\n" +
            "          \"user_id\": \"student\",\n" +
            "          \"givenname\": \"studentin\",\n" +
            "          \"familyname\": \"studentin\",\n" +
            "          \"email\": \"studentin@student.in\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"roles\": {\n" +
            "\"studentin\": \"ADMIN\"" +
            "},\n"+
            "      \"type\": \"LECTURE\",\n" +
            "      \"visibility\": \"PUBLIC\",\n" +
            "      \"parent\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    databaseUpdater.updateDatabaseEvents(response);

    List<Long> groupIds = new ArrayList<>();
    groupIds.add(2L);
    List<User> users = new ArrayList<>();
    User user1 = new User();
    user1.setName("student");
    users.add(user1);

    Group updatedGroup = new Group(2L, "Lorem", users);
    when(groupRepository.findById(2L).get()).thenReturn(updatedGroup);

    System.out.println(updatedGroup);
    List<User> updatedGroupUsers = updatedGroup.getUsers();

    for (User userUpdatedGroup: updatedGroupUsers) {
      Assert.assertEquals("studentin", userUpdatedGroup.getName());

    }

  }

  @SuppressWarnings("PMD")
  @Test
  public void saveNewGroupTest() {

    List<Long> groupIds = new ArrayList<>();
    groupIds.add(2L);

    List<User> userList = new ArrayList<>();
    User user = new User();
    user.setName("studentin");
    userList.add(user);

    List<Group> groups = new ArrayList<>();
    groups.add(new Group(2L, "Lorem", userList));

    String response = "{\n" +
            "  \"status\": 4,\n" +
            "  \"groupList\": [\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"title\": null,\n" +
            "      \"description\": null,\n" +
            "      \"members\": [\n" +
            "        {\n" +
            "          \"user_id\": \"studentin\",\n" +
            "          \"givenname\": \"studentin\",\n" +
            "          \"familyname\": \"studentin\",\n" +
            "          \"email\": \"studentin@student.in\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"roles\": {\n" +
            "\"studentin\": \"ADMIN\"" +
            "},\n"+
            "      \"type\": \"LECTURE\",\n" +
            "      \"visibility\": \"PUBLIC\",\n" +
            "      \"parent\": null\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    databaseUpdater.updateDatabaseEvents(response);

    when(groupRepository.findAllById(groupIds)).thenReturn(groups);
    List<Group> groupsFromRepository = (List<Group>) groupRepository.findAllById(groupIds);

    Assert.assertEquals(groups, groupsFromRepository);

  }

}
