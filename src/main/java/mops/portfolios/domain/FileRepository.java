package mops.portfolios.domain;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import mops.portfolios.PortfoliosApplication;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryFieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@Repository
public class FileRepository {

    @Value("${minio.url}")
    private transient String url;

    @Value("${minio.accessKey}")
    private transient String accessKey;

    @Value("${minio.secretKey}")
    private transient String secretKey;

    private transient String bucketName = "portfolios";

    private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

    private MinioClient minioClient;

    @Autowired
    private EntryFieldRepository entryFieldRepository;

    public FileRepository() throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException, InvalidPortException, InvalidEndpointException {
        this.minioClient = new MinioClient("http://127.0.0.1:9000", "minio", "minio123");
        try {
            boolean isExist = minioClient.bucketExists(bucketName);
            if (!isExist) {
                minioClient.makeBucket(bucketName);
            }
        } catch (MinioException e) {
            logger.warn(e.toString());
        }
    }

    public void saveFile(MultipartFile file, EntryField entryField) {
        String objName = UUID.randomUUID().toString();
        try {
            minioClient.putObject(bucketName, objName + "." + file.getName(), file.getName());
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            logger.info(e.toString());
        }
        entryField.setAttachment(objName);
        entryFieldRepository.save(entryField);
    }

    public String getFileUrl(String objName) {
        try {
            return minioClient.getObjectUrl(bucketName, objName);
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            logger.info(e.toString());
        }
        return null;
    }
}
