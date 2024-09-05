package nameco.stikku.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "API 서버에서 발급받은 액세스 토큰", required = true)
public @interface AuthHeaderParameter {
}
