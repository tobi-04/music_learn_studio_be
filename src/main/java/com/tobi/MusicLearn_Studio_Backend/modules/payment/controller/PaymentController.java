package com.tobi.MusicLearn_Studio_Backend.modules.payment.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.BadRequestException;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.Course;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.repository.CourseRepository;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.dto.request.CreatePaymentRequest;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.dto.response.PaymentResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.entity.Payment;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.service.PaymentService;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.service.SepayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/student/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment", description = "Payment APIs for students")
public class PaymentController {

    private final PaymentService paymentService;
    private final SepayService sepayService;
    private final CourseRepository courseRepository;

    @PostMapping("/create")
    @Operation(summary = "Create payment and get QR code for course purchase")
    public ResponseEntity<BaseResponse<PaymentResponse>> createPayment(
            @RequestBody CreatePaymentRequest request,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Creating payment for user {} and course {}", userId, request.getCourseId());

        // Validate course exists
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Validate course has a price
        if (course.getPrice() == null || course.getPrice() <= 0) {
            throw new BadRequestException("This is a free course. No payment required.");
        }

        // Check if user already purchased this course
        if (paymentService.hasUserPurchasedCourse(userId, request.getCourseId())) {
            throw new BadRequestException("You have already purchased this course");
        }

        // Check if there's already a pending payment for this user+course
        Payment existingPendingPayment = paymentService.findPendingPayment(userId, request.getCourseId());
        if (existingPendingPayment != null) {
            log.info("Found existing pending payment {} for user {} and course {}",
                    existingPendingPayment.getId(), userId, request.getCourseId());

            // Return existing payment with QR code
            PaymentResponse response = mapToResponse(existingPendingPayment);
            return ResponseEntity.ok(BaseResponse.<PaymentResponse>builder()
                    .success(true)
                    .message("Existing pending payment found")
                    .data(response)
                    .build());
        }

        // Create payment record
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setCourseId(request.getCourseId());
        payment.setAmount(course.getPrice());
        payment.setCurrency("VND");
        payment.setPaymentMethod("sepay");
        payment.setStatus("pending");

        // Generate unique transaction ID
        String transactionId = "PAY_" + System.currentTimeMillis() + "_"
                + userId.substring(0, Math.min(6, userId.length()));
        payment.setTransactionId(transactionId);

        // Generate QR code URL with transactionId (service will add system code prefix)
        String qrCodeUrl = sepayService.generateQrCodeUrl(course.getPrice(), transactionId);
        payment.setQrCodeUrl(qrCodeUrl);
        payment.setDescription("Payment for course: " + course.getTitle());

        Payment savedPayment = paymentService.createPayment(payment);

        // Convert to response
        PaymentResponse response = mapToResponse(savedPayment);

        log.info("Payment created successfully with ID: {}", savedPayment.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<PaymentResponse>builder()
                        .success(true)
                        .message("Payment created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping("/{paymentId}/status")
    @Operation(summary = "Check payment status (for polling)")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getPaymentStatus(
            @PathVariable String paymentId,
            @RequestHeader("X-User-Id") String userId) {

        Payment payment = paymentService.getPaymentById(paymentId);

        // Verify payment belongs to user
        if (!payment.getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to payment");
        }

        Map<String, Object> statusData = new HashMap<>();
        statusData.put("paymentId", payment.getId());
        statusData.put("status", payment.getStatus());
        statusData.put("amount", payment.getAmount());
        statusData.put("courseId", payment.getCourseId());

        return ResponseEntity.ok(BaseResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Payment status retrieved")
                .data(statusData)
                .build());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .userId(payment.getUserId())
                .courseId(payment.getCourseId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .qrCodeUrl(payment.getQrCodeUrl())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : null)
                .description(payment.getDescription())
                .build();
    }
}
