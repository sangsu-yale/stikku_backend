package nameco.stikku.advice;

import nameco.stikku.responseDto.ErrorResponse;
import nameco.stikku.advice.exception.InvalidUserDataException;
import nameco.stikku.advice.exception.UserNotFoundExeption;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundExeption.class)
    public ErrorResponse userNotFoundHandler(UserNotFoundExeption e) {
        return new ErrorResponse(404, "Not Found - " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserDataException.class)
    public ErrorResponse invalidUserDataExeption(InvalidUserDataException e) {
        return new ErrorResponse(400, "Bad Request - " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllExceptions(Exception ex) {
        return new ErrorResponse(500, "Internal Server Error - " + ex.getMessage());
    }
}
