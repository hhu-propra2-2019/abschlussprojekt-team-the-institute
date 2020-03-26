package mops.portfolios.domain.file;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.NoResponseException;
import io.minio.errors.RegionConflictException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import mops.portfolios.PortfoliosApplication;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryFieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

@Repository
public class FileRepository {

  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);
  private static String bucketName = "portfolios";
  private transient MinioClient minioClient;

  @Autowired
  private transient EntryFieldRepository entryFieldRepository;

  /**
   * FileRepository constructor.
   *
   * @param config FileRepositoryConfig
   * @throws InvalidPortException     Exception
   * @throws InvalidEndpointException Exception
   */
  public FileRepository(FileRepositoryConfig config) throws InvalidPortException,
      InvalidEndpointException {
    this.minioClient = new MinioClient(config.getUrl(), config.getAccessKey(),
        config.getSecretKey());
    try {
      boolean isExist = minioClient.bucketExists(bucketName);
      if (!isExist) {
        minioClient.makeBucket(bucketName);
      }
    } catch (NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException
        | InvalidBucketNameException | RegionConflictException | InsufficientDataException
        | NoResponseException | ErrorResponseException | InternalException
        | InvalidResponseException e) {
      logger.warn(e.toString());
    }
  }

  /**
   * Save MultipartFile file to minio keeping reference in entryField.attachment
   *
   * @param file       MultipartFile
   * @param entryField entryField to save the object to
   */
  public void saveFile(MultipartFile file, EntryField entryField) {
    String objName = UUID.randomUUID().toString() + "." + file.getName();
    try {
      minioClient.putObject(bucketName, objName, file.getOriginalFilename());
    } catch (Exception e) {
      logger.info(e.toString());
    }
    entryField.setAttachment(objName);
    entryFieldRepository.save(entryField);
  }

  /**
   * Get full URL of a file.
   * @Notice Bucken policy should be set to prefix: *, READ_ONLY
   *
   * @param objName name of the saved object
   * @return
   */
  public String getFileUrl(String objName) {
    try {
      return minioClient.getObjectUrl(bucketName, objName);
    } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
        | IOException | InvalidKeyException | NoResponseException | XmlPullParserException
        | ErrorResponseException | InternalException | InvalidResponseException e) {
      logger.info(e.toString());
    }
    return "";
  }

}
