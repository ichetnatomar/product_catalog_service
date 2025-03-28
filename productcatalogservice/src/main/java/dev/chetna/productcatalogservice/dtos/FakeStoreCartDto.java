package dev.chetna.productcatalogservice.dtos;

import dev.chetna.productcatalogservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FakeStoreCartDto {
    private int id;
    private int userId;
    private Product[] productsArray;
}
