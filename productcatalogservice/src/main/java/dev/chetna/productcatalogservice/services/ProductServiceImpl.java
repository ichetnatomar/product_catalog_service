package dev.chetna.productcatalogservice.services;

import dev.chetna.productcatalogservice.dtos.ProductDto;
import dev.chetna.productcatalogservice.models.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService{

    RestTemplateBuilder restTemplateBuilder;

    public ProductServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    private Product convertProductDtotoProduct(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setUrl(productDto.getImage());
        return product;
    }

    public ProductDto convertProductToProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setDescription(product.getDescription());
        productDto.setCategory(product.getCategory());
        productDto.setPrice(product.getPrice());
        productDto.setImage(product.getUrl());
        return productDto;
    }

    //generic utility for REST API calls in which we configured execute()
    public <T> ResponseEntity<T> requestForEntity(String url, HttpMethod httpMethod, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }
    //hit the url of thirdpartyApi, it returns an array of productDto Objects, convert this array into a list of products and send
    public List<Product> getAllProducts(){
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<ProductDto[]> response = restTemplate.getForEntity("https://fakestoreapi.com/products", ProductDto[].class);

//        iterate over response body, convert productdto to product, add to list, return it

        List<Product> listOfProducts = new ArrayList<>();

        for(ProductDto productDto : response.getBody()){
            Product product = convertProductDtotoProduct(productDto);
            listOfProducts.add(product);
        }
        return listOfProducts;
    }

    public Optional<Product> getProductById(int productId){
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<ProductDto> response = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", ProductDto.class, productId);
        return response.getBody() == null ? Optional.empty() : Optional.of(convertProductDtotoProduct(response.getBody()));
    }

    public Product addProduct(Product product){

        ProductDto productDto = convertProductToProductDto(product);
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<ProductDto> response = restTemplate.postForEntity("https://fakestoreapi.com/products", productDto, ProductDto.class);
        ProductDto responseProductDto = response.getBody();
        Product responseProduct = convertProductDtotoProduct(responseProductDto);
        return responseProduct;
    }

    public Product replaceProduct(int productId, Product product){

        ProductDto productDto = convertProductToProductDto(product);

        ResponseEntity<ProductDto> response = requestForEntity("https://fakestoreapi.com/products/{productId}", HttpMethod.PUT, productDto, ProductDto.class, productId);
        ProductDto responseProductDto = response.getBody();
        Product responseProduct = convertProductDtotoProduct(responseProductDto);
        return responseProduct;
    }

    //get productid, and product from controller, convert product to productDto, send reuqest to thirdpartyapi, get responseentity,
    //extract dto from it convert it to product and send
    public Product updateProduct(int productId, Product product){
        ProductDto productDto = convertProductToProductDto(product);
        ResponseEntity<ProductDto> response = requestForEntity("https://fakestoreapi.com/products/{productId}", HttpMethod.PATCH, productDto, ProductDto.class, productId);
        ProductDto responseProductDto = response.getBody();
        return convertProductDtotoProduct(responseProductDto);
    }

    public String deleteProduct(int productId){
       ResponseEntity<String> response = requestForEntity("https://fakestoreapi.com/products/{productId}", HttpMethod.DELETE, null, String.class, productId);
       return response.getBody();
    }
}
