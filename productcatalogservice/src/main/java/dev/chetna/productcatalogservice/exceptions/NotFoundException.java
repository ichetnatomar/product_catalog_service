package dev.chetna.productcatalogservice.exceptions;
import java.lang.Exception;

public class NotFoundException extends Exception{
    public NotFoundException(String message) {
        super(message);
    }
}
