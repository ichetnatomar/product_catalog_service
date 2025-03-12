package dev.chetna.productcatalogservice.services;

import dev.chetna.productcatalogservice.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> getAllUsers();

    public Optional<User> getUserById(int userId);

    public User addUser(User user);

    public User replaceUser(User user, int userId);

    public User updateUser(User user, int userId);

    public String deleteUser(int userId);
}
