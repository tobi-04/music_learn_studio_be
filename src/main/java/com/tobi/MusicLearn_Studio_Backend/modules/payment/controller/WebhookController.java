package com.tobi.MusicLearn_Studio_Backend.modules.payment.controller;

import com.tobi.MusicLearn_Studio_Backend.common.dto.BaseResponse;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.dto.webhook.SepayWebhookPayload;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.entity.Payment;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.service.PaymentService;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.service.SepayService;
import com.tobi.MusicLearn_Studio_Backend.modules.learning.service.ProgressService;
import com.tobi.MusicLearn_Studio_Backend.common.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Webhooks", description = "Webhook endpoints for payment gateways")
public class WebhookController {

    private final PaymentService paymentService;
    private final SepayService sepayService;
    private final ProgressService progressService;

    @PostMapping("/sepay")
    @Operation(summary = "SePay webhook callback (public endpoint)")
    public ResponseEntity<BaseResponse<Void>> handleSepayWebhook(@RequestBody SepayWebhookPayload payload) {

        log.info("Received SePay webhook: {}", payload);

        // Validate webhook
        if (!sepayService.validateWebhook(payload)) {
            log.warn("Invalid webhook payload received");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.<Void>builder()
                            .success(false)
                            .message("Invalid webhook payload")
                            .build());
        }

        // Extract payment ID from content
        String paymentId = sepayService.extractPaymentIdFromContent(payload.getContent());
        if (paymentId == null) {
            log.warn("Could not extract payment ID from webhook content: {}", payload.getContent());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.<Void>builder()
                            .success(false)
                            .message("Payment ID not found in webhook")
                            .build());
        }

        try {
            // Find payment by transaction ID
            Payment payment = paymentService.getPaymentByTransactionId(paymentId);

            // Check if payment is already completed
            if ("completed".equals(payment.getStatus())) {
                log.info("Payment {} already completed, skipping", paymentId);
                return ResponseEntity.ok(BaseResponse.<Void>builder()
                        .success(true)
                        .message("Payment already processed")
                        .build());
            }

            // Verify amount matches
            if (!payment.getAmount().equals(payload.getTransferAmount())) {
                log.warn("Payment amount mismatch. Expected: {}, Got: {}",
                        payment.getAmount(), payload.getTransferAmount());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(BaseResponse.<Void>builder()
                                .success(false)
                                .message("Payment amount mismatch")
                                .build());
            }

            // Update payment status to completed
            paymentService.updatePaymentStatus(payment.getId(), "completed", payload.getId());

            // Auto-enroll user in course (bypass price check since payment is completed)
            try {
                progressService.enrollCourseAfterPayment(payment.getUserId(), payment.getCourseId());
                log.info("User {} enrolled in course {} after successful payment",
                        payment.getUserId(), payment.getCourseId());
            } catch (Exception e) {
                log.error("Failed to enroll user after payment", e);
                // Payment is still marked as completed, enrollment can be retried manually
            }

            return ResponseEntity.ok(BaseResponse.<Void>builder()
                    .success(true)
                    .message("Payment processed successfully")
                    .build());

        } catch (ResourceNotFoundException e) {
            log.error("Payment not found: {}", paymentId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.<Void>builder()
                            .success(false)
                            .message("Payment not found")
                            .build());
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.<Void>builder()
                            .success(false)
                            .message("Internal server error")
                            .build());
        }
    }
}
