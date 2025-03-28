package dev.chetna.productcatalogservice;

import dev.chetna.productcatalogservice.models.Product;
import dev.chetna.productcatalogservice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductRepository productRepository;
    @Test
    void savingProducts(){
        Product product = new Product();
        product.setPrice(100);
        product.setUrl("hello");
        product.setCategory("phones");
        productRepository.save(product);
    }
}
