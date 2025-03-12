package dev.chetna.productcatalogservice.repositories;

import dev.chetna.productcatalogservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Product save (Product product);
}
