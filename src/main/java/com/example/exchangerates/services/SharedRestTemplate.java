package com.example.exchangerates.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SharedRestTemplate {

    private final RestTemplate restTemplate;

    @Autowired
    public SharedRestTemplate() {
        this.restTemplate = new RestTemplate();
    }

    public <V> V restGet(String url, Class<V> responseType) {
        return rest(url, HttpMethod.GET, responseType, new HashMap<>());
    }

    private <V> V rest(String url, HttpMethod httpMethod, Class<V> responseType, Map<String, String> params) {
    	ResponseEntity<V> responseEntity = restTemplate.exchange(url, httpMethod, null, responseType, params);
    	return responseEntity.getBody();
    }

}