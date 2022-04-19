package bsb.group5.converter.controllers.handlers;

import bsb.group5.converter.config.MessageConfig;
import bsb.group5.converter.controllers.exceptions.PaginationException;
import bsb.group5.converter.service.exceptions.UpdateStatusServiceException;
import bsb.group5.converter.util.ResponseBodyUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private final ResponseBodyUtil responseBodyUtil;
    private final MessageConfig messageConfig;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.CONFLICT);
        Set<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());
        responseBody.put("errors", errors);

        return new ResponseEntity<>(responseBody, headers, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(ex.getMessage(), status);
        return new ResponseEntity<>(responseBody, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(ex.getMessage(), status);
        return new ResponseEntity<>(responseBody, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(messageConfig.getMessageCannotProcess(), status);
        log.error(ex.getMessage());
        return new ResponseEntity<>(responseBody, headers, status);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex) {
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(
                messageConfig.getMessageSomethingWentWrong(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleSearchValidation(ConstraintViolationException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST);
        Set<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        responseBody.put("errors", errors);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PaginationException.class})
    protected ResponseEntity<Object> handlePaginationException(PaginationException ex) {
        String message = ex.getMessage();
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(
                message, HttpStatus.BAD_REQUEST);
        log.error(message);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UpdateStatusServiceException.class})
    protected ResponseEntity<Object> handlePaginationException(UpdateStatusServiceException ex) {
        String message = ex.getMessage();
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(
                message, HttpStatus.NOT_ACCEPTABLE);
        log.error(message);
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_ACCEPTABLE);
    }
}