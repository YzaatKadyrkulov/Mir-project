package kg.mir.Mirproject.service.impl;

import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.WorldDto.DebtRequest;
import kg.mir.Mirproject.dto.WorldDto.DebtResponse;
import kg.mir.Mirproject.dto.WorldUserResponse;
import kg.mir.Mirproject.dto.payment.PaymentRequest;
import kg.mir.Mirproject.dto.payment.SumRequest;
import kg.mir.Mirproject.entities.Payment;
import kg.mir.Mirproject.entities.TotalSum;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.enums.Status;
import kg.mir.Mirproject.enums.UserStatus;
import kg.mir.Mirproject.exception.BadRequestException;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.PaymentRepo;
import kg.mir.Mirproject.repository.TotalSumRepo;
import kg.mir.Mirproject.repository.UserRepository;
import kg.mir.Mirproject.service.PaymentService;
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
    @Override
    public SimpleResponse payDebtsOfUser(Long id, PaymentRequest paymentRequest) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User by id " + id + " not found"));
        Payment payment = new Payment();
        if (paymentRequest.status().equals(Status.OMITTED)) {
            payment.setSum(0);
            payment.setStatus(Status.OMITTED);
            payment.setUser(user);
            payment.setDate(LocalDate.now());
            user.getPayments().add(payment);
            userRepository.save(user);
            paymentRepo.save(payment);
            return SimpleResponse.builder().message("successfully added").httpStatus(HttpStatus.OK).build();
        }
        if (paymentRequest.status().equals(Status.WAITING)) {
            payment.setSum(0);
            payment.setStatus(Status.WAITING);
            payment.setUser(user);
            payment.setDate(LocalDate.now());
            user.getPayments().add(payment);
            userRepository.save(user);
            paymentRepo.save(payment);
            return SimpleResponse.builder().message("successfully added").httpStatus(HttpStatus.OK).build();
        }
        payment.setSum(paymentRequest.sum());
        payment.setStatus(paymentRequest.status());
        payment.setUser(user);
        payment.setDate(LocalDate.now());
        user.getPayments().add(payment);
        user.setPaidDebt(user.getPaidDebt() + paymentRequest.sum());
        if (user.getPrincipalDebt() <= user.getPaidDebt() ){
            user.setUserStatus(UserStatus.FINISHED);
        }
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseThrow(()-> new NotFoundException("Total sum not found"));
        totalSum.setTotalSum(totalSum.getTotalSum() + paymentRequest.sum());
        totalSumRepo.save(totalSum);
        userRepository.save(user);
        paymentRepo.save(payment);
        return SimpleResponse.builder().message("successfully added").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse addSumToUser(SumRequest sumRequest) {
        User user = userRepository.getUserByEmail(sumRequest.email()).orElseThrow(() -> new NotFoundException("User by email " + sumRequest.email() + " not found"));
        user.setTotalSum(user.getTotalSum()+sumRequest.sum());
        userRepository.save(user);
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseThrow(()-> new NotFoundException("Total sum not found"));
        totalSum.setTotalSum(totalSum.getTotalSum() + sumRequest.sum());
        totalSumRepo.save(totalSum);
        return SimpleResponse.builder().message("successfully added").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public SimpleResponse refundAllSumOfUser(SumRequest sumRequest) {
        User user = userRepository.getUserByEmail(sumRequest.email()).orElseThrow(() -> new NotFoundException("User by email " + sumRequest.email() + " not found"));
        user.setTotalSum(user.getTotalSum() - sumRequest.sum());
        userRepository.save(user);
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseThrow(()-> new NotFoundException("Total sum not found"));
        totalSum.setTotalSum(totalSum.getTotalSum() - sumRequest.sum());
        totalSumRepo.save(totalSum);
        return SimpleResponse.builder().message("successfully refund user sum").httpStatus(HttpStatus.OK).build();
    }

    @Override
    public WorldUserResponse getUserWorldById(Long id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User by id " + id + " not found"));
       return WorldUserResponse.builder()
                .photoUrl(user.getPhotoUrl())
                .userName(user.getUsername())
                .userTotalSum(user.getTotalSum())
                .userGoal(user.getGoal()).build();

    }

    @Override
    public DebtResponse giveDebtToUser(Long id, DebtRequest debtRequest) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User by id " + id + " not found"));
        TotalSum totalSum = totalSumRepo.getTotalSumById(5L).orElseThrow(()-> new NotFoundException("Total sum not found"));
        if(totalSum.getTotalSum() < debtRequest.debtSum()){
            throw  new BadRequestException("Debt sum is too low");
        }
        totalSum.setTotalSum(totalSum.getTotalSum() - (debtRequest.debtSum() - user.getTotalSum()));
        totalSumRepo.save(totalSum);
        int debt = (debtRequest.debtSum() - user.getTotalSum());
        int sixPercent = (debt * 6) / 100;
        user.setPrincipalDebt(user.getPrincipalDebt()+(debt + sixPercent));
        user.setUserStatus(UserStatus.RECEIVED);
        userRepository.save(user);
        return DebtResponse.builder()
                .debtSum(debt + " " + "(+" + sixPercent + ") сом")
                .employees(((debt * 3) / 100) + " сом")
                .insurance(((debt * 2) / 100) + " сом")
                .program(((debt) / 100) + " сом")
                .build();

    }
}
