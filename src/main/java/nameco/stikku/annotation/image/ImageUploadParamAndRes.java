package nameco.stikku.annotation.image;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "201", description = "S3 버킷에 업로드된 이미지 URL", content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(value = """
                {
                    "imageUrl": "https://s3..."
                }
                """)
))
@Parameter(name = "image", description = "Multipart/form-data 타입의 이미지", required = true, content = @Content(mediaType = "multipart/form-data"))
public @interface ImageUploadParamAndRes {
}
