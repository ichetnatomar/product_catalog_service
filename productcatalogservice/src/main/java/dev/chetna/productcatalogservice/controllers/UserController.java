package dev.chetna.productcatalogservice.controllers;

import dev.chetna.productcatalogservice.dtos.UserDto;
import dev.chetna.productcatalogservice.exceptions.NotFoundException;
import dev.chetna.productcatalogservice.models.User;
import dev.chetna.productcatalogservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private User convertUserDtoToUser(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    @GetMapping
    public ResponseEntity<List> getAllUsers(){
        //get response of list from service, make a responseentity, add list to it and headers, and send
        List<User> usersList = userService.getAllUsers();
        MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
        headers.add("List of users", "yes");
        ResponseEntity<List> listOfUsers = new ResponseEntity<>(usersList, headers, HttpStatus.OK);
        return listOfUsers;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") int userId) throws NotFoundException {
       Optional<User> optionalUser = userService.getUserById(userId);
       User user = optionalUser.orElseThrow(()->new NotFoundException("User not found"));

       MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
       headers.add("user found", "yes");

       ResponseEntity<User> response= new ResponseEntity<>(user, headers, HttpStatus.OK);
       return response;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto){
        //convert userdto to user, and then send to userservice
        //you get response as user. make a responseentity, add header, and send it

        User user = convertUserDtoToUser(userDto);
        User responseUser = userService.addUser(user);

        MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
        headers.add("user added", "YES");

        ResponseEntity<User> response = new ResponseEntity<>(responseUser, headers, HttpStatus.CREATED);
        return response;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> replaceProduct(@PathVariable("userId") int userId, @RequestBody UserDto userDto){
        //convert userdto to user, send it to userservice, get back user, create a responseentity, add header and send
        User user = convertUserDtoToUser(userDto);
        User responseUser = userService.replaceUser(user, userId);

        MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
        headers.add("user relaced", "YES");

        ResponseEntity<User> response = new ResponseEntity<>(user, headers, HttpStatus.OK);
        return response;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateProduct(@PathVariable("userId") int userId, @RequestBody UserDto userDto){
        //convert userdto to user, send it to userservice, get back user, create a responseentity, add header and send
        User user = convertUserDtoToUser(userDto);
        User responseUser = userService.replaceUser(user, userId);

        MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
        headers.add("user relaced", "YES");

        ResponseEntity<User> response = new ResponseEntity<>(user, headers, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") int userId){
        String responseMessage = userService.deleteUser(userId);
        MultiValueMap<String, String>headers = new LinkedMultiValueMap<>();
        headers.add("user relaced", "YES");

        ResponseEntity<String> response = new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        return response;
    }








}
