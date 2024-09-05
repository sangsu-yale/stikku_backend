package nameco.stikku.annotation.games;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import nameco.stikku.annotation.UuidParameter;
import nameco.stikku.game.dto.GameResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "티켓 조회", description = "uuid를 통해 Game Result와 Game Review를 조회합니다.")
@ApiResponse(responseCode = "200", description = "uuid를 통해 조회한 Game Result와 Game Review", content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = GameResponseDto.class)))
@UuidParameter
public @interface GetGameByIdOperation {
}
