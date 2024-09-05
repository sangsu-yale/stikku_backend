package nameco.stikku.responseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "에러 응답 DTO")
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "예시) 404")
    private int code;
    @Schema(description = "에러 메시지", example = "예시) Not Found - 유저를 찾을 수 없습니다.")
    private String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

