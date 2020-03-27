package mops.portfolios.controller.services;

import mops.portfolios.domain.file.FileService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileServiceTest {

  private transient MultipartFile multipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
  private transient FileService fileService;



  @Test
  public void nothingUploadedTest() {
    boolean nothingUploaded = fileService.nothingUploaded(multipartFile);

    Assert.assertEquals(false, nothingUploaded);

  }

  @Test
  public void readFileTest() throws Exception {
    byte[] filesize = fileService.readFile(multipartFile);

    Assert.assertEquals(multipartFile.getBytes(), filesize);

  }


}
