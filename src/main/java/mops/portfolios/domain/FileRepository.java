package mops.portfolios.domain;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Value;

public class FileRepository {

    @Value("${miniIO.url}")
    private transient String url;
    @Value("${miniIO.accessKey}")
    private transient String accessKey;
    @Value("${miniIO.secretKey}")
    private transient String secretKey;

    MinioClient minioClient = new MinioClient(url, accessKey, secretKey);

    public FileRepository() throws InvalidPortException, InvalidEndpointException {
    }
}
