package dev.chetna.productcatalogservice.repositories;

import dev.chetna.productcatalogservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
//    public Product save (Product product);
//    Product findProductById(int Id);
}
