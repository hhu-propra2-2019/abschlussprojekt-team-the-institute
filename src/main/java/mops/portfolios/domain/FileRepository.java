package mops.portfolios.domain;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

public class FileRepository {

    MinioClient minioClient = new MinioClient("http://127.0.0.1:9000/", "minio", "minio123");

    public FileRepository() throws InvalidPortException, InvalidEndpointException {
    }
}
