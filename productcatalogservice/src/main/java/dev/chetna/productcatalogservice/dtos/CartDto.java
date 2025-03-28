package dev.chetna.productcatalogservice.dtos;

import dev.chetna.productcatalogservice.models.BaseModel;
import dev.chetna.productcatalogservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto extends BaseModel {
    private int id;
    private int userId;
    private Product[] products;
}
