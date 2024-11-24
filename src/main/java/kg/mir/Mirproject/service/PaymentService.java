package kg.mir.Mirproject.service;

import kg.mir.Mirproject.dto.AdminResponse;
import kg.mir.Mirproject.dto.SimpleResponse;
import kg.mir.Mirproject.dto.payment.DebtRequest;
import kg.mir.Mirproject.dto.MirUsersResponse;
import kg.mir.Mirproject.dto.payment.PaymentRequest;
import kg.mir.Mirproject.dto.payment.SumRequest;
import kg.mir.Mirproject.enums.PercentType;

public interface PaymentService {
    SimpleResponse payDebtsOfUser(Long userId, PaymentRequest paymentRequest);

    SimpleResponse addSumToUser(SumRequest sumRequest);

    SimpleResponse refundAllSumOfUser(SumRequest sumRequest);

    MirUsersResponse getUserWorldById(Long userId);

    SimpleResponse giveDebtToUser(Long id, DebtRequest debtRequest);

    AdminResponse updatePercent(PercentType percent, Double sum);
}

