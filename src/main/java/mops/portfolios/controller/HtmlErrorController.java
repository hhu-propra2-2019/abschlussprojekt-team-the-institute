package mops.portfolios.controller;

import javax.servlet.http.HttpServletResponse;
import mops.portfolios.PortfoliosApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlErrorController implements ErrorController {
  private static final transient Logger logger =
          LoggerFactory.getLogger(PortfoliosApplication.class);

  /**
   * Error mapping for GET requests.
   *
   * @param model    The spring model to add the attributes to
   * @param response The http response
   * @return The page to load
   */

  @GetMapping("/error")
  public String handleError(Model model, HttpServletResponse response) {

    HttpStatus status = HttpStatus.valueOf(response.getStatus());

    model.addAttribute("errorCode", status.value());
    model.addAttribute("errorMessage", status.getReasonPhrase());

    logger.warn("an error occured. Status Code: " + status.value());

    return "common/error";
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}