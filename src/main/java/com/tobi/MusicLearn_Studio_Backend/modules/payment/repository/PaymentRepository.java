package com.tobi.MusicLearn_Studio_Backend.modules.payment.repository;

import com.tobi.MusicLearn_Studio_Backend.modules.payment.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    List<Payment> findByUserId(String userId);

    List<Payment> findByCourseId(String courseId);

    List<Payment> findByStatus(String status);

    @Query("{ 'status': 'completed', 'paymentDate': { $gte: ?0, $lte: ?1 } }")
    List<Payment> findCompletedPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'status': 'completed' }")
    List<Payment> findAllCompletedPayments();

    Long countByStatus(String status);

    boolean existsByUserIdAndCourseIdAndStatus(String userId, String courseId, String status);
}
