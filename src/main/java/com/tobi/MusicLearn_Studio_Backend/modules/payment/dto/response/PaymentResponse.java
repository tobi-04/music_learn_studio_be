package com.tobi.MusicLearn_Studio_Backend.modules.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private String id;
    private String userId;
    private String courseId;
    private Double amount;
    private String currency;
    private String status;
    private String qrCodeUrl;
    private String transactionId;
    private String paymentDate;
    private String description;
}
