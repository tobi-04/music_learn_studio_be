package com.tobi.MusicLearn_Studio_Backend.modules.payment.service.impl;

import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.entity.Payment;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.repository.PaymentRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(Payment payment) {
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public List<Payment> getPaymentsByUserId(String userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public List<Payment> getPaymentsByCourseId(String courseId) {
        return paymentRepository.findByCourseId(courseId);
    }

    @Override
    public Double getTotalRevenue() {
        List<Payment> completedPayments = paymentRepository.findAllCompletedPayments();
        return completedPayments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public Double getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findCompletedPaymentsBetweenDates(startDate, endDate);
        return payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public Map<String, Double> getMonthlyRevenue(int months) {
        Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        for (int i = months - 1; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.now().minusMonths(i);
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            Double revenue = getRevenueByDateRange(startOfMonth, endOfMonth);
            monthlyRevenue.put(yearMonth.format(formatter), revenue);
        }

        return monthlyRevenue;
    }

    @Override
    public boolean hasUserPurchasedCourse(String userId, String courseId) {
        return paymentRepository.existsByUserIdAndCourseIdAndStatus(userId, courseId, "completed");
    }
}
