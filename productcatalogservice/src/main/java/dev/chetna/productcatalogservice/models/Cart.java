package dev.chetna.productcatalogservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart extends BaseModel{
    private int userId;

    @ManyToMany
    @JoinTable(
            name = "cart_products", //join table for many-to-many
            joinColumns = @JoinColumn(name = "cart_id"), //foreign key t Cart
            inverseJoinColumns = @JoinColumn(name = "product_id")//foriegn key to Product
    )
    private List<Product> productList;
}
