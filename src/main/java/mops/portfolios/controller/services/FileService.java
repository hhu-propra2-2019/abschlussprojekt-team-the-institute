package mops.portfolios.controller.services;

import mops.portfolios.PortfoliosApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {
  private static transient final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

  // TODO Extract into Service class
  public boolean nothingUploaded(MultipartFile file) {
    byte[] fileBytes = readFile(file);
    if(fileBytes == null) {
      return true;
    }

    return false;
  }

  // TODO: Extract into a Service
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
