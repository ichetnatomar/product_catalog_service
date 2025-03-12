package dev.chetna.productcatalogservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ProductCatalogServiceException {
    private final String message;
    private final String cause;
    private final HttpStatus httpStatus;

    public ProductCatalogServiceException(String message, String cause, HttpStatus httpStatus) {
        this.message = message;
        this.cause = cause;
        this.httpStatus = httpStatus;
    }
}
