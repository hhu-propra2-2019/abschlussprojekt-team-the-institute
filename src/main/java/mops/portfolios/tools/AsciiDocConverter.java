package mops.portfolios.tools;

import org.asciidoctor.Asciidoctor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AsciiDocConverter {

  private Asciidoctor asciiDoctor = Asciidoctor.Factory.create();

  /**
   * Convert AsciiDoc to HTML
   * @param asciiDocText The AsciiDoc text
   * @return The html structure
   */

  @SuppressWarnings("PMD")
  public String convertToHTML(String asciiDocText) {
    return asciiDoctor.convert(asciiDocText, new HashMap<>());
  }
}
