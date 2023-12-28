package candidate.test.service.impl;

import candidate.test.dto.user.UserRegistrationRequestDto;
import candidate.test.dto.user.UserRegistrationResponseDto;
import candidate.test.exception.UserAlreadyExistException;
import candidate.test.mapper.UserMapper;
import candidate.test.model.User;
import candidate.test.repository.UserRepository;
import candidate.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRegistrationResponseDto add(UserRegistrationRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistException(request.getUsername());
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userMapper.toRegistrationResponseDto(userRepository.save(user));
    }
}
