package com.jpmc.midascore.services;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveService {

    private final RestTemplate restTemplate;
    private final String incentiveUrl;

    public IncentiveService(RestTemplate restTemplate,
                           @Value("${incentives.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.incentiveUrl = baseUrl + "/incentive";
    }

    public float fetchIncentiveAmount(Transaction transaction) {
        Incentive incentive = restTemplate.postForObject(incentiveUrl, transaction, Incentive.class);

        if (incentive == null) return 0.0f;
        if (incentive.getAmount() <= 0.0f) return 0.0f;
        return incentive.getAmount();
    }
}
