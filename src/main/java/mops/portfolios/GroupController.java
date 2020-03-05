package mops.portfolios;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class GroupController {
  transient Group group;
  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

  public GroupController(Group group) {
    this.group = group;
  }

  List<MatrikelNr> getGroupmembers(MatrikelNr matrikelnr) {
    HttpClient httpClient = new HttpClient();
    String responseBody = httpClient.getBody("/gruppen2/groupmembers");

    // Content-Type von gruppen2 ist JSON
    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(responseBody);
    } catch (JSONException err) {
      logger.error("Error: " + err.toString());
      //TODO ERROR!!!!!!!!!!!
    }

    // TODO genaues URI mit gruppen2 absprechen

    List<MatrikelNr> matrikelNrList = new ArrayList<>();
    return matrikelNrList;
  }

}
