
package com.water.distribution.service;

import com.water.distribution.entity.WaterSupplyRequest;
import com.water.distribution.repository.WaterSupplyRequestRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WaterSupplyRequestService {
    private final WaterSupplyRequestRepository requestRepository;

    public WaterSupplyRequestService(
            WaterSupplyRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public WaterSupplyRequest addRequest(WaterSupplyRequest request) {
        return requestRepository.save(request);
    }

    public List<WaterSupplyRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public Optional<WaterSupplyRequest> getRequestById(Long id) {
        return requestRepository.findById(id);
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
        request.setSupplyZone(updatedRequest.getSupplyZone());

        return requestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request not found");
        }
        requestRepository.deleteById(id);
    }
    public List<WaterSupplyRequest> searchRequests(
            String keyword, String status, String priority, Long zoneId) {
        return requestRepository.findAll().stream()
            .filter(r -> isBlank(keyword)
                || containsIgnoreCase(r.getRequesterName(), keyword)
                || containsIgnoreCase(r.getAddress(), keyword)
                || containsIgnoreCase(r.getPurpose(), keyword))
            .filter(r -> isBlank(status)
                || status.equalsIgnoreCase(r.getStatus()))
            .filter(r -> isBlank(priority)
                || priority.equalsIgnoreCase(r.getPriority()))
            .filter(r -> zoneId == null
                || (r.getSupplyZone() != null
                    && zoneId.equals(r.getSupplyZone().getId())))
            .toList();
    }

    public WaterSupplyRequest updatePriority(Long id, String priority) {
        WaterSupplyRequest request = requestRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Request not found"));
        request.setPriority(priority);
        return requestRepository.save(request);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean containsIgnoreCase(String text, String search) {
        return text != null
            && text.toLowerCase().contains(search.trim().toLowerCase());
    }
}
