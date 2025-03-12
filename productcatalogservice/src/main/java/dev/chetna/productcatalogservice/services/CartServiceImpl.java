package dev.chetna.productcatalogservice.services;

import dev.chetna.productcatalogservice.dtos.CartDto;
import dev.chetna.productcatalogservice.models.Cart;
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
public class CartServiceImpl implements CartService {

    RestTemplateBuilder restTemplateBuilder;

    public CartServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
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
}
