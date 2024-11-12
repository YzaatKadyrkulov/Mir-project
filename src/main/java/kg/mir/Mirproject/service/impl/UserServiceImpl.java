package kg.mir.Mirproject.service.impl;

import jakarta.transaction.Transactional;
import kg.mir.Mirproject.dto.AdminResponse;
import kg.mir.Mirproject.dto.PercentResponse;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.WorldDto.AllUsersResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.*;
import kg.mir.Mirproject.entities.TotalSum;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.enums.UserStatus;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.TotalSumRepo;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TotalSumRepo totalSumRepo;

    @Override
    public List<AllReceivedResponse> getAllReceivedUsers() {
        List<AllReceivedResponse> users = userRepository.getAllReceivedUsers();
        return users.isEmpty() ? Collections.emptyList() : users;
    }

    @Override
    public ReceivedResponse getReceivedUserById(Long id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        return ReceivedResponse.builder()
                .id(user.getId())
                .userName(user.getUsername()).photoUrl(user.getPhotoUrl())
                .totalSum(user.getTotalSum())
                .principalDebt(user.getPrincipalDebt()).payDebt(user.getPaidDebt())
                .remainingAmount(Math.abs(user.getPrincipalDebt() - user.getPaidDebt()))
                .payment(userRepository.getUsersPaymentById(user.getId())).build();
    }

    @Override
    public AdminResponse getAdminProfileById() {
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseThrow(() -> new NotFoundException("Общая сумма не найдена"));
        List<AllUsersResponse> users = userRepository.getAllWorldUsers();
        if (users.isEmpty()) {
            return AdminResponse.builder().globalSum(totalSum.getTotalSum()).users(Collections.emptyList()).build();
        }
        return AdminResponse.builder().globalSum(totalSum.getTotalSum()).users(userRepository.getAllWorldUsers()).build();
    }

    @Override
    @Transactional
    public void clearUsersByStatus(UserStatus userStatus) {
        userRepository.deleteAllByUserStatus(userStatus);
    }

    @Override
    public List<UserStatusResponse> searchReceivedUser(String query) {
        List<UserStatusResponse> result = userRepository.searchReceivedUser(query);
        if (result.isEmpty()) {
            throw new NotFoundException("Не найдено полученных пользователей по запросу: " + query);
        }
        return result;
    }

    @Override
    public List<UserStatusResponse> searchFinishedUser(String query) {
        List<UserStatusResponse> result = userRepository.searchFinishedUser(query);
        if (result.isEmpty()) {
            throw new NotFoundException("Не найдено завершенных пользователей по запросу: " + query);
        }
        return result;
    }

    @Override
    public List<UserStatusResponse> searchSubmittedUser(String query) {
        List<UserStatusResponse> result = userRepository.searchSubmittedUser(query);
        if (result.isEmpty()) {
            throw new NotFoundException("Не найдено отправленных пользователей по запросу: " + query);
        }
        return result;
    }


    @Override
    public ProfileResponse updateUserProfileById(ProfileUpdateRequest profileUpdateRequest) {
        User user = getAuthentication();
        user.setUserName(profileUpdateRequest.name());
        user.setPhotoUrl(profileUpdateRequest.photoUrl());
        user.setPhoneNumber(profileUpdateRequest.phoneNumber());
        user.setGoal(profileUpdateRequest.goal());
        userRepository.save(user);
        return ProfileResponse.builder()
                .id(user.getId())
                .number(user.getPhoneNumber())
                .photoUrl(user.getPhotoUrl())
                .goal(user.getGoal())
                .name(user.getUsername())
                .build();
    }

    @Override
    public ProfileResponse getUserById() {
        User user = getAuthentication();
        return ProfileResponse.builder()
                .id(user.getId())
                .photoUrl(user.getPhotoUrl())
                .name(user.getUsername())
                .number(user.getPhoneNumber())
                .goal(user.getGoal())
                .totalSum(user.getTotalSum())
                .principalDebt(user.getPrincipalDebt())
                .payDebt(user.getPaidDebt())
                .remainingAmount(Math.abs(user.getPrincipalDebt() - user.getPaidDebt()))
                .payment(userRepository.getUsersPaymentById(user.getId()))
                .build();
    }

    @Override
    public SimpleResponse deleteUserById(Long id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        userRepository.delete(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Пользователь " + user.getUsername() + " успешно удален")
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
                .message("Статус пользователя успешно изменен на сдавший")
                .build();
    }

    @Override
    public List<GraduatedResponseOne> getAllGraduatedUsers() {
        List<GraduatedResponseOne> users = new ArrayList<>();
        List<User> all = userRepository.findAll();
        for (User user : all) {
            if (user.getUserStatus() != null && user.getUserStatus().equals(UserStatus.FINISHED)) {
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
    public List<UserWorldResponse> getUsersByTotalSumRange(int minAmount, int maxAmount) {
        return userRepository.findByTotalSumRange(minAmount, maxAmount);
    }

    @Override
    public Optional<UserWorldProfileResponse> findUserById(Long id) {
        return Optional.ofNullable(userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден")));
    }

    @Override
    public PercentResponse getPercentUserById(Long id) {
        return userRepository.findPercentResponseByUserId(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));

    }

    private User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с электронной почтой " + email + " не найден"));
    }
}
