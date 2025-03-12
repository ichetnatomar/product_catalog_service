package dev.chetna.productcatalogservice.services;

import dev.chetna.productcatalogservice.dtos.FakeStoreUserDto;
import dev.chetna.productcatalogservice.dtos.UserDto;
import dev.chetna.productcatalogservice.models.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class UserServiceImpl implements UserService{
    RestTemplateBuilder restTemplateBuilder;

    public UserServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    private User convertFakeStorUserDtoToUser(FakeStoreUserDto fakeStoreUserDto){
        User user = new User();
        user.setId(fakeStoreUserDto.getId());
        user.setUsername(fakeStoreUserDto.getUsername());
        user.setEmail(fakeStoreUserDto.getEmail());
        user.setPassword(fakeStoreUserDto.getPassword());
        return user;
    }

    public UserDto convertUserToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public <T> ResponseEntity<T> requestForEntity(String url, HttpMethod httpMethod, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    public List<User> getAllUsers(){
        //go to thirdparty endpoint, and get its response of itd=s dto arrays, extract array from body, and for each fakestroreuserdto, convert itto user and add to list and send
        RestTemplate restTemplate = restTemplateBuilder.build();
       ResponseEntity<FakeStoreUserDto[]> response =  restTemplate.getForEntity("https://fakestoreapi.com/users", FakeStoreUserDto[].class);

       List<User> usersList = new ArrayList<>();

       for(FakeStoreUserDto fakeStoreUserDto : response.getBody()){
           User user = new User();
           user.setId(fakeStoreUserDto.getId());
           user.setUsername(fakeStoreUserDto.getUsername());
           user.setEmail(fakeStoreUserDto.getEmail());
           user.setPassword(fakeStoreUserDto.getPassword());
           usersList.add(user);
       }
        return usersList;
    }

    public Optional<User> getUserById(int userId){
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreUserDto> response = restTemplate.getForEntity("https://fakestoreapi.com/users/{userid}", FakeStoreUserDto.class, userId);
        Optional<User> optionalUser = response.getBody() == null ? Optional.empty() : Optional.of(convertFakeStorUserDtoToUser(response.getBody()));
        return optionalUser;
    }

    public User addUser(User user){
        //convert user to userdto, then send across url to add to fakestore. you will get responseentity of fakestoreuserdto, convert it back to user and send
        UserDto userDto = convertUserToUserDto(user);
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreUserDto> response = restTemplate.postForEntity("https://fakestoreapi.com/users", userDto, FakeStoreUserDto.class);
        FakeStoreUserDto fakeStoreUserDto = response.getBody();
        User responseUser = convertFakeStorUserDtoToUser(fakeStoreUserDto);
        return responseUser;
    }

    public User replaceUser(User user, int userId){
        //convert a user to userdto, send it and id to url, you get a responseentity<fakestoreuser>,
//        get fakestoreuserdto from it, convert it to user, and send

        UserDto userDto = convertUserToUserDto(user);
        ResponseEntity<FakeStoreUserDto> response = requestForEntity("https://fakestoreapi.com/users/{userId}", HttpMethod.PUT, userDto, FakeStoreUserDto.class, userId);
        FakeStoreUserDto fakeStoreUserDto = response.getBody();
        User responseUser = convertFakeStorUserDtoToUser(fakeStoreUserDto);
        return responseUser;
    }

    public User updateUser(User user, int userId){
        //convert a user to userdto, send it and id to url, you get a responseentity<fakestoreuser>,
//        get fakestoreuserdto from it, convert it to user, and send

        UserDto userDto = convertUserToUserDto(user);
        ResponseEntity<FakeStoreUserDto> response = requestForEntity("https://fakestoreapi.com/users/{userId}", HttpMethod.PATCH, userDto, FakeStoreUserDto.class, userId);
        FakeStoreUserDto fakeStoreUserDto = response.getBody();
        User responseUser = convertFakeStorUserDtoToUser(fakeStoreUserDto);
        return responseUser;
    }

    public String deleteUser(int userId){
        //go to url with delete request, send a success messgae from responseentity
       ResponseEntity<String> response = requestForEntity("https://fakestoreapi.com/users/{userId}", HttpMethod.DELETE, null, String.class, userId );
        String message = response.getBody();
        return message;
    }
}
