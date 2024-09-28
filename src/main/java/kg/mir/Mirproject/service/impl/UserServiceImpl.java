package kg.mir.Mirproject.service.impl;

import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.GraduatedResponse;
import kg.mir.Mirproject.dto.userDto.ProfileResponse;
import kg.mir.Mirproject.dto.userDto.ProfileUpdateRequest;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.enums.UserStatus;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.mapper.UserMapper;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public ProfileResponse updateUserProfileById(ProfileUpdateRequest profileUpdateRequest) {
        User user = getAuthentication();
        mapper.updateUserProfile(profileUpdateRequest, user);
        userRepository.save(user);
        return mapper.toResponse(user);
    }

    @Override
    public ProfileResponse getUserById() {
        return mapper.toResponse(getAuthentication());
    }

    @Override
    public SimpleResponse deleteUserById(Long id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User by id " + id + " not found"));
        userRepository.delete(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("User " + user.getUsername() + " successfully deleted")
                .build();
    }

    @Override
    public List<SubmittedResponse> getAllSubmittedUsers() {
        List<SubmittedResponse> users = userRepository.getAllSubmittedUsers();
        return users.isEmpty() ? Collections.emptyList() : users;
    }

    @Override
    public SimpleResponse changeUserStatusToSubmitted() {
        User user = getAuthentication();
        user.setUserStatus(UserStatus.SUBMITTED);
        userRepository.save(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("User " + user.getUsername() + " successfully deleted")
                .build();
    }

    @Override
    public List<GraduatedResponse> getAllGraduatedUsers() {
        List<GraduatedResponse> users = userRepository.getAllGraduatedUsers();
        return users.isEmpty() ? Collections.emptyList() : users;
    }

    @Override
    public List<User> searchUsers(String query) {
        List<User> users = userRepository.findByUserName(query);
        return users.isEmpty() ? Collections.emptyList() : users;
    }

    @Override
    public List<UserWorldResponse> getUsersByTotalSumRange(int minAmount, int maxAmount) {
        return userRepository.findByTotalSumBetween(minAmount, maxAmount);
    }

    @Override
    public Optional<UserWorldProfileResponse> findUserById(Long id) {
        return Optional.ofNullable(userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found")));
    }

    private User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с электронной почтой " + email + " не найден"));
    }
}
