package com.example.order.utils.exception;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomExceptionHandlerTest {
    // Handle single validation error and return correct ApiErrorMessage
    @Test
    public void test_handle_single_validation_error() {
        CustomExceptionHandler handler = new CustomExceptionHandler();

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "validation error");
        List<FieldError> fieldErrors = Collections.singletonList(fieldError);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        HttpHeaders headers = new HttpHeaders();
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(ex, headers, status, request);

        ApiErrorMessage errorMessage = (ApiErrorMessage) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, errorMessage.getStatus());
        assertEquals(1, errorMessage.getErrors().size());
        assertEquals("validation error", errorMessage.getErrors().get(0));
    }

    // Handle empty binding result with no errors
    @Test
    public void test_handle_empty_binding_result() {
        CustomExceptionHandler handler = new CustomExceptionHandler();

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = Collections.emptyList();

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        HttpHeaders headers = new HttpHeaders();
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(ex, headers, status, request);

        ApiErrorMessage errorMessage = (ApiErrorMessage) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, errorMessage.getStatus());
        assertTrue(errorMessage.getErrors().isEmpty());
    }
}