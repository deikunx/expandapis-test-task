package candidate.test.repository;

import candidate.test.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);
}
