package bsb.group5.employee.controllers.handlers;

import bsb.group5.employee.config.MessageConfig;
import bsb.group5.employee.service.exceptions.EmployeeGetException;
import bsb.group5.employee.service.exceptions.EmployeeSearchException;
import bsb.group5.employee.util.ResponseBodyUtil;
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
        responseBody.put(messageConfig.getMessageFieldTimestamp(), LocalDateTime.now());
        responseBody.put(messageConfig.getMessageFieldStatus(), HttpStatus.BAD_REQUEST);

        Set<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());
        responseBody.put(messageConfig.getMessageFieldErrors(), errors);
        return new ResponseEntity<>(responseBody, headers, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex) {

        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(
                messageConfig.getMessageSomethingWentWrong(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> responseBody = responseBodyUtil.getResponseBody(messageConfig.getMessageCannotProcess(), status);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(responseBody, headers, status);
    }

    @ExceptionHandler(value = {EmployeeSearchException.class})
    protected ResponseEntity<Object> handleViewEmployeeConflict(EmployeeSearchException ex) {
        String responseBody = ex.getMessage();
        return new ResponseEntity<>(responseBody, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {EmployeeGetException.class})
    protected ResponseEntity<Object> handleGetEmployeeConflict(EmployeeGetException ex) {
        String responseBody = ex.getMessage();
        return new ResponseEntity<>(responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}