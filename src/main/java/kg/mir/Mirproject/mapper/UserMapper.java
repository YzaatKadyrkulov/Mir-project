package kg.mir.Mirproject.mapper;

import kg.mir.Mirproject.dto.userDto.ProfileResponse;
import kg.mir.Mirproject.dto.userDto.ProfileUpdateRequest;
import kg.mir.Mirproject.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ProfileResponse toResponse(User user);

    void updateUserProfile(ProfileUpdateRequest profileUpdateRequest, @MappingTarget User user);
}

