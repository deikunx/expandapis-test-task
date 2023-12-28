package candidate.test.service;

import candidate.test.dto.UserRegistrationRequestDto;
import candidate.test.dto.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto add(UserRegistrationRequestDto request);
}
