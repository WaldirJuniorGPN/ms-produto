package br.com.grupo27.tech.challenge.produto.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.util.TypeInformation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;

import static br.com.grupo27.tech.challenge.produto.utils.ConstantesUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ControllerExceptionHandlerTest {

    @InjectMocks
    private ControllerExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testEntityNotFound() {
        var exception = new ControllerNotFoundException("Entity not found");

        var response = exceptionHandler.entityNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ENTITY_NOT_FOUND, Objects.requireNonNull(response.getBody()).getError());
        assertEquals("Entity not found", response.getBody().getMessage());
        verify(request, times(1)).getRequestURI();
    }

    @Test
    void testPropertyReferenceException() {

        var typeInformation = TypeInformation.LIST;
        List<PropertyPath> alreadyResolvedPaths = List.of();

        var exception = new PropertyReferenceException("Invalid property reference", typeInformation, alreadyResolvedPaths);
        var response = exceptionHandler.propertyReferenceException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(PROPERTY_REFERENCE_INVALID, Objects.requireNonNull(response.getBody()).getError());
        verify(request, times(1)).getRequestURI();
    }

    @Test
    void testValidation() {
        var fieldError1 = new FieldError("objectName", "field1", "Field1 error message");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(methodArgumentNotValidException.getMessage()).thenReturn("Validation failed");

        var response = exceptionHandler.validation(methodArgumentNotValidException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        VaidateError validateError = (VaidateError) response.getBody();
        assert validateError != null;
        assertEquals(ERRO_VALIDACAO, validateError.getError());
        assertEquals("Validation failed", validateError.getMessage());

        verify(request, times(1)).getRequestURI();
        verify(methodArgumentNotValidException, times(1)).getBindingResult();
        verify(bindingResult, times(1)).getFieldErrors();
    }
}