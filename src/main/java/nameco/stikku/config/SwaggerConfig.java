package nameco.stikku.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Schema;

import nameco.stikku.responseDto.ErrorResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        // SecurityRequirement와 SecurityScheme 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components()
                .addSecuritySchemes(jwt, new SecurityScheme()
                        .name("jwt")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("BEARER"));

        // OpenAPI 객체에 components, securityRequirement, apiInfo 설정
        return new OpenAPI()
                .components(components.addSchemas("ErrorResponse", new Schema<ErrorResponse>()
                        .description("오류 응답 객체")
                        .addProperty("code", new Schema<>().type("integer").format("int32"))
                        .addProperty("message", new Schema<>().type("string"))
                ))
                .info(apiInfo())
                .addSecurityItem(securityRequirement);
    }

    @Bean
    public OpenApiCustomizer gameApiResponsesCustomizer() {
        return openApi -> openApi.getPaths().entrySet().stream()
                // /games 경로에만 적용
                .filter(path -> path.getKey().startsWith("/games") || path.getKey().startsWith("/settings") || path.getKey().startsWith("/users"))
                .forEach(path -> path.getValue().readOperations().forEach(operation -> {
                    // 400 Bad Request 예외 응답 추가
                    ApiResponse badRequestResponse = new ApiResponse()
                            .description("Bad Request - 요청 바디 값이 올바르지 않은 경우")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                                            .example(new Example().value(new ErrorResponse(400, "Bad Request - Invalid User Email")))
                            ));

                    // 404 Not Found 예외 응답 추가
                    ApiResponse gameNotFoundResponse = new ApiResponse()
                            .description("Not Found - 해당하는 게임 티켓/사용자 정보를 찾을 수 없는 경우")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                                            .example(new Example().value(new ErrorResponse(404, "Not Found - Game Not Found")))
                            ));

                    // 406 Not Acceptable 예외 응답 추가
                    ApiResponse accessDeniedResponse = new ApiResponse()
                            .description("Access Denied - (Authorization 헤더 사용 시) 액세스 토큰이 유효하지 않은 경우")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                                            .example(new Example().value(new ErrorResponse(406, "Access Denied - 유효하지 않은 접근입니다.")))
                            ));

                    // 500 Internal Server Error 예외 응답 추가
                    ApiResponse internalServerErrorResponse = new ApiResponse()
                            .description("Internal Server Error - 서버 오류")
                            .content(new Content().addMediaType("application/json",
                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                                            .example(new Example().value(new ErrorResponse(500, "Internal Server Error - 서버 에러")))
                            ));

                    // 공통 예외 응답을 전역에 추가
                    operation.getResponses().addApiResponse("400", badRequestResponse);
                    operation.getResponses().addApiResponse("404", gameNotFoundResponse);
                    operation.getResponses().addApiResponse("406", accessDeniedResponse);
                    operation.getResponses().addApiResponse("500", internalServerErrorResponse);
                })
        );
    }

    private Info apiInfo() {
        return new Info()
                .title("Stida API Docs")
                .description("스티다 API 서버 명세")
                .version("1.0.0");
    }
}
