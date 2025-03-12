package dev.chetna.productcatalogservice.dtos;

import dev.chetna.productcatalogservice.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDto {
    private int cartId;
    private int userId;
    private List<Product> productList;
}
