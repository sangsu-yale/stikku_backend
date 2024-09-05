package nameco.stikku.annotation.games;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import nameco.stikku.annotation.AuthHeaderParameter;
import nameco.stikku.game.dto.GameResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "새 게임 티켓 생성", description = "새로운 게임 티켓(Game Result와 Game Review)을 생성합니다.")
@ApiResponse(responseCode = "201", description = "생성된 새로운 Game Result와 Game Review", content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = GameResponseDto.class)
))
@AuthHeaderParameter
@Parameter(name = "GameRequestDto", description = "요청 바디 - GameRequestDto Scheme 참고")
public @interface CreateGameOperation {
}
