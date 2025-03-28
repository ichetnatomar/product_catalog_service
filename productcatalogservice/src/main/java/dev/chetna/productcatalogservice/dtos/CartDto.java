package dev.chetna.productcatalogservice.dtos;

import dev.chetna.productcatalogservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {
    private int cartId;
    private int userId;
    private Product[] products;
}
