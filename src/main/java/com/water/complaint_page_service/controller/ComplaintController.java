package com.water.complaint_page_service.controller;
import com.water.complaint_page_service.entity.Complaint;
import com.water.complaint_page_service.service.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.water.complaint_page_service.dto.ComplaintResponse;
@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")

public class ComplaintController {

	private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Complaint> addComplaint(
            @RequestBody Complaint complaint) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addComplaint(complaint));
    }

    @GetMapping
    public List<ComplaintResponse> getAllComplaints() {
        return service.getAllComplaintResponses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(
            @PathVariable Long id) {

        Optional<Complaint> complaint =
                service.getComplaintById(id);

        if (complaint.isPresent()) {
            return ResponseEntity.ok(complaint.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");

        if (status == null ||
                !List.of("PENDING", "IN_PROGRESS", "RESOLVED")
                        .contains(status)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid status"));
        }

        Optional<Complaint> complaint =
                service.updateStatus(id, status);

        if (complaint.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(complaint.get());
    }
}
