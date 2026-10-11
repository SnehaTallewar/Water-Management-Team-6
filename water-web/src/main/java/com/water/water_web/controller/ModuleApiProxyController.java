
package com.water.water_web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
public class ModuleApiProxyController {

    @Value("${resource.service.url:http://localhost:8081}")
    private String resourceUrl;

    @Value("${distribution.service.url:http://localhost:8082}")
    private String distributionUrl;

    @Value("${quality.service.url:http://localhost:8083}")
    private String qualityUrl;

    @Value("${infrastructure.service.url:http://localhost:8084}")
    private String infrastructureUrl;

    @Value("${complaint.service.url:http://localhost:8085}")
    private String complaintUrl;

    private final RestTemplate restTemplate = createRestTemplate();

    private static RestTemplate createRestTemplate() {
        org.springframework.http.client.HttpComponentsClientHttpRequestFactory factory =
            new org.springframework.http.client.HttpComponentsClientHttpRequestFactory();

        return new RestTemplate(factory);
    }

    private String baseUrl(String service) {
        return switch (service) {
            case "resources" -> resourceUrl;
            case "distribution" -> distributionUrl;
            case "quality" -> qualityUrl;
            case "infrastructure" -> infrastructureUrl;
            case "complaints" -> complaintUrl;
            default -> throw new IllegalArgumentException("Unknown service: " + service);
        };
    }

    @RequestMapping("/ui-api/{service}/**")
    public ResponseEntity<String> proxy(
            @PathVariable String service,
            HttpServletRequest request,
            @RequestBody(required = false) String body) {

        String prefix = request.getContextPath() + "/ui-api/" + service;
        String path = request.getRequestURI().substring(prefix.length());

        String query = request.getQueryString();
        String url = baseUrl(service) + path
                + (query == null ? "" : "?" + query);

        HttpHeaders headers = new HttpHeaders();
        String contentType = request.getContentType();
        if (contentType != null) {
            headers.set("Content-Type", contentType);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                UriComponentsBuilder.fromUriString(url).build(true).toUri(),
                HttpMethod.valueOf(request.getMethod()),
                entity,
                String.class
        );
    }
}
