package dev.chetna.productcatalogservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.chetna.productcatalogservice.dtos.CartDto;
import dev.chetna.productcatalogservice.dtos.FakeStoreCartDto;
import dev.chetna.productcatalogservice.dtos.FakeStoreProductDto;
import dev.chetna.productcatalogservice.models.Cart;
import dev.chetna.productcatalogservice.models.Product;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FakeStoreCartServiceImpl implements CartService {
    @Autowired
    private final RestTemplateBuilder restTemplateBuilder;
    private final FakeStoreProductServiceImpl fakeStoreProductService;
    @Autowired
    private FakeStoreProductServiceImpl fakeStoreProductServiceImpl;


    public FakeStoreCartServiceImpl(RestTemplateBuilder restTemplateBuilder, FakeStoreProductServiceImpl fakeStoreProductService) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreProductService = fakeStoreProductService;
    }

    private List<Product> convertProductArrayToProductList(Product[] productArray){
        List<Product> listOfProducts = new ArrayList<>();
        for(Product product : productArray){
            listOfProducts.add(product);
        }
        return listOfProducts;
    }

    //convertFakeStoreCartDto to Cart
    private Cart convertFakeStoreCartDtoToCart(FakeStoreCartDto fakeStoreCartDto){

        //fakestorecartdto has an array of fakestoreroductdto. so
//       1. extract array of fakestoreproductdto from fakestorecartdto
//       2. make a list of Products,
//       3.iterate over fakestoreproductdto array and fetch product details for each product using productId from fakestoreproductDto
//       4. add product object to list<Products> product.
//       5. add this list to cart
        Cart cart = new Cart();
        cart.setId(fakeStoreCartDto.getId());
        cart.setUserId(fakeStoreCartDto.getUserId());

        FakeStoreProductDto[] fakeStoreProductDtos =  fakeStoreCartDto.getProducts();

        List<Product> products = new ArrayList<>();

        for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos){
            Optional<Product> optionalProduct = fakeStoreProductServiceImpl.getProductById(fakeStoreProductDto.getProductId());
            Product product = optionalProduct.orElse(null);
            products.add(product);
        }
        cart.setProducts(products);
        return cart;
    }

    private Product[] convertProductListToArray(List<Product> productList){
        int noOfProducts = productList.size();
        Product[] productArray = new Product[noOfProducts];
        for(int i = 0; i < noOfProducts; i++){
            productArray[i] =  productList.get(i);
        }
        return productArray;
    }

    //generic utility for REST API calls in which we configured execute()
    public <T> ResponseEntity<T> requestForEntity(String url, HttpMethod httpMethod, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    private Cart convertCartDtoToCart(CartDto cartDto){
        //cartDto has userId, cartId, and an array of productDtos
        return null;
    }

    private  String printJson(Object obj){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            String ObjectToString = objectMapper.writeValueAsString(obj);
            return ObjectToString;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "{}";
    }

    private CartDto convertCartToCartDto(Cart cart){
        CartDto cartDto = new CartDto();

        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUserId());
        //convert List to array and then  add to cartdto
        cartDto.setProducts(convertProductListToArray(cart.getProducts()));

        return cartDto;
    }

    @Override
    public List<Cart> getAllCarts(){
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreCartDto[]>response = restTemplate.getForEntity("https://fakestoreapi.com/carts", FakeStoreCartDto[].class);
        //extract fakestorecartdto from array one by one, get respective cart details from service getCartById()
//        and product details from product dto, make cart array and send
        List<Cart> carts = new ArrayList<>();

        for(FakeStoreCartDto fakeStoreCartDto : response.getBody()){
            int fakesStoreCartDto_id = fakeStoreCartDto.getId();
             Cart cart = getCartById(fakesStoreCartDto_id);
             carts.add(cart);
        }
        return carts;
    }

    @Override
    public Cart getCartById(int id) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        //get raw string from third party api
        ResponseEntity<String> response = restTemplate.getForEntity("https://fakestoreapi.com/carts/{id}", String.class, id);

        if (response.getBody() == null || response.getBody().trim().isEmpty() || "null".equals(response.getBody().trim())){
            throw new RuntimeException("Received empty response from FakeStore API for cart ID: " + id);
        }


        //parse this string and convert it into a cart object
        ObjectMapper objectMapper = new ObjectMapper();
        FakeStoreCartDto fakeStoreCartDto;

        try{
            fakeStoreCartDto = objectMapper.readValue(response.getBody(), FakeStoreCartDto.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("Error parsing FakeStoreCartDto response", e);
        }

        Cart cart = convertFakeStoreCartDtoToCart(fakeStoreCartDto);
        return cart;
    }

    @Override
    public Cart addCart(Cart cart) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        // Convert cart to DTO for sendting it forward to thirdparty api
        CartDto cartDto = convertCartToCartDto(cart);

        // Hit the FakeStore API
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://fakestoreapi.com/carts",
                cartDto,
                String.class // Get response as raw JSON string first
        );

        // Convert JSON string to DTO to ensure proper parsing
        ObjectMapper objectMapper = new ObjectMapper();
        FakeStoreCartDto responseCartDto;
        try {
            responseCartDto = objectMapper.readValue(response.getBody(), FakeStoreCartDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing FakeStoreCartDto response", e);
        }

        // Convert FakeStoreCartDto to Cart and return to controller
        Cart responseCart = convertFakeStoreCartDtoToCart(responseCartDto);

        return responseCart;
    }

    @Override
    public Cart replaceCart(int id, Cart cart) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> response =  restTemplate.postForEntity("https://fakestoreapi.com/carts/{id}", cart, String.class, id);
        String responseCartString = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        FakeStoreCartDto responseCartDto;
        try{
            responseCartDto = objectMapper.readValue(responseCartString, FakeStoreCartDto.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("Error parsing FakeStoreCartDto response", e);
        }

        Cart responseCart = convertFakeStoreCartDtoToCart(responseCartDto);
        return responseCart;

    }

    @Override
    public Cart updateCart(int id, Cart cart) {

        ResponseEntity<String> responseFakeStoreCartDto =  requestForEntity("https://fakestoreapi.com/carts/{id}", HttpMethod.PATCH, cart, String.class, id);
        //convert tgis string of fakestorecartdto to cart and return
        ObjectMapper objectMapper = new ObjectMapper();
        FakeStoreCartDto fakeStoreCartDto;
        try{
            fakeStoreCartDto =  objectMapper.readValue(responseFakeStoreCartDto.getBody(), FakeStoreCartDto.class);

        }catch(JsonProcessingException e){
            throw new RuntimeException("Error parsing FakeStoreCartDto response", e);
        }
        Cart resposneCart = convertFakeStoreCartDtoToCart(fakeStoreCartDto);
        return resposneCart;
    }

    @Override
    public String deleteCart(int id) {
       ResponseEntity<String> resposneString = requestForEntity("http://fakestoreapi.com/carts/{id}", HttpMethod.DELETE, null, String.class, id);
       return resposneString.getBody();
    }



}
