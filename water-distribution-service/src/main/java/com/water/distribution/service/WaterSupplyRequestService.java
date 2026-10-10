package com.water.distribution.service;

import com.water.distribution.entity.WaterSupplyRequest;
import com.water.distribution.repository.WaterSupplyRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Locale;

@Service
public class WaterSupplyRequestService {

    private final WaterSupplyRequestRepository requestRepository;

    public WaterSupplyRequestService(
            WaterSupplyRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public WaterSupplyRequest addRequest(WaterSupplyRequest request) {
        if (request.getStatus() == null) {
            request.setStatus("PENDING");
        }

        if (request.getPriority() == null) {
            request.setPriority("MEDIUM");
        }

        if (request.getRequestDate() == null) {
            request.setRequestDate(LocalDateTime.now());
        }

        return requestRepository.save(request);
    }

    public List<WaterSupplyRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public Optional<WaterSupplyRequest> getRequestById(Long id) {
        return requestRepository.findById(id);
    }
    public List<WaterSupplyRequest> searchRequests(
            String keyword, String status,
            String priority, Long zoneId) {

        return requestRepository.findAll().stream()
            .filter(r -> keyword == null || keyword.isBlank()
                || contains(r.getRequesterName(), keyword)
                || contains(r.getAddress(), keyword)
                || contains(r.getPurpose(), keyword))
            .filter(r -> status == null || status.isBlank()
                || status.equalsIgnoreCase(r.getStatus()))
            .filter(r -> priority == null || priority.isBlank()
                || priority.equalsIgnoreCase(r.getPriority()))
            .filter(r -> zoneId == null
                || (r.getSupplyZone() != null
                && zoneId.equals(r.getSupplyZone().getId())))
            .toList();
    }

    private boolean contains(String value, String keyword) {
        return value != null
            && value.toLowerCase(Locale.ROOT)
                .contains(keyword.toLowerCase(Locale.ROOT));
    }

    public WaterSupplyRequest updateRequest(
            Long id, WaterSupplyRequest updatedRequest) {

        WaterSupplyRequest request = requestRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Request not found"));

        request.setRequesterName(updatedRequest.getRequesterName());
        request.setAddress(updatedRequest.getAddress());
        request.setPurpose(updatedRequest.getPurpose());
        request.setRequestedLitres(updatedRequest.getRequestedLitres());
        request.setStatus(updatedRequest.getStatus());
        request.setPriority(updatedRequest.getPriority());
        request.setSupplyZone(updatedRequest.getSupplyZone());

        return requestRepository.save(request);
    }

    public WaterSupplyRequest updatePriority(
            Long id, String priority) {

        if (priority == null ||
            !(priority.equalsIgnoreCase("HIGH")
            || priority.equalsIgnoreCase("MEDIUM")
            || priority.equalsIgnoreCase("LOW"))) {
            throw new IllegalArgumentException(
                "Priority must be HIGH, MEDIUM, or LOW");
        }

        WaterSupplyRequest request = requestRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Request not found"));

        request.setPriority(priority.toUpperCase(Locale.ROOT));
        return requestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request not found");
        }

        requestRepository.deleteById(id);
    }
}

   
