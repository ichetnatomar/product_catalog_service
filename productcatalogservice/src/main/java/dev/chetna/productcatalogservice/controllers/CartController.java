package dev.chetna.productcatalogservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.chetna.productcatalogservice.dtos.CartDto;
import dev.chetna.productcatalogservice.models.Cart;
import dev.chetna.productcatalogservice.models.Product;
import dev.chetna.productcatalogservice.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private Cart convertCartDtoToCart(CartDto cartDto) {
        if (cartDto == null) {
            throw new IllegalArgumentException("CartDto cannot be null");
        }

        Cart cart = new Cart();
        cart.setId(cartDto.getId());
        cart.setUserId(cartDto.getUserId());

        if (cartDto.getProducts() != null) {  // ✅ Check for null before conversion
            //print list of products of cart
            List<Product> products = new ArrayList<>();

            //populate list from array of products
            for(Product product : cartDto.getProducts()) {
                products.add(product);
            }

            cart.setProducts(products);

        } else {
            cart.setProducts(new ArrayList<>());  // ✅ Avoid null list issues
        }
        return cart;
    }

    private  String printJson(Object obj){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            String cartDtoJsonToString = objectMapper.writeValueAsString(obj);
            return cartDtoJsonToString;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "{}";
    }


    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts(){
        List<Cart> cartList =  cartService.getAllCarts();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("got list of cart", "Yes");

        ResponseEntity<List<Cart>> response = new ResponseEntity<>(cartList, headers, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") int id){
       Cart responseCart = cartService.getCartById(id);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cart retrieved", "yes");

        ResponseEntity<Cart> responseEntity = new ResponseEntity<>(responseCart, headers, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<Cart> addCart(@RequestBody CartDto cartDto){

        //convert dto to cart, send it to service addCart(), it will relay request to fakestoreApi and return a cart, convert it to dto and wrap in a responseentity wht headers and return
        Cart cart = convertCartDtoToCart(cartDto);

        Cart responseCart = cartService.addCart(cart);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cart added", "yes");

        ResponseEntity<Cart> responseEntity = new ResponseEntity<>(responseCart, headers, HttpStatus.OK);
        return responseEntity;
    }

    @PatchMapping("{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable("id") int id, @RequestBody CartDto cartDto){
        //convert the cartdto to cart, send to service class, get a cart resposne, amke a reposne entity and return
        Cart cart = convertCartDtoToCart(cartDto);
        Cart responseCart = cartService.updateCart(id, cart);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cart updated", "yes");

        ResponseEntity<Cart> responseEntity = new ResponseEntity<>(responseCart, headers, HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCart(@PathVariable("id") int id){

        String response = cartService.deleteCart(id);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cart deleted", "yes");

        ResponseEntity<String> responseEntity = new ResponseEntity<>(response, headers, HttpStatus.OK);
        return responseEntity;
    }


}
