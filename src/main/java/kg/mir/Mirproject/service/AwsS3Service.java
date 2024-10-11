package kg.mir.Mirproject.service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface AwsS3Service {
    Map<String, String> uploadFile(MultipartFile file) throws IOException;

    Map<String, String> deleteObject(String s, String fileName);
}
