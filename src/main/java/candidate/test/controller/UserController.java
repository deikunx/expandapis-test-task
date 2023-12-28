package candidate.test.controller;

import candidate.test.dto.user.UserLoginRequestDto;
import candidate.test.dto.user.UserLoginResponseDto;
import candidate.test.dto.user.UserRegistrationRequestDto;
import candidate.test.dto.user.UserRegistrationResponseDto;
import candidate.test.security.AuthenticationService;
import candidate.test.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    UserRegistrationResponseDto add(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.add(request);
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
