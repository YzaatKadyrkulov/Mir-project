package kg.mir.Mirproject.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.authDto.AuthResponse;
import kg.mir.Mirproject.dto.authDto.SignInRequest;
import kg.mir.Mirproject.dto.authDto.SignUpRequest;
import kg.mir.Mirproject.dto.userDto.ResetPasswordRequest;
import kg.mir.Mirproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth-API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthApi {
    private final AuthService authService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/signUp")
    public SimpleResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @PostMapping("/signIn")
    public AuthResponse signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }
    @PostMapping("/forgot-password")
    @Operation(
            summary = "Метод для сброса пароля через электронную почту!",
            description = "Этот метод отправляет пользователю ссылку для сброса пароля на указанный email." +
                    " Права на метод имеют все!")
    public SimpleResponse forgotPassword(@RequestParam("email") String email, @RequestParam("link") String link) {
        return authService.forgotPassword(email, link);
    }

    @PostMapping("/reset_password")
    @Operation(
            summary = "Метод для сброса пароля пользователя!",
            description = "Этот метод позволяет пользователю установить новый пароль," +
                    "используя ссылку, полученную по электронной почте. Доступ к методу имеют все!")
    public SimpleResponse resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }
}
