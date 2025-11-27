package com.tobi.MusicLearn_Studio_Backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SepayConfig {

    @Value("${sepay.api-key:}")
    private String apiKey;

    @Value("${sepay.base-url:https://my.sepay.vn}")
    private String baseUrl;

    @Value("${sepay.bank-account:}")
    private String bankAccount;

    @Value("${sepay.bank-code:}")
    private String bankCode;

    @Value("${sepay.system-code:MUSICLEARN}")
    private String systemCode;

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty()
                && bankAccount != null && !bankAccount.isEmpty()
                && bankCode != null && !bankCode.isEmpty();
    }
}
