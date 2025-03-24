package dev.chetna.productcatalogservice.repositories;

import dev.chetna.productcatalogservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);
    Product save(Product product);
}
