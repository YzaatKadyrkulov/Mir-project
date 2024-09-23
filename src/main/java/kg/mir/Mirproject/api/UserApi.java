package kg.mir.Mirproject.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.submittedDto.SubmittedResponse;
import kg.mir.Mirproject.dto.userDto.ProfileResponse;
import kg.mir.Mirproject.dto.userDto.ProfileUpdateRequest;
import kg.mir.Mirproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        ProfileResponse updatedProfile = userService.updateUserProfileById( profileUpdateRequest);
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
}
