package com.tobi.MusicLearn_Studio_Backend.modules.payment.service;

import com.tobi.MusicLearn_Studio_Backend.modules.payment.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    Payment createPayment(Payment payment);

    Payment getPaymentById(String id);

    List<Payment> getPaymentsByUserId(String userId);

    List<Payment> getPaymentsByCourseId(String courseId);

    Double getTotalRevenue();

    Double getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Map<String, Double> getMonthlyRevenue(int months);

    boolean hasUserPurchasedCourse(String userId, String courseId);
}
