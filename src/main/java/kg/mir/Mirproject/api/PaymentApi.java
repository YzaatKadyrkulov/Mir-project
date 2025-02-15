package kg.mir.Mirproject.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kg.mir.Mirproject.dto.AdminResponse;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.payment.DebtRequest;
import kg.mir.Mirproject.dto.MirUsersResponse;
import kg.mir.Mirproject.dto.payment.PaymentRequest;
import kg.mir.Mirproject.dto.payment.SumRequest;
import kg.mir.Mirproject.enums.PercentType;
import kg.mir.Mirproject.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Tag(name = "Payment-API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentApi {
    private final PaymentService paymentService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавить платеж к пользователю (закрывать долги)",
            description = "Добавляет платеж к пользователю по указанному ID. Доступно только пользователю с ролью 'ADMIN'."
    )
    @PostMapping("/{userId}/addPayment")
    public ResponseEntity<SimpleResponse> addPaymentToUser(@PathVariable Long userId,
                                                           @RequestBody @Valid PaymentRequest paymentRequest) {
        SimpleResponse response = paymentService.payDebtsOfUser(userId, paymentRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Добавить сумму к пользователю",
            description = "Добавляет указанную сумму к пользователю. Доступно только пользователю с ролью 'ADMIN'."
    )
    @PostMapping("/addSum")
    public ResponseEntity<SimpleResponse> addSumToUser(@RequestBody @Valid SumRequest sumRequest) {
        SimpleResponse response = paymentService.addSumToUser(sumRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Возврат всей суммы пользователя",
            description = "Возвращает всю сумму пользователя. Доступно только пользователю с ролью 'ADMIN'."
    )
    @PostMapping("/refundSum")
    public ResponseEntity<SimpleResponse> refundAllSumOfUser(@RequestBody @Valid SumRequest sumRequest) {
        SimpleResponse response = paymentService.refundAllSumOfUser(sumRequest);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Получить данные пользователя по миру",
            description = "Возвращает информацию о мире пользователя по ID. Доступно только пользователю с ролью 'ADMIN'."
    )
    @GetMapping("/userWorld/{userId}")
    public ResponseEntity<MirUsersResponse> getUserWorldById(@PathVariable Long userId) {
        MirUsersResponse response = paymentService.getUserWorldById(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Выдать долг пользователю",
            description = "Выдает долг пользователю по указанному ID. Доступно только пользователю с ролью 'ADMIN'."
    )
    @PostMapping("/{userId}/giveDebt")
    public ResponseEntity<SimpleResponse> giveDebtToUser(@PathVariable Long userId,
                                                         @RequestBody @Valid DebtRequest debtRequest) {
        SimpleResponse response = paymentService.giveDebtToUser(userId, debtRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Обновляет проценты",
            description = "Обновляет проценты каждой категории Сотрудники, Страховка, Программа." +
                    " Доступно только пользователю с ролью 'ADMIN'."
    )
    @PutMapping("update/percent")
    public ResponseEntity<AdminResponse> updatePercent(@RequestParam
                                                        @NotNull(message = "PercentType cannot be null") PercentType percentType,
                                                        @RequestParam
                                                        @NotNull(message = "Sum cannot be null")
                                                        @Min(value = 0, message = "Sum must be greater than 0") Double sum) {
        AdminResponse adminResponse = paymentService.updatePercent(percentType,sum);
        return ResponseEntity.ok(adminResponse);
    }
}
