package nameco.stikku.annotation.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nameco.stikku.annotation.AuthHeaderParameter;
import nameco.stikku.annotation.UserIdParameter;
import nameco.stikku.game.dto.GameResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "게임 동기화", description = "클라이언트 로컬에 저장된 게임 티켓 정보와 서버에 저장된 게임 티켓 정보를 동기화합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "동기화된 사용자의 모든 게임 티켓", content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = GameResponseDto.class))
        ))
})
@UserIdParameter @AuthHeaderParameter
public @interface SyncGameOperation {
}
