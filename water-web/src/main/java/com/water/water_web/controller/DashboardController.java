
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
        

        // Infrastructure API returns an object containing three lists.
        Map<String, Object> infrastructureData =
                fetchMap(infrastructureUrl, "/api/infrastructure");

        List<Map<String, Object>> infrastructurePipelines =
                getRecordList(infrastructureData, "pipelines");

        List<Map<String, Object>> infrastructurePumps =
                getRecordList(infrastructureData, "pumps");

        List<Map<String, Object>> infrastructureMaintenance =
                getRecordList(infrastructureData, "maintenance");
        model.addAttribute("infrastructurePumpColumns",
                getColumns(infrastructurePumps));

        model.addAttribute("infrastructurePipelineColumns",
                getColumns(infrastructurePipelines));

        model.addAttribute("infrastructureMaintenanceColumns",
                getColumns(infrastructureMaintenance));

        List<Map<String, Object>> complaints =
                fetchList(complaintUrl, "/api/complaints");
     // Dashboard data
        model.addAttribute("reservoirs", reservoirs);
        model.addAttribute("distributions", distributions);

        model.addAttribute("qualityRecords", qualityRecords);

        model.addAttribute("qualityRecords", qualityRecords);


model.addAttribute("qualityRecords", qualityRecords);

List<String> qualityColumns = getColumns(qualityRecords);

qualityColumns.removeIf(column ->
        column.equalsIgnoreCase("inspectorNotes"));

model.addAttribute("qualityColumns", qualityColumns);
System.out.println("QUALITY COLUMNS: " + qualityColumns);
        model.addAttribute("complaints", complaints);

        model.addAttribute("infrastructurePipelines", infrastructurePipelines);
        model.addAttribute("infrastructurePumps", infrastructurePumps);
        model.addAttribute("infrastructureMaintenance", infrastructureMaintenance);

        // Service status
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

        // Low-water alert count
        long lowWaterCount = reservoirs.stream()
                .filter(r -> Boolean.TRUE.equals(r.get("lowWater")))
                .count();

        model.addAttribute("lowWaterCount", lowWaterCount);

        // Dynamic columns for list-based tables
        model.addAttribute("distributionColumns",
                getColumns(distributions));

        model.addAttribute("qualityColumns",
                getColumns(qualityRecords));

        model.addAttribute("complaintColumns",
                getColumns(complaints));

        return "dashboard";
    }

    // Fetch a JSON array represented as a List.
    private List<Map<String, Object>> fetchList(
            String baseUrl, String endpoint) {

        try {
            List<Map<String, Object>> result = restClient.get()
                    .uri(baseUrl + endpoint)
                    .retrieve()
                    .body(new ParameterizedTypeReference<
                            List<Map<String, Object>>>() {});

            return result == null ? new ArrayList<>() : result;

        } catch (Exception e) {
            System.err.println(
                    "Failed to call " + baseUrl + endpoint
                    + ": " + e.getMessage());

            return new ArrayList<>();
        }
    }

    // Fetch a JSON object containing multiple lists.
    private Map<String, Object> fetchMap(
            String baseUrl, String endpoint) {

        try {
            Map<String, Object> result = restClient.get()
                    .uri(baseUrl + endpoint)
                    .retrieve()
                    .body(new ParameterizedTypeReference<
                            Map<String, Object>>() {});

            return result == null ? new LinkedHashMap<>() : result;

        } catch (Exception e) {
            System.err.println(
                    "Failed to call " + baseUrl + endpoint
                    + ": " + e.getMessage());

            return new LinkedHashMap<>();
        }
    }

    // Safely extract a list from the Infrastructure response.
    private List<Map<String, Object>> getRecordList(
            Map<String, Object> data, String key) {

        Object value = data.get(key);

        if (!(value instanceof List<?> list)) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> records = new ArrayList<>();

        for (Object item : list) {
            if (item instanceof Map<?, ?> map) {
                Map<String, Object> record = new LinkedHashMap<>();

                map.forEach((k, v) -> {
                    if (k instanceof String) {
                        record.put((String) k, v);
                    }
                });

                records.add(record);
            }
        }

        return records;
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
                    + ": " + e.getMessage());

            return false;
        }
    }

    private List<String> getColumns(
            List<Map<String, Object>> records) {

        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                records.get(0).keySet());
    }
}
