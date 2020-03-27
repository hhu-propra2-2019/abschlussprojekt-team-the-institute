package mops.portfolios.domain.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mops.portfolios.minio")
@Getter
@Setter
public class FileRepositoryConfig {
  private String url;
  private String accessKey;
  private String secretKey;
}