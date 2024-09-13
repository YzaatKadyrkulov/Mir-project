package kg.mir.Mirproject.service;

import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.userDto.ProfileResponse;
import kg.mir.Mirproject.dto.userDto.ProfileUpdateRequest;

public interface UserService {
    ProfileResponse updateUserProfileById(Long id, ProfileUpdateRequest profileUpdateRequest);
    ProfileResponse getUserById(Long id);
    SimpleResponse deleteUserById(Long id);
}
