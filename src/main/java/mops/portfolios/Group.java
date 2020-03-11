package mops.portfolios;

/*
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;



@Service
public class Group {

  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

  Group get(int id) {
    return this;
  } //TODO

  /*
   * Use this method to get the updates from Gruppenbildung regarding groups.
   * @param matrikelnr The MatrikelNr of the student to get the group members of
   * @return List of the MatrikelNr of each group member
   */
  /* List<MatrikelNr> getGroupmembers(MatrikelNr matrikelnr) {
     HttpClient httpClient = new HttpClient();
    // TODO genaues URI mit gruppen2 absprechen
    String responseBody = httpClient.get("/gruppen2/groupmembers");

    if (responseBody == null) {
      // TODO: use data from database further
    } else {
      JSONObject jsonObject;
      try {
        jsonObject = new JSONObject(responseBody);
      } catch (JSONException err) {
        logger.error("The service Gruppenbildung is temporarily not reachable ", err);
        // only while in development
        throw new RuntimeException("Error while trying to get JSON object");
      }
    } // TODO once we get data from gruppen2

    List<MatrikelNr> matrikelNrList = new ArrayList<>();
    return matrikelNrList;
  } */

}
