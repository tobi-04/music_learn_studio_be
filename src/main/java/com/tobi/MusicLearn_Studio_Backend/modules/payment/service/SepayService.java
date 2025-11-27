package com.tobi.MusicLearn_Studio_Backend.modules.payment.service;

import com.tobi.MusicLearn_Studio_Backend.modules.payment.dto.webhook.SepayWebhookPayload;

public interface SepayService {

    String generateQrCodeUrl(Double amount, String description);

    boolean validateWebhook(SepayWebhookPayload payload);

    String extractPaymentIdFromContent(String content);
}
