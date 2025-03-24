package dev.chetna.productcatalogservice.repositories;

import dev.chetna.productcatalogservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserbyId(int id);
    User save(User user);
}
