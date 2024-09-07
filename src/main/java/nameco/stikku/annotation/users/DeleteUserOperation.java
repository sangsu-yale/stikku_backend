package nameco.stikku.annotation.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nameco.stikku.annotation.AuthHeaderParameter;
import nameco.stikku.annotation.UserIdParameter;
import nameco.stikku.responseDto.MessageResponse;
import nameco.stikku.user.User;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제된 사용자 ID", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MessageResponse.class),
                examples = @ExampleObject(value = """
                        {
                                "message": "User 0 deleted successfully."
                        }
                        """)
        ))
})
@UserIdParameter @AuthHeaderParameter
public @interface DeleteUserOperation {
}
