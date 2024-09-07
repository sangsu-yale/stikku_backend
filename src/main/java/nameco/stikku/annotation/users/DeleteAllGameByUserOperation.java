package nameco.stikku.annotation.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nameco.stikku.annotation.UserIdParameter;
import nameco.stikku.responseDto.MessageResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "사용자 게임 티켓 전체 삭제", description = "특정 사용자가 작성한 모든 티켓을 삭제합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자 ID", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MessageResponse.class)
        ))
})
@UserIdParameter
public @interface DeleteAllGameByUserOperation {
}
