package mops.portfolios.tools;


import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class AsciiDocConverterTest {

  private transient AsciiDocConverter asciiDocConverter = new AsciiDocConverter();

  @Test
  void convertTest() {

    String asciidoc = "+\n" +
            "[quote,\"Charles Dickens\",\"A Tale of Two Cities\"]\n" +
            "It was the best of times, it was the worst of times, it was the age of wisdom,\n" +
            "it was the age of foolishness...";

    String converted = asciiDocConverter.convertToHtml(asciidoc);
    String expected = "<div class=\"paragraph\">\n" +
            "<p>+</p>\n" +
            "</div>\n" +
            "<div class=\"quoteblock\">\n" +
            "<blockquote>\n" +
            "It was the best of times, it was the worst of times, it was the age of wisdom,\n" +
            "it was the age of foolishness&#8230;&#8203;\n" +
            "</blockquote>\n" +
            "<div class=\"attribution\">\n" +
            "&#8212; Charles Dickens<br>\n" +
            "<cite>A Tale of Two Cities</cite>\n" +
            "</div>\n" +
            "</div>";

    Assert.assertEquals(expected, converted);

  }

}
