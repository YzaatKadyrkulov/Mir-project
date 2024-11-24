package kg.mir.Mirproject.service.impl;

import kg.mir.Mirproject.dto.AdminResponse;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.payment.DebtRequest;
import kg.mir.Mirproject.dto.MirUsersResponse;
import kg.mir.Mirproject.dto.payment.PaymentRequest;
import kg.mir.Mirproject.dto.payment.SumRequest;
import kg.mir.Mirproject.entities.Payment;
import kg.mir.Mirproject.entities.TotalSum;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.enums.PercentType;
import kg.mir.Mirproject.enums.Status;
import kg.mir.Mirproject.enums.UserStatus;
import kg.mir.Mirproject.exception.BadCredentialException;
import kg.mir.Mirproject.exception.BadRequestException;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.PaymentRepo;
import kg.mir.Mirproject.repository.TotalSumRepo;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.service.PaymentService;
import kg.mir.Mirproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final UserRepository userRepository;
    private final TotalSumRepo totalSumRepo;
    private final PaymentRepo paymentRepo;
    private final UserService userService;

    @Override
    public SimpleResponse payDebtsOfUser(Long id, PaymentRequest paymentRequest) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден"));
        Payment payment = new Payment();
        if (paymentRequest.status().equals(Status.OMITTED)) {
            payment.setSum(0);
            payment.setStatus(Status.OMITTED);
            payment.setUser(user);
            payment.setDate(LocalDate.now());
            user.getPayments().add(payment);
            userRepository.save(user);
            return SimpleResponse.builder().message("Успешно добавлено").httpStatus(HttpStatus.OK).build();
        }
        if (paymentRequest.status().equals(Status.WAITING)) {
            payment.setSum(0);
            payment.setStatus(Status.WAITING);
            payment.setUser(user);
            payment.setDate(LocalDate.now());
            user.getPayments().add(payment);
            userRepository.save(user);
            return SimpleResponse.builder().message("Успешно добавлено").httpStatus(HttpStatus.OK).build();
        }
        payment.setSum(paymentRequest.sum());
        payment.setStatus(paymentRequest.status());
        payment.setUser(user);
        payment.setDate(LocalDate.now());
        paymentRepo.save(payment);
        user.getPayments().add(payment);
        user.setPaidDebt(user.getPaidDebt() + paymentRequest.sum());
        userRepository.save(user);
        if (user.getPrincipalDebt() <= user.getPaidDebt()) {
            user.setUserStatus(UserStatus.FINISHED);
            userRepository.save(user);
        }
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseThrow(() -> new NotFoundException("Общая сумма не найдена"));
        totalSum.setTotalSum(totalSum.getTotalSum() + paymentRequest.sum());
        totalSumRepo.save(totalSum);
        return SimpleResponse.builder().message("Успешно добавлено").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse addSumToUser(SumRequest sumRequest) {
        User user = userRepository.getUserByEmail(sumRequest.email())
                .orElseThrow(() -> new NotFoundException("Пользователь с электронной почтой " + sumRequest.email() + " не найден"));
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L)
                .orElseGet(() -> {
                    TotalSum newTotalSum = new TotalSum();
                    newTotalSum.setId(5L);
                    newTotalSum.setTotalSum(0);
                    newTotalSum.setEmployees((double) 0);
                    newTotalSum.setInsurance((double) 0);
                    newTotalSum.setProgram((double) 0);
                    totalSumRepo.save(newTotalSum);
                    return newTotalSum;
                });
        if (!user.getUserStatus().equals(UserStatus.MIR)) {
            throw new BadCredentialException("Вы не можете погасить долг здесь! Пожалуйста, перейдите в личный кабинет получателя.");
        }
        user.setTotalSum(user.getTotalSum() + sumRequest.sum());
        userRepository.save(user);
        totalSum.setTotalSum(totalSum.getTotalSum() + sumRequest.sum());
        totalSumRepo.save(totalSum);
        return SimpleResponse.builder()
                .message("Успешно добавлено")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public SimpleResponse refundAllSumOfUser(SumRequest sumRequest) {
        User user = userRepository.getUserByEmail(sumRequest.email()).orElseThrow(() -> new NotFoundException("Пользователь с электронной почтой " + sumRequest.email() + " не найден"));
        user.setTotalSum(user.getTotalSum() - sumRequest.sum());
        if (user.getTotalSum() <= 0) {
            user.setUserStatus(UserStatus.SUBMITTED);
        }
        userRepository.save(user);
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseThrow(() -> new NotFoundException("Общая сумма не найдена"));
        totalSum.setTotalSum(totalSum.getTotalSum() - sumRequest.sum());

        totalSumRepo.save(totalSum);
        return SimpleResponse.builder().message("Успешно возвращена сумма пользователю").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public MirUsersResponse getUserWorldById(Long id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден"));
        return MirUsersResponse.builder()
                .id(user.getId())
                .photoUrl(user.getPhotoUrl())
                .userName(user.getUsername())
                .userTotalSum(user.getTotalSum())
                .userGoal(user.getGoal()).build();
    }

    @Override
    public SimpleResponse giveDebtToUser(Long id, DebtRequest debtRequest) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден"));
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseThrow(() -> new NotFoundException("Общая сумма не найдена"));
        if (totalSum.getTotalSum() < debtRequest.debtSum()) {
            throw new BadRequestException("Сумма MIR слишком мала");
        }
        user.setPrincipalDebt(user.getPrincipalDebt() + debtRequest.debtSum());
        user.setUserStatus(UserStatus.RECEIVED);
        int total = user.getTotalSum();
        userRepository.save(user);
        int debt = debtRequest.debtSum();
        debt = (int) (debt / (1 + (double) 6 / 100));
        totalSum.setTotalSum(totalSum.getTotalSum() - (debt + total));
        totalSum.setEmployees(totalSum.getEmployees() + (debt * 0.03));
        totalSum.setInsurance(totalSum.getInsurance() + (debt * 0.02));
        totalSum.setProgram(totalSum.getProgram() + (debt * 0.01));
        totalSumRepo.save(totalSum);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Основной долг " + user.getUsername() + " составляет " + user.getPrincipalDebt() + " сома")
                .build();
    }

    @Override
    public AdminResponse updatePercent(PercentType percent, Double sum) {
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L)
                .orElseThrow(() -> new NotFoundException("Общая сумма не найдена"));
        if (percent == PercentType.EMPLOYEES) {
            checkSumPercent(sum, totalSum.getEmployees(), "Сумма не должна быть больше чем преждний");
            totalSum.setEmployees(sum);
        }
        if (percent == PercentType.INSURANCE) {
            checkSumPercent(sum, totalSum.getProgram(), "Сумма не должна быть больше чем преждний");
            totalSum.setInsurance(sum);
        }
        if (percent == PercentType.PROGRAM) {
            checkSumPercent(sum, totalSum.getProgram(), "Сумма не должна быть больше чем преждний");
            totalSum.setProgram(sum);
        }
        totalSumRepo.save(totalSum);
        return userService.getPercent();
    }

    private void checkSumPercent(Double newSum, Double oldSum, String message) {
        if (newSum > oldSum)
            throw new BadRequestException(message);
    }
}
