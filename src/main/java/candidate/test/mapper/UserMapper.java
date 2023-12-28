package candidate.test.mapper;

import candidate.test.config.MapperConfig;
import candidate.test.dto.UserRegistrationResponseDto;
import candidate.test.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserRegistrationResponseDto toRegistrationResponseDto(User user);
}
