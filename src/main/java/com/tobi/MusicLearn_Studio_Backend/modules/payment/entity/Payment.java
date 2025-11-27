package com.tobi.MusicLearn_Studio_Backend.modules.payment.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "payments")
public class Payment extends BaseEntity {

    @Indexed
    private String userId; // User who made the payment

    @Indexed
    private String courseId; // Course that was purchased

    private Double amount; // Payment amount

    private String currency; // VND, USD, etc.

    private String paymentMethod; // credit_card, bank_transfer, momo, etc.

    private String status; // pending, completed, failed, refunded

    private String transactionId; // External payment gateway transaction ID

    @Indexed
    private String sepayTransactionId; // SePay-specific transaction ID

    private String qrCodeUrl; // URL of the generated QR code for payment

    @Indexed
    private LocalDateTime paymentDate; // When payment was made

    private String description; // Optional payment description
}
