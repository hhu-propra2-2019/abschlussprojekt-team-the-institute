package mops.portfolios;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
public class GroupController {
  transient Group group;

  public GroupController(Group group) {
    this.group = group;
  }

  List<MatrikelNr> getGroupmembers(MatrikelNr matrikelnr) {
    /* HttpClient httpClient = new HttpClient();
    * String responseBody = httpClient.getBody("/gruppen2/groupmembers"); */
    // TODO genaues URI und Content-Type mit gruppen2 absprechen

    List<MatrikelNr> matrikelNrList = new ArrayList<>();
    return matrikelNrList;
  }

}
