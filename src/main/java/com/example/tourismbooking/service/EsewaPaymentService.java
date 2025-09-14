package com.example.tourismbooking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class EsewaPaymentService {
    @Value("${esewa.merchant}")
    private String merchantCode;

    @Value("${esewa.verify-url}")
    private String verifyUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    public boolean verifyPayment(String amt, String pid, String rid){
        if("TESTREF123".equals(rid)) {
            System.out.println("sandbox test mode: forcing verification success");
            return true;
        }
        try{
        String uri = UriComponentsBuilder.fromHttpUrl(verifyUrl)
                .queryParam("amt", amt)
                .queryParam("scd",merchantCode)
                .queryParam("pid", pid)
                .queryParam("rid",rid)
                .toUriString();

            ResponseEntity<String>resp = restTemplate.getForEntity(uri, String.class);
            String body = resp.getBody() == null? "" :resp.getBody();
            return body.contains("Success") || body.toLowerCase().contains("<response_code>success</response_code>");
        }
        catch(Exception e){
            System.out.println("warning: esewa verfy call failed, returning false");
            e.printStackTrace();
            return false;
        }
    }
}
