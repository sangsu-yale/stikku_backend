package nameco.stikku.annotation.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nameco.stikku.annotation.UserIdParameter;
import nameco.stikku.game.dto.GameResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "사용자 게임 티켓 조회", description = "특정 사용자가 작성한 모든 티켓을 조회합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자가 작성한 모든 게임 티켓 정보(Game Result, Game Review)", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GameResponseDto.class)
        ))
})
@UserIdParameter
public @interface GetAllGameByUserOperation {
}
