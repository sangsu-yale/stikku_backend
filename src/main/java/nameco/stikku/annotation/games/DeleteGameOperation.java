package nameco.stikku.annotation.games;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import nameco.stikku.annotation.AuthHeaderParameter;
import nameco.stikku.annotation.UuidParameter;
import nameco.stikku.responseDto.MessageResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "티켓 삭제", description = "Game Result와 Game Review를 삭제합니다.")
@ApiResponse(responseCode = "204", description = "정상적으로 수정이 완료된 경우", content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = MessageResponse.class),
        examples = @ExampleObject(
                value = """
                        {
                            "message": "Game 1004 deleted successfully."
                        }
                        """
        )
))
@AuthHeaderParameter
@UuidParameter
public @interface DeleteGameOperation {
}
