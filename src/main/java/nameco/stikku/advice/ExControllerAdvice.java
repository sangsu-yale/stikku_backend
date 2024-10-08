package nameco.stikku.advice;

import nameco.stikku.advice.exception.*;
import nameco.stikku.responseDto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse userNotFoundHandler(UserNotFoundException e) {
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

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(AccesDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccesDeniedException e) {
        return new ErrorResponse(406, "Access Denied - " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllExceptions(Exception ex) {
        return new ErrorResponse(500, "Internal Server Error - " + ex.getMessage());
    }

}
