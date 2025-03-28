package dev.chetna.productcatalogservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User  extends BaseModel{
    @Id
    private int id;
    private String username;
    private String email;
    private String password;
}
