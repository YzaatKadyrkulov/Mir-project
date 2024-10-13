package kg.mir.Mirproject.service.impl;

import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import kg.mir.Mirproject.dto.payment.PaymentRequest;
import kg.mir.Mirproject.dto.payment.SumRequest;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.*;
import kg.mir.Mirproject.entities.Payment;
import kg.mir.Mirproject.entities.TotalSum;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.enums.Status;
import kg.mir.Mirproject.enums.UserStatus;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.mapper.UserMapper;
import kg.mir.Mirproject.repository.TotalSumRepo;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public List<AllReceivedResponse> getAllReceivedUsers() {
        List<AllReceivedResponse> users = userRepository.getAllReceivedUsers();
        return users.isEmpty() ? Collections.emptyList() : users;
    }

    @Override
    public ReceivedResponse getReceivedUserById(Long id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User by id " + id + " not found"));
        return ReceivedResponse.builder()
                .id(user.getId())
                .userName(user.getUsername()).photoUrl(user.getPhotoUrl())
                .principalDebt(user.getPrincipalDebt()).payDebt(user.getPaidDebt())
                .remainingAmount(Math.abs(user.getPrincipalDebt() - user.getPaidDebt()))
                .payment(userRepository.getUsersPaymentById(user.getId())).build();
    }


    @Override
    public ProfileResponse updateUserProfileById(ProfileUpdateRequest profileUpdateRequest) {
        User user = getAuthentication();
        user.setUserName(profileUpdateRequest.name());
        user.setPhotoUrl(user.getPhotoUrl());
        user.setPhoneNumber(profileUpdateRequest.phoneNumber());
        user.setGoal(profileUpdateRequest.goal());
        userRepository.save(user);
        return ProfileResponse.builder()
                .id(user.getId())
                .photoUrl(user.getPhotoUrl())
                .goal(user.getGoal())
                .name(user.getUsername())
                .build();
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
                .message("User status successfully changed to submitted")
                .build();
    }

    @Override
    public List<GraduatedResponseOne> getAllGraduatedUsers() {
        List<GraduatedResponseOne> users = new ArrayList<>();
        List<User> all = userRepository.findAll();
        for(User user : all){
            if (user.getUserStatus() != null && user.getUserStatus().equals(UserStatus.FINISHED)){
                GraduatedResponseOne finishedUser = GraduatedResponseOne.builder()
                        .id(user.getId())
                        .userName(user.getUsername())
                        .photoUrl(user.getPhotoUrl())
                        .totalSum(user.getTotalSum() + user.getPaidDebt())
                        .build();
                users.add(finishedUser);
            }
        }
        return users;
    }

    @Override
    public List<List<UserStatusResponse>> searchUsers(String query) throws NotFoundException {
        List<UserStatusResponse> allUsers = userRepository.findByUserName(query);

        if (allUsers.isEmpty()) {
            throw new NotFoundException("Users not found");
        }

        List<UserStatusResponse> receivedUsers = allUsers.stream()
                .filter(user -> user.userStatus() == UserStatus.RECEIVED)
                .toList();

        List<UserStatusResponse> finishedUsers = allUsers.stream()
                .filter(user -> user.userStatus() == UserStatus.FINISHED)
                .toList();

        List<UserStatusResponse> submittedUsers = allUsers.stream()
                .filter(user -> user.userStatus() == UserStatus.SUBMITTED)
                .toList();

        return List.of(receivedUsers, finishedUsers, submittedUsers);
    }

    @Override
    public List<UserWorldResponse> getUsersByTotalSumRange(int minAmount, int maxAmount) {
        return userRepository.findByTotalSumRange(minAmount, maxAmount);
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
