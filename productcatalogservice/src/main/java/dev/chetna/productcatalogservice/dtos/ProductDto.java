package dev.chetna.productcatalogservice.dtos;

import dev.chetna.productcatalogservice.models.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto{
    private int id;
    private String title;
    private float price;
    private String description;
    private String category;
    private String image;
}
