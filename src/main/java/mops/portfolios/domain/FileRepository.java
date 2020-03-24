package mops.portfolios.domain;

import com.jlefebure.spring.boot.minio.MinioService;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@Component
public class FileRepository {

    @Value("${spring.minio.bucket}")
    private transient String bucketName;
    private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);

    @Autowired
    private transient MinioClient minioClient;

    @Autowired
    private transient EntryFieldRepository entryFieldRepository;

    public FileRepository(){}

    public void saveFile(MultipartFile file, EntryField entryField) {
        String objName = UUID.randomUUID().toString();
        try {
            minioClient.putObject(bucketName, objName, file.getName());
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
