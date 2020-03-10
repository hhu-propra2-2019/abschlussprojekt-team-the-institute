package mops.portfolios;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class Group {

  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

  Group get(int id) {
    return this;
  } //TODO

  /**
   * Use this method to retrieve users in a Group.
   * @param userId The user ID of the student to get the group members of
   * @return List of the Account of each group member
   */
  List<String> getGroupMembers(String userId) {

    List<String> accountList = new ArrayList<>();
    return accountList;
  }

  private List<String> getFromLocalDatabase(String userId) {
    // TODO List<Group> groups = findGroupsByUserId(userId);
    return null; // FIXME this is a filler
  }


}
