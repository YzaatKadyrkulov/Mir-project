package kg.mir.Mirproject.service.impl;

import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.userDto.ProfileResponse;
import kg.mir.Mirproject.dto.userDto.ProfileUpdateRequest;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public ProfileResponse updateUserProfileById(Long id, ProfileUpdateRequest profileUpdateRequest) {
        User user = userRepository.getUserById(id).
                orElseThrow(() -> new NotFoundException("User by id " + id + " not found"));
        return ProfileResponse
                .builder()
                .photoUrl(user.getPhotoUrl())
                .fullName(user.getUsername())
                .goal(user.getGoal())
                .build();
    }

    @Override
    public ProfileResponse getUserById(Long id) {
        User user = userRepository.getUserById(id).
                orElseThrow(() -> new NotFoundException("User by id " + id + " not found"));
        return ProfileResponse
                .builder()
                .photoUrl(user.getPhotoUrl())
                .fullName(user.getUsername())
                .goal(user.getGoal())
                .build();
    }

    @Override
    public SimpleResponse deleteUserById(Long id) {
        User user = userRepository.getUserById(id).
                orElseThrow(() -> new NotFoundException("User by id " + id + " not found"));
        userRepository.delete(user);
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("User "+user.getUsername()+" successfully deleted").build();
    }
}