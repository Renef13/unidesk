package br.ufma.glp.unidesk.backend.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import io.minio.MinioClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {
    @Value("${minio.root.user}")
    private String rootUser;
    @Value("${minio.root.password}")
    private String passwordUser;
    @Value("${java.minio.url}")
    private String urlMinio;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://" + urlMinio +":9000")
                .credentials(rootUser, passwordUser)
                .build();
    }
}
