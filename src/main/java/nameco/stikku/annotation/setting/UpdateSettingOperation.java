package nameco.stikku.annotation.setting;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nameco.stikku.annotation.AuthHeaderParameter;
import nameco.stikku.annotation.UserIdParameter;
import nameco.stikku.setting.Setting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "사용자 설정 정보", description = "사용자 설정 정보를 조회합니다.")
@UserIdParameter
@AuthHeaderParameter
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정된 설정 정보", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Setting.class)
        ))
})
public @interface UpdateSettingOperation {
}
