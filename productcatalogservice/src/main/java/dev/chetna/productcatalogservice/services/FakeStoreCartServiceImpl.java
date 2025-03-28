package dev.chetna.productcatalogservice.services;

import dev.chetna.productcatalogservice.dtos.CartDto;
import dev.chetna.productcatalogservice.dtos.FakeStoreCartDto;
import dev.chetna.productcatalogservice.models.Cart;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreCartServiceImpl implements CartService {

    RestTemplateBuilder restTemplateBuilder;

    public FakeStoreCartServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
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
        Cart cart = new Cart();
        cart.setUserId(fakeStoreCartDto.getUserId());
        cart.setProductList(convertProductArrayToProductList(fakeStoreCartDto.getProductsArray()));
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

    private CartDto convertCartToCartDto(Cart cart){
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getId());
        cartDto.setUserId(cart.getUserId());

        cartDto.setProducts(convertProductListToArray(cart.getProductList()));
        return cartDto;
    }

    @Override
    public List<Cart> getAllCarts(){
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<CartDto[]>response = restTemplate.getForEntity("https://fakestoreapi.com/carts", CartDto[].class);
        //response body has cartDto[] or cartDtos
        //iterate over array, convert each cartDto to cart, add in list,and send list

        List<Cart> listOfCarts  = new ArrayList<>();
        for(CartDto cartDto : response.getBody()){
            Cart cart = convertCartDtoToCart(cartDto);
            listOfCarts.add(cart);
        }
        return listOfCarts;
    }

    @Override
    public Cart addCart(Cart cart){
        //convert cart object to cartdto, hit the fakestoreapi using resttemplate, get responseentity from it, extract fakestorecartdto from it,
//        convert it to cart and return

        RestTemplate restTemplate = restTemplateBuilder.build();
        CartDto cartDto = convertCartToCartDto(cart);
        ResponseEntity<FakeStoreCartDto> response = restTemplate.postForEntity("https://fakestoreapi.com/carts", cartDto,  FakeStoreCartDto.class);
        FakeStoreCartDto responseCartDto = response.getBody();
        Cart responseCart = convertFakeStoreCartDtoToCart(responseCartDto);
        return responseCart;
    }

}
