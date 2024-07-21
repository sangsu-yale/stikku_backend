package nameco.stikku.advice;

import nameco.stikku.dto.ErrorResponse;
import nameco.stikku.user.exception.InvalidUserDataException;
import nameco.stikku.user.exception.UserNotFoundExeption;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GameResultNotFoundException.class)
    public ErrorResponse gameResultNotFoundHandler(GameResultNotFoundException e) {
        return new ErrorResponse(404, "Not Found - " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GameReviewNotFoundException.class)
    public ErrorResponse gameReviewNotFoundHandler(GameReviewNotFoundException e) {
        return new ErrorResponse(404, "Not Found - " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingFieldException.class)
    public ErrorResponse missingFieldHandler(MissingFieldException e) {
        return new ErrorResponse(404, "Not Found - " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllExceptions(Exception ex) {
        return new ErrorResponse(500, "Internal Server Error - " + ex.getMessage());
    }


}
