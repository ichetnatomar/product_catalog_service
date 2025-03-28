package dev.chetna.productcatalogservice.services;

import dev.chetna.productcatalogservice.models.Cart;

import java.util.List;

public interface CartService {
    public List<Cart> getAllCarts();
    public Cart addCart(Cart cart);
    public Cart getCartById(int id);
}
