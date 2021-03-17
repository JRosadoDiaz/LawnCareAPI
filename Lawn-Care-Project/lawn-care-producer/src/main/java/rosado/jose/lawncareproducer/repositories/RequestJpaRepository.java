package rosado.jose.lawncareproducer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rosado.jose.lawncareproducer.model.Request;

public interface RequestJpaRepository extends JpaRepository<Request, Integer> {
}
