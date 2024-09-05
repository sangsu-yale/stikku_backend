package nameco.stikku.annotation.games;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import nameco.stikku.annotation.AuthHeaderParameter;
import nameco.stikku.annotation.UuidParameter;
import nameco.stikku.game.dto.GameResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "티켓 업데이트", description = "uuid를 통해 Game Result와 Game Review를 조회하고, 수정합니다.")
@ApiResponse(responseCode = "200", description = "수정된 Game Result와 Game Review", content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = GameResponseDto.class)
))
@AuthHeaderParameter
@UuidParameter
@Parameter(name = "GameRequestDto", description = "요청 바디 - GameRequestDto Scheme 참고")
public @interface UpdateGameOperation {
}
