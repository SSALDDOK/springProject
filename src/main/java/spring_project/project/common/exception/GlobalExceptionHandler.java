package spring_project.project.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //ResponseEntityExceptionHandler안에 이미 선언되있어서 오버라이딩해서 만들어야 함
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("ex:{}", ex.getMessage());
        return handleExceptionInternal(ex, ResponseEntity.status(status)
                .body(ex.getBindingResult()
                        .getAllErrors()
                        .get(0)
                        .getDefaultMessage()), headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected String handleCustomException(ConstraintViolationException e) {
        log.error("handleCustomException throw ConstraintViolationException : {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(ValidationException.class)
    protected String handleCustomException(ValidationException e) {
        log.error("handleCustomException throw ValidationException : {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<Object> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(e.getErrorCode().getDetail());
    }
}
