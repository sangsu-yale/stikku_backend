package nameco.stikku.annotation.image;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "프로필 이미지 업로드", description = "사용자(User) 프로필 이미지를 업로드 합니다.")
@ImageUploadParamAndRes
public @interface UploadProfileImageOperation {
}
