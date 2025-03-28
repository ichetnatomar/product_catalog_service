package dev.chetna.productcatalogservice.controllers;

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
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

//    private Cart convertCartDtoToCart(CartDto cartDto){
//        Cart cart = new Cart();
//        cart.setId(cartDto.getCartId());
//        cart.setUserId(cartDto.getUserId());
//
//        //convert Product[] to a list of Products
//        List<Product> listOfProducts = new ArrayList<>();
//        Product[] productArray = cartDto.getProducts();
//        for(Product product : productArray){
//            listOfProducts.add(product);
//        }
//        cart.setProductList(listOfProducts);
//        return cart;
//    }


    private Cart convertCartDtoToCart(CartDto cartDto) {
        if (cartDto == null) {
            throw new IllegalArgumentException("CartDto cannot be null");
        }

        Cart cart = new Cart();
        cart.setUserId(cartDto.getUserId());

        if (cartDto.getProducts() != null) {  // ✅ Check for null before conversion
            cart.setProductList(Arrays.asList(cartDto.getProducts()));
        } else {
            cart.setProductList(new ArrayList<>());  // ✅ Avoid null list issues
        }

        return cart;
    }



    @GetMapping
    public List<Cart> getAllCarts(){
        return cartService.getAllCarts();
    }

    @PostMapping
    public ResponseEntity<Cart> addCart(@RequestBody CartDto cartDto){

        //convert dto to cart, send it to service addCart(), it will relay request to fakestoreApi and return a cart, convert it to dto and wrap in a responseentity wht headers and return


        System.out.println("Received CartDto: " + cartDto);

        // Additional null check for products
        if (cartDto.getProducts() == null) {
            System.out.println("Products is NULL in CartDto!");
        } else {
            System.out.println("Products list size: " + cartDto.getProducts().length);
        }

        Cart cart = convertCartDtoToCart(cartDto);

        System.out.println("cartDto from ui: "+cartDto);

        Cart responseCart = cartService.addCart(cart);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cart added", "yes");

        ResponseEntity<Cart> responseEntity = new ResponseEntity<>(responseCart, headers, HttpStatus.CREATED);
        return responseEntity;
    }
}
