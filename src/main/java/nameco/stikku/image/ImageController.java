package nameco.stikku.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/images")
@Tag(name = "Image API", description = "이미지 처리와 관련된 API")
public class ImageController {

    private final AmazonS3Client amazonS3Client;

    public ImageController(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping("/ticket")
    public ResponseEntity<?> uploadTicketImage(@RequestParam("image") MultipartFile image) {

        try {
            String originalFilename = image.getOriginalFilename();
            String imageName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueImageName = "ticket/" + imageName + "-" + UUID.randomUUID().toString() + extension;
            String imageUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + uniqueImageName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            amazonS3Client.putObject(bucket, uniqueImageName, image.getInputStream(), metadata);

            HashMap<String, String> responseBody = new HashMap<>();
            responseBody.put("imageUrl", imageUrl);
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/profile")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile image) {

        try {
            String originalFilename = image.getOriginalFilename();
            String imageName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueImageName = "profile/" + imageName + "-" + UUID.randomUUID().toString() + extension;
            String imageUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + uniqueImageName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            amazonS3Client.putObject(bucket, uniqueImageName, image.getInputStream(), metadata);

            HashMap<String, String> responseBody = new HashMap<>();
            responseBody.put("imageUrl", imageUrl);
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
