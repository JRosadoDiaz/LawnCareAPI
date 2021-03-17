package rosado.jose.lawncareproducer.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import rosado.jose.lawncareproducer.model.User;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Cacheable(value = "users")
    List<User> findAll();

    @CacheEvict(value = "users", allEntries = true)
    User save(User user);
}
