package dev.chetna.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreUserDto {
    private int id;
    private String username;
    private String email;
    private String password;
}
