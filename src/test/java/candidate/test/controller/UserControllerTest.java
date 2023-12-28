package candidate.test.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import candidate.test.dto.user.UserLoginRequestDto;
import candidate.test.dto.user.UserRegistrationRequestDto;
import candidate.test.exception.UserAlreadyExistException;
import candidate.test.model.User;
import candidate.test.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Add user with valid data should save user to DB and return 201 status")
    @Sql(scripts = "classpath:db/user/remove-all-users.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUser_WithValidData_ShouldAddUserToDbAndReturn201Status() throws Exception {
        UserRegistrationRequestDto registrationDto = new UserRegistrationRequestDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setUsername("johndoe")
                .setPassword("password")
                .setRepeatPassword("password");

        String jsonRequest = objectMapper.writeValueAsString(registrationDto);

        mockMvc.perform(post("/user/add")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Optional<User> expected = userRepository.findById(1L);

        Assertions.assertTrue(expected.isPresent());
    }

    @Test
    @DisplayName("Add user invalid password return 400 status")
    void addUser_WithInvalidPassword_ShouldReturn400Status() throws Exception {
        UserRegistrationRequestDto registrationDto = new UserRegistrationRequestDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setUsername("johndoe")
                .setPassword("pas")
                .setRepeatPassword("pas");

        String jsonRequest = objectMapper.writeValueAsString(registrationDto);

        mockMvc.perform(post("/user/add")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add user with different password and repeat password should return 400 status")
    void addUser_WithDifferentPasswordAndRepeatPassword_ShouldReturn400Status() throws Exception {
        UserRegistrationRequestDto registrationDto = new UserRegistrationRequestDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setUsername("johndoe")
                .setPassword("password")
                .setRepeatPassword("password123");

        String jsonRequest = objectMapper.writeValueAsString(registrationDto);

        mockMvc.perform(post("/user/add")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Authenticate with valid credentials should return 200 status")
    @Sql(scripts = "classpath:db/user/add-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/user/remove-all-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUser_WhenUserWithTheSameUsernameAlreadyExists_ShouldReturn400Status() throws Exception {
        UserRegistrationRequestDto registrationDto = new UserRegistrationRequestDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setUsername("johndoe")
                .setPassword("password")
                .setRepeatPassword("password");

        String jsonRequest = objectMapper.writeValueAsString(registrationDto);

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/user/add")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON));
        });

        assertTrue(exception.getCause() instanceof UserAlreadyExistException);
    }

    @Test
    @Sql(scripts = "classpath:db/user/add-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/user/remove-all-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void authentication_WithValidCredentials_ShouldReturn200Status() throws Exception {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto()
                .setUsername("johndoe")
                .setPassword("password");

        String jsonRequest = objectMapper.writeValueAsString(userLoginRequestDto);

        mockMvc.perform(post("/user/authenticate")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Authenticate with invalid credentials should return 400 status")
    @Sql(scripts = "classpath:db/user/add-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/user/remove-all-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void authentication_WithInvalidCredentials_ShouldReturn4xxStatus() throws Exception {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto()
                .setUsername("johndoe")
                .setPassword("incorrectpassword");

        String jsonRequest = objectMapper.writeValueAsString(userLoginRequestDto);

        mockMvc.perform(post("/user/authenticate")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
