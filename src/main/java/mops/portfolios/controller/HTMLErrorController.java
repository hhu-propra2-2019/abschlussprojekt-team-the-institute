package mops.portfolios.controller;

import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HTMLErrorController implements ErrorController {

  /**
   * Error mapping for GET requests.
   *
   * @param model    The spring model to add the attributes to
   * @param response The http response
   * @return The page to load
   */

  @GetMapping("/error")
  public String handleError(Model model, HttpServletResponse response) {

    model.addAttribute("errorCode", response.getStatus());
    model.addAttribute("errorMessage", "Some error message");

    //TODO: ADD LOG ENTRY

    return "error";
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}