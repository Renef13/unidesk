package br.ufma.glp.unidesk.backend.domain.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;

@Service
public class StorageService {
    private MinioClient minioClient;


    public StorageService( MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadFile(MultipartFile file) throws Exception{
        var inputStream = file.getInputStream();
        var objectId = UUID.randomUUID().toString();
        System.out.println(inputStream);
        if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket("files").build())){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("files").build());
        }
        
        minioClient.putObject(
            PutObjectArgs.builder()
            .bucket("files")
            .object(objectId)
            .stream(inputStream, inputStream.available(), -1)
            .contentType(file.getContentType())
            .build()
        );
        return objectId;
    }


    public String getUrl(String objectId) throws InvalidKeyException, io.minio.errors.ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, io.minio.errors.ServerException, IllegalArgumentException, IOException  {

        String url = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
            .method(Method.GET)
            .bucket("files")
            .object(objectId)
            .expiry(60 * 10)
            .build());
        return url;
    }
}
