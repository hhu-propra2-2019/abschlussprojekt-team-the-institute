package mops.portfolios.tools;

import java.util.HashMap;
import org.asciidoctor.Asciidoctor;
import org.springframework.stereotype.Service;

@Service
public class AsciiDocConverter {

  private transient Asciidoctor asciiDoctor = Asciidoctor.Factory.create();

  /**
   * Convert AsciiDoc to HTML.
   * @param asciiDocText The AsciiDoc text
   * @return The html structure
   */

  @SuppressWarnings("PMD")
  public String convertToHtml(String asciiDocText) {
    return asciiDoctor.convert(asciiDocText, new HashMap<>());
  }
}
