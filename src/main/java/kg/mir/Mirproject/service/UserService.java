package kg.mir.Mirproject.service;

import kg.mir.Mirproject.dto.AdminResponse;
import kg.mir.Mirproject.dto.PercentResponse;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.*;
import kg.mir.Mirproject.enums.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ProfileResponse updateUserProfileById(ProfileUpdateRequest profileUpdateRequest);

    ProfileResponse getUserById();

    SimpleResponse deleteUserById(Long id);

    List<SubmittedResponse> getAllSubmittedUsers();

    SimpleResponse changeUserStatusToSubmitted();

    List<GraduatedResponseOne> getAllGraduatedUsers();

    List<UserWorldResponse> getUsersByTotalSumRange(int minAmount, int maxAmount);

    Optional<UserWorldProfileResponse> findUserById(Long id);

    List<AllReceivedResponse> getAllReceivedUsers();

    ReceivedResponse getReceivedUserById(Long id);

    AdminResponse getAdminProfileById();

    void clearUsersByStatus(UserStatus userStatus);

    List<UserStatusResponse> searchReceivedUser(String query);

    List<UserStatusResponse> searchFinishedUser(String query);

    List<UserStatusResponse> searchSubmittedUser(String query);

    PercentResponse getPercentUserById(Long id);

    AdminResponse getPercent();
}
