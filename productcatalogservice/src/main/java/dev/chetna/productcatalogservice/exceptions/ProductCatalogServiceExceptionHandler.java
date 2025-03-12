package dev.chetna.productcatalogservice.exceptions;

import dev.chetna.productcatalogservice.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductCatalogServiceExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(Exception exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(exception.getMessage());
        ResponseEntity<ErrorResponseDto> errorResponseEntity = new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
        return  errorResponseEntity;
    }
}
