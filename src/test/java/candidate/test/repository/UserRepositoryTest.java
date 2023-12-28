package candidate.test.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import candidate.test.model.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find user by username work correctly")
    void findByUsername() {
        String username = "johndoe";

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .password("qwerty1234")
                .username(username)
                .build();

        userRepository.save(user);

        User expected = User.builder()
                .id(1L)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();

        Optional<User> actual = userRepository.findByUsername(username);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}
