package dev.chetna.productcatalogservice;

import dev.chetna.productcatalogservice.models.Product;
import dev.chetna.productcatalogservice.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductRepository productRepository;
    @Test
    @Transactional
    @Rollback(value = false)
    void savingProducts(){
        Product product = new Product();
        product.setId(1);
        product.setPrice(100);
        product.setUrl("hello");
        product.setCategory("phones");
        product.setDescription("iphone 15 description");
        product.setTitle("iphone 15");
        productRepository.save(product);

        Product product1 = new Product();
        product1.setId(2);
        product1.setPrice(200);
        product1.setUrl("hello again");
        product1.setCategory("tv");
        product1.setDescription("OnePlus TV description");
        product1.setTitle("OnePlus TV");
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setId(3);
        product2.setPrice(300);
        product2.setUrl("hello again..");
        product2.setCategory("dresses");
        product2.setDescription("dresses description");
        product2.setTitle("flared short dress");
        productRepository.save(product2);

        Product product3 = new Product();
        product3.setId(4);
        product3.setPrice(400);
        product3.setUrl("bye");
        product3.setCategory("shoes");
        product3.setDescription("shoes description");
        product3.setTitle("heels flatform");
        productRepository.save(product3);

    }
}
