package dev.chetna.productcatalogservice.services;
import java.util.List;
import java.util.Optional;

import dev.chetna.productcatalogservice.models.Product;

public interface ProductService {
    public List<Product> getAllProducts();

    public Optional<Product> getProductById(int productId);

    public Product addProduct(Product product);

    public Product replaceProduct(int productId, Product product);

    public Product updateProduct(int productId, Product product);

    public String deleteProduct(int productId);
}