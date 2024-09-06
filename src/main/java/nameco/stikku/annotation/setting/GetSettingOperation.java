package nameco.stikku.annotation.setting;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nameco.stikku.annotation.UserIdParameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "사용자 설정 정보", description = "사용자 설정 정보를 조회합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자의 설정 정보", content = @Content(
                mediaType = "application/json"
        ))
})
@UserIdParameter
public @interface GetSettingOperation {
}
