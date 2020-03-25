package mops.portfolios.controller.services;

import java.io.IOException;
import mops.portfolios.PortfoliosApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
  private static final transient Logger logger =
          LoggerFactory.getLogger(PortfoliosApplication.class);

  /**
   * Checks whether anything is uploaded (whether file has any content).
   * @param file The file to read
   * @return <strong>true</strong> if no content or an error while parsing, \
   * <strong>false</strong> if all's well
   */
  public boolean nothingUploaded(MultipartFile file) {
    byte[] fileBytes = readFile(file);
    if (fileBytes == null) {
      return true;
    }

    return false;
  }

  /**
   * Reads and returns file as byte[].
   * @param file the file to read
   * @return <strong>null</strong> if any IOException while reading, the bytes of the file otherwise
   */
  @SuppressWarnings("PMD")
  public byte[] readFile(MultipartFile file) {
    byte[] fileBytes;

    try {
      fileBytes = file.getBytes();
    } catch (IOException io) {
      logger.warn("Could not read file", io);
      return null;
    }

    return fileBytes;
  }


}
