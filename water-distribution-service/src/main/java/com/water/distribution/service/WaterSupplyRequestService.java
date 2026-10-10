
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
}
