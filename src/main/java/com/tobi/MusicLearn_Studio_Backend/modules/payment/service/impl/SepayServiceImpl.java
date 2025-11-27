package com.tobi.MusicLearn_Studio_Backend.modules.payment.service.impl;

import com.tobi.MusicLearn_Studio_Backend.config.SepayConfig;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.dto.webhook.SepayWebhookPayload;
import com.tobi.MusicLearn_Studio_Backend.modules.payment.service.SepayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class SepayServiceImpl implements SepayService {

    private final SepayConfig sepayConfig;

    @Override
    public String generateQrCodeUrl(Double amount, String description) {
        if (!sepayConfig.isConfigured()) {
            log.error("SePay is not configured. Please set environment variables.");
            throw new IllegalStateException("SePay configuration is incomplete");
        }

        // Format:
        // https://qr.sepay.vn/img?acc=ACCOUNT&bank=BANK_CODE&amount=AMOUNT&des=SYSTEM_CODE+PaymentID
        String qrUrl = String.format(
                "https://qr.sepay.vn/img?acc=%s&bank=%s&amount=%d&des=%s%s",
                sepayConfig.getBankAccount(),
                sepayConfig.getBankCode(),
                amount.longValue(),
                sepayConfig.getSystemCode(),
                description);

        log.info("Generated QR code URL for amount: {} VND", amount);
        return qrUrl;
    }

    @Override
    public boolean validateWebhook(SepayWebhookPayload payload) {
        if (payload == null) {
            log.warn("Webhook payload is null");
            return false;
        }

        // Validate account number matches
        if (!sepayConfig.getBankAccount().equals(payload.getAccountNumber())) {
            log.warn("Webhook account number mismatch. Expected: {}, Got: {}",
                    sepayConfig.getBankAccount(), payload.getAccountNumber());
            return false;
        }

        // Validate transfer type is 'in'
        if (!"in".equalsIgnoreCase(payload.getTransferType())) {
            log.warn("Webhook transfer type is not 'in': {}", payload.getTransferType());
            return false;
        }

        // Validate amount is positive
        if (payload.getTransferAmount() == null || payload.getTransferAmount() <= 0) {
            log.warn("Invalid transfer amount: {}", payload.getTransferAmount());
            return false;
        }

        log.info("Webhook validation successful for transaction: {}", payload.getId());
        return true;
    }

    @Override
    public String extractPaymentIdFromContent(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }

        // Extract payment ID with system code prefix: MUSICLEARNPAY_xxxxx or
        // MUSICLEARNPAY17xxxxx (bank may remove underscores)
        String systemCode = sepayConfig.getSystemCode();

        // Try pattern with underscores first: MUSICLEARNPAY_timestamp_userId
        Pattern patternWithUnderscore = Pattern.compile(systemCode + "(PAY_[a-zA-Z0-9_]+)");
        Matcher matcher = patternWithUnderscore.matcher(content);

        if (matcher.find()) {
            String paymentId = matcher.group(1);
            log.debug("Extracted payment ID from content (with underscores): {}", paymentId);
            return paymentId;
        }

        // Try pattern without underscores: MUSICLEARNPAY17xxxxx
        Pattern patternWithoutUnderscore = Pattern.compile(systemCode + "(PAY[a-zA-Z0-9]+)");
        matcher = patternWithoutUnderscore.matcher(content);

        if (matcher.find()) {
            String rawId = matcher.group(1); // e.g., "PAY17642149885766927bf"
            // Try to reconstruct original format by finding pattern: PAY + timestamp +
            // userId
            // Format: PAY17642149885 (timestamp 13 digits) + remaining (userId)
            if (rawId.length() > 16) { // PAY(3) + timestamp(13) = 16
                String reconstructed = "PAY_" + rawId.substring(3, 16) + "_" + rawId.substring(16);
                log.debug("Extracted and reconstructed payment ID from content (without underscores): {} -> {}", rawId,
                        reconstructed);
                return reconstructed;
            }

            log.debug("Extracted payment ID from content (without underscores, no reconstruction): {}", rawId);
            return rawId;
        }

        log.warn("Could not extract payment ID from content: {}", content);
        return null;
    }
}
