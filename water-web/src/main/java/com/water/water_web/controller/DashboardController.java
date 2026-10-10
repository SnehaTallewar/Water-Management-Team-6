
package com.water.water_web.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

@Controller
public class DashboardController {

    private final RestClient restClient;

    private final String resourceUrl;
    private final String distributionUrl;
    private final String qualityUrl;
    private final String infrastructureUrl;
    private final String complaintUrl;

    public DashboardController(
            RestClient.Builder builder,
            @Value("${resource.service.url}") String resourceUrl,
            @Value("${distribution.service.url}") String distributionUrl,
            @Value("${quality.service.url}") String qualityUrl,
            @Value("${infrastructure.service.url}") String infrastructureUrl,
            @Value("${complaint.service.url}") String complaintUrl) {

        this.restClient = builder.build();

        this.resourceUrl = resourceUrl;
        this.distributionUrl = distributionUrl;
        this.qualityUrl = qualityUrl;
        this.infrastructureUrl = infrastructureUrl;
        this.complaintUrl = complaintUrl;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        
        List<Map<String, Object>> reservoirs =
                fetchList(resourceUrl, "/api/reservoirs");

        List<Map<String, Object>> distributions =
                fetchList(distributionUrl, "/api/distributions");

        List<Map<String, Object>> qualityRecords =
                fetchList(qualityUrl, "/api/quality");
        System.out.println("Quality URL: " + qualityUrl);
        System.out.println("Quality Records: " + qualityRecords);
        System.out.println("Quality Columns: " + getColumns(qualityRecords));

        List<Map<String, Object>> infrastructureRecords =
                fetchList(infrastructureUrl, "/api/infrastructure");

        List<Map<String, Object>> complaints =
                fetchList(complaintUrl, "/api/complaints");

        
        model.addAttribute("reservoirs", reservoirs);
        model.addAttribute("distributions", distributions);
        model.addAttribute("qualityRecords", qualityRecords);
        model.addAttribute("infrastructureRecords", infrastructureRecords);
        model.addAttribute("complaints", complaints);

        
        model.addAttribute("resourceOnline",
                isOnline(resourceUrl, "/api/reservoirs"));
        model.addAttribute("distributionOnline",
                isOnline(distributionUrl, "/api/distributions"));
        model.addAttribute("qualityOnline",
                isOnline(qualityUrl, "/api/quality"));
        model.addAttribute("infrastructureOnline",
                isOnline(infrastructureUrl, "/api/infrastructure"));
        model.addAttribute("complaintOnline",
                isOnline(complaintUrl, "/api/complaints"));

        
        long lowWaterCount = reservoirs.stream()
                .filter(r -> Boolean.TRUE.equals(r.get("lowWater")))
                .count();

        model.addAttribute("lowWaterCount", lowWaterCount);

        
        model.addAttribute("distributionColumns",
                getColumns(distributions));
        model.addAttribute("qualityColumns",
                getColumns(qualityRecords));
        model.addAttribute("infrastructureColumns",
                getColumns(infrastructureRecords));
        model.addAttribute("complaintColumns",
                getColumns(complaints));

        return "dashboard";
    }

    private List<Map<String, Object>> fetchList(
            String baseUrl, String endpoint) {

        try {
            List<Map<String, Object>> result = restClient.get()
                    .uri(baseUrl + endpoint)
                    .retrieve()
                    .body(new ParameterizedTypeReference<
                            List<Map<String, Object>>>() {});

            return result == null ? new ArrayList<>() : result;

        }  catch (Exception e) {
            System.err.println(
                    "Failed to call " + baseUrl + endpoint
                    + ": " + e.getMessage()
                );
                return new ArrayList<>();
            }
    }

    private boolean isOnline(String baseUrl, String endpoint) {
        try {
            restClient.get()
                    .uri(baseUrl + endpoint)
                    .retrieve()
                    .toBodilessEntity();

            return true;

        } catch (Exception e) {
            System.err.println(
                    "Failed to call " + baseUrl + endpoint
                    + ": " + e.getMessage()
                );
            return false;
                
            }
    }

    private List<String> getColumns(
            List<Map<String, Object>> records) {

        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                new LinkedHashMap<>(records.get(0)).keySet());
    }
}

