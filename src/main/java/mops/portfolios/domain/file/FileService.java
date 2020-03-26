package mops.portfolios.domain.file;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import mops.portfolios.PortfoliosApplication;
import mops.portfolios.domain.file.FileRepository;
import mops.portfolios.domain.entry.EntryField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class FileService {
  private static final transient Logger logger =
          LoggerFactory.getLogger(PortfoliosApplication.class);

  @Autowired @NonNull
  private transient FileRepository fileRepository;

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


  public void updateField(MultipartFile file, EntryField field) {
    System.out.println(field.getContent());
    System.out.println(file.getName());
    fileRepository.saveFile(file, field);
  }
}
