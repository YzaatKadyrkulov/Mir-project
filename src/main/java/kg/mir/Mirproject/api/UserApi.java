package kg.mir.Mirproject.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import kg.mir.Mirproject.dto.AdminResponse;
import kg.mir.Mirproject.dto.PercentResponse;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldProfileResponse;
import kg.mir.Mirproject.dto.WorldDto.UserWorldResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.*;
import kg.mir.Mirproject.enums.UserStatus;
import kg.mir.Mirproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User-API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserApi {
    private final UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Обновить профиль пользователя по ID",
            description = "Обновляет профиль пользователя с указанным ID, используя предоставленные данные. Доступно только пользователю с ролью 'USER'."
    )
    @PutMapping("/updateProfile")
    public ResponseEntity<ProfileResponse> updateUserProfileById(
            @RequestBody @Valid ProfileUpdateRequest profileUpdateRequest) {
        ProfileResponse updatedProfile = userService.updateUserProfileById(profileUpdateRequest);
        return ResponseEntity.ok(updatedProfile);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Получить профиль пользователя по ID",
            description = "Возвращает профиль пользователя по указанному ID. Доступно только пользователю с ролью 'USER'."
    )
    @GetMapping("/userProfile")
    public ResponseEntity<ProfileResponse> getUserById() {
        ProfileResponse profileResponse = userService.getUserById();
        return ResponseEntity.ok(profileResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя по указанному ID. Доступно только пользователю с ролью 'ADMIN'."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deleteUserById(@PathVariable Long id) {
        SimpleResponse response = userService.deleteUserById(id);
        return ResponseEntity.ok(response);
    }

    @PermitAll
    @Operation(
            summary = "Получить всех пользователей, которые сдались",
            description = "Возвращает список всех пользователей со статусом 'сдался'. Доступно всем пользователям."
    )
    @GetMapping("/submitted")
    public ResponseEntity<List<SubmittedResponse>> getAllSubmittedUsers() {
        List<SubmittedResponse> submittedUsers = userService.getAllSubmittedUsers();
        return ResponseEntity.ok(submittedUsers);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Изменить статус пользователя на 'сдался'",
            description = "Изменяет статус пользователя на 'сдался' по указанному ID. Доступно только пользователю с ролью 'USER'."
    )
    @PatchMapping("/status/submitted")
    public ResponseEntity<SimpleResponse> changeUserStatusToSubmitted() {
        SimpleResponse response = userService.changeUserStatusToSubmitted();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(
            summary = "Получить всех закончивих пользователей",
            description = "Получает всех закончивших пользователей. Доступны только пользователям с ролью 'USER' и 'ADMIN'."
    )
    @GetMapping("/getAllGraduatedUsers")
    public ResponseEntity<List<GraduatedResponseOne>> getAllGraduatedUsers() {
        List<GraduatedResponseOne> users = userService.getAllGraduatedUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(
            summary = "Получить всех пользователей по рейтингу",
            description = "Получает всех пользователей из мира. Доступны только пользователям с ролью 'USER' и 'ADMIN'."
    )
    @GetMapping("/getUsersFromWorldByRating")
    public ResponseEntity<List<UserWorldResponse>> getUsersByTotalSumRange(@RequestParam int minAmount,
                                                                           @RequestParam int maxAmount) {
        List<UserWorldResponse> users = userService.getUsersByTotalSumRange(minAmount, maxAmount);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получить пользователя ",
            description = "Получает пользователей из мира. Доступны только пользователю с ролью 'ADMIN'."
    )
    @GetMapping("/getUserFromWorld/{id}")
    public ResponseEntity<Optional<UserWorldProfileResponse>> findUserById(@PathVariable Long id) {
        Optional<UserWorldProfileResponse> users = userService.findUserById(id);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(
            summary = "Получить всех пользователей, (получивший)",
            description = "Возвращает список всех пользователей, которые что-то получили. Доступны только пользователям с ролью 'USER' и 'ADMIN'."
    )
    @GetMapping("/allReceivedUsers")
    public ResponseEntity<List<AllReceivedResponse>> getAllReceivedUsers() {
        List<AllReceivedResponse> users = userService.getAllReceivedUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(
            summary = "Получить пользователя по ID (получивший)",
            description = "Возвращает пользователя, который что-то получил, по указанному ID. Доступны только пользователям с ролью 'USER' и 'ADMIN'."
    )
    @GetMapping("/receivedUser/{id}")
    public ResponseEntity<ReceivedResponse> getReceivedUserById(@PathVariable Long id) {
        ReceivedResponse user = userService.getReceivedUserById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получить профиль администратора",
            description = "Возвращает профиль администратора с глобальной суммой и списком пользователей. Доступно только пользователю с ролью 'ADMIN'."
    )
    @GetMapping("/adminProfile")
    public ResponseEntity<AdminResponse> getAdminProfileById() {
        AdminResponse adminResponse = userService.getAdminProfileById();
        return ResponseEntity.ok(adminResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Очистить пользователей по статусу",
            description = "Удаляет всех пользователей с указанным статусом. Доступно только пользователю с ролью 'ADMIN'."
    )
    @DeleteMapping("/clearUserByStatus")
    public ResponseEntity<String> clearUserByStatus(@RequestParam UserStatus status) {
        userService.clearUsersByStatus(status);
        return ResponseEntity.ok("Users with status: " + status + " were removed");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(
            summary = "Искать пользователя по имени и фамилии",
            description = "Ищет получившего пользователя по имени и фамилии. Доступны только пользователям с ролью 'USER' и 'ADMIN'."
    )
    @GetMapping("/search/receivedUser")
    public ResponseEntity<List<UserStatusResponse>> searchReceivedUser(@RequestParam("query") String query) {
            List<UserStatusResponse> users = userService.searchReceivedUser(query);
            return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(
            summary = "Искать пользователя по имени и фамилии",
            description = "Ищет закончившего пользователя по имени и фамилии. Доступны только пользователям с ролью 'USER' и 'ADMIN'."
    )
    @GetMapping("/search/finishedUser")
    public ResponseEntity<List<UserStatusResponse>> searchFinishedUser(@RequestParam("query") String query) {
        List<UserStatusResponse> users = userService.searchFinishedUser(query);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(
            summary = "Искать пользователя по имени и фамилии",
            description = "Ищет сдавшего пользователя по имени и фамилии. Доступны только пользователям с ролью 'USER' и 'ADMIN'."
    )
    @GetMapping("/search/submittedUser")
    public ResponseEntity<List<UserStatusResponse>> searchSubmittedUser(@RequestParam("query") String query) {
        List<UserStatusResponse> users = userService.searchSubmittedUser(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/percent/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Вывод процента пользователя",
            description = "Выводит пользователя и его проценты метод для 'ADMIN'"
    )
    public ResponseEntity<PercentResponse> getPercent(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getPercentUserById(userId));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получить проценты общей суммы администратора",
            description = "Возвращает профиль администратора с глобальной суммой и процентами сумм. Доступно только пользователю с ролью 'ADMIN'."
    )
    @GetMapping("/percent")
    public ResponseEntity<AdminResponse> getPercentSum() {
        AdminResponse adminResponse = userService.getPercent();
        return ResponseEntity.ok(adminResponse);
    }
}