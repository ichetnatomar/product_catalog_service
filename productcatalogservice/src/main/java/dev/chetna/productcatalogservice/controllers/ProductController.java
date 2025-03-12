package dev.chetna.productcatalogservice.controllers;

import dev.chetna.productcatalogservice.dtos.ProductDto;
import dev.chetna.productcatalogservice.exceptions.NotFoundException;
import dev.chetna.productcatalogservice.models.Product;
import dev.chetna.productcatalogservice.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private Product convertProductDtoToProduct(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setUrl(productDto.getImage());
        return product;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") int productId) throws NotFoundException {

        Optional<Product> optionalProduct =  productService.getProductById(productId);
        Product product = optionalProduct.orElseThrow(()->new NotFoundException("Product was not found."));
        //add headers
        MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
        headers.add("auth", "no access, sorry");
        ResponseEntity<Product> response = new ResponseEntity<>(product, headers, HttpStatus.OK);
        return response;
    }


    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto){
        Product product = convertProductDtoToProduct(productDto);
         Product responseProduct = productService.addProduct(product);
         MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
         headers.add("product added", "yes");
        ResponseEntity<Product> responseEntity = new ResponseEntity<>(responseProduct, headers, HttpStatus.ACCEPTED);
        return responseEntity;
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> replaceProduct(@PathVariable("productId") int productId, @RequestBody ProductDto productDto){
        Product product = convertProductDtoToProduct(productDto);
        Product responseProduct = productService.replaceProduct(productId, product);

        MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
        headers.add("is product replaced", "yes");

        ResponseEntity<Product> responseEntity = new ResponseEntity<>(responseProduct, headers, HttpStatus.OK);
        return responseEntity;
    }


    //take a dto request and id, convert it to product, send them to service, get back product, return a responseentity<product> with appropraite headers
    @PatchMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductDto productDto){
        Product product = convertProductDtoToProduct(productDto);
        Product responseProduct = productService.updateProduct(productId, product);
        MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
        headers.add("is product updated", "yes");
        ResponseEntity<Product> responseEntity = new ResponseEntity<>(product, headers, HttpStatus.OK);
        return  responseEntity;
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable("productId") int productId){
        return productService.deleteProduct(productId);
    }



}


