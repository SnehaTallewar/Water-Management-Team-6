package com.water.distribution.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Makes the main dashboard URL available to every Thymeleaf page. */
@ControllerAdvice(basePackages = "com.water.distribution.web")
public class UiModelAdvice {

    private final String dashboardUrl;

    public UiModelAdvice(
            @Value("${web.dashboard.url:http://localhost:8090}") String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    @ModelAttribute("dashboardUrl")
    public String dashboardUrl() {
        return dashboardUrl;
    }
}
