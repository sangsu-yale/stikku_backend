package nameco.stikku.annotation.games;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import nameco.stikku.annotation.AuthHeaderParameter;
import nameco.stikku.annotation.UuidParameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "티켓 즐겨찾기 업데이트", description = "uuid를 통해 Game Result를 조회하고, 해당 Game Result의 즐겨찾기를 업데이트합니다.")
@ApiResponse(responseCode = "204", description = "정상적으로 수정이 완료된 경우")
@AuthHeaderParameter @UuidParameter
public @interface UpdateGameFavoriteOperation {
}
