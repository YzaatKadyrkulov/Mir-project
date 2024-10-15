package kg.mir.Mirproject.service.impl;

import kg.mir.Mirproject.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3ServiceImpl implements AwsS3Service {

    private final S3Client s3Client;

    @Value("${s3.bucketName}")
    private String bucketName;

    @Value("${s3.bucketUrl}")
    private String bucketPath;

    @Override
    public Map<String, String> uploadFile(MultipartFile file) throws IOException{
        String key = System.currentTimeMillis() + file.getOriginalFilename();
        PutObjectRequest putObjectAclRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(file.getContentType())
                .key(key).build();
        s3Client.putObject(putObjectAclRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        log.info("Файл:%s успешно загружен на сервер.".formatted(key));
        return Map.of("link",bucketPath+key);
    }

    @Override
    public Map<String, String> deleteObject(String s, String fileName) {
        try {
            DeleteObjectRequest delete = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.deleteObject(delete);
            log.info("Object deleted : " + fileName);
            return Map.of(
                    "Сообщение", "Объект успешно удален"
            );
        } catch (S3Exception e) {
            log.error("Error deleting object: " + e.getMessage());
            return Map.of(
                    "Ошибка",
                    "Не удалось удалить объект"
            );
        }
    }
}
