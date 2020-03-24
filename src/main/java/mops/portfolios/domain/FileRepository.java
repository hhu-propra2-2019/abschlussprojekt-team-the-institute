package mops.portfolios.domain;

import io.minio.MinioClient;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import mops.portfolios.PortfoliosApplication;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryFieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class FileRepository {

    @Value("${minio.url}")
    private transient String url;
    @Value("${minio.accessKey}")
    private transient String accessKey;
    @Value("${minio.secretKey}")
    private transient String secretKey;

    private static final String bucketName = "portfolios.attachments";
    private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private EntryFieldRepository entryFieldRepository;

    public FileRepository() throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {
        try {
            minioClient = new MinioClient(url, accessKey, secretKey);
            boolean isExist = minioClient.bucketExists(bucketName);
            if (!isExist) {
                minioClient.makeBucket(bucketName);
            }
        } catch (MinioException e) {
            logger.warn(e.toString());
        }
    }

    public void saveFile(MultipartFile file, EntryField entryField) {
        try {
            minioClient.putObject(bucketName, UUID.randomUUID().toString(), file.getName());
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            logger.info(e.toString());
        }
    }

    public String getFileUrl(String objName) {
        String url = "";
        try {
            url = minioClient.getObjectUrl(bucketName, objName);
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException | XmlPullParserException e) {
            logger.info(e.toString());
        }
        return url;
    }
}
