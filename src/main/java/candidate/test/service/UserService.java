package candidate.test.service;

import candidate.test.dto.user.UserRegistrationRequestDto;
import candidate.test.dto.user.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto add(UserRegistrationRequestDto request);
}
