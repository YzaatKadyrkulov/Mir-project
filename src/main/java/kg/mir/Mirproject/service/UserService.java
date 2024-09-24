package kg.mir.Mirproject.service;

import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.GraduatedResponse;
import kg.mir.Mirproject.dto.userDto.ProfileResponse;
import kg.mir.Mirproject.dto.userDto.ProfileUpdateRequest;
import kg.mir.Mirproject.entities.User;

import java.util.List;

public interface UserService {
    ProfileResponse updateUserProfileById(ProfileUpdateRequest profileUpdateRequest);

    ProfileResponse getUserById();

    SimpleResponse deleteUserById(Long id);

    List<SubmittedResponse> getAllSubmittedUsers();

    SimpleResponse changeUserStatusToSubmitted();

    List<GraduatedResponse> getAllGraduatedUsers();

    List<User> searchUsers(String query);
}
