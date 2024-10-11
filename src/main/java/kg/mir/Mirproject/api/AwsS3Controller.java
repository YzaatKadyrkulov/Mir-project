//package kg.mir.Mirproject.api;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.annotation.security.PermitAll;
//import kg.mir.Mirproject.service.AwsS3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/awsS3")
//@RequiredArgsConstructor
//@Tag(name = "AwsS3-api")
//@CrossOrigin(origins = "*", maxAge = 3600)
//@PermitAll
//public class AwsS3Controller {
//
//    private final AwsS3Service s3Service;
//
//    @Operation(summary = "Загрузить файл на s3", description = "Пользователь может загрузить файл")
//    @PostMapping(path = "/upload",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        return s3Service.uploadFile(file);
//    }
//
//    @Operation(summary = "Удалить файл из s3", description = "Пользователь может удалить файл")
//    @DeleteMapping(path = "/delete")
//    @ResponseStatus(HttpStatus.OK)
//    Map<String, String> deleteObject(@RequestParam("fileName") String fileName) throws IOException {
//        return s3Service.deleteObject("my-bucket", fileName);
//    }
//
//}
