package nameco.stikku.annotation.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import nameco.stikku.annotation.UserIdParameter;
import nameco.stikku.user.User;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자의 정보", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
        ))
})
@UserIdParameter
public @interface GetUserByIdOperation {
}
