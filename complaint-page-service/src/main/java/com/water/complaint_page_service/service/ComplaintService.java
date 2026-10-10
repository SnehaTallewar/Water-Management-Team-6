package com.water.complaint_page_service.service;
import com.water.complaint_page_service.entity.Complaint;
import com.water.complaint_page_service.repositary.ComplaintRepository;
import org.springframework.stereotype.Service;
import com.water.complaint_page_service.dto.ComplaintResponse;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {
	 private final ComplaintRepository repository;

	    public ComplaintService(ComplaintRepository repository) {
	        this.repository = repository;
	    }

	    public Complaint addComplaint(Complaint complaint) {
	        complaint.setStatus("PENDING");
	        return repository.save(complaint);
	    }

	    public List<Complaint> getAllComplaints() {
	        return repository.findAll();
	    }

	    public Optional<Complaint> getComplaintById(Long id) {
	        return repository.findById(id);
	    }

	    public Optional<Complaint> updateStatus(Long id, String status) {
	        Optional<Complaint> result = repository.findById(id);

	        if (result.isPresent()) {
	            Complaint complaint = result.get();
	            complaint.setStatus(status);
	            repository.save(complaint);
	        }

	        return result;
	    }
	    
	    public List<ComplaintResponse> getAllComplaintResponses() {
	        List<Complaint> complaints = repository.findAll();

	        return complaints.stream()
	                .map(c -> new ComplaintResponse(
	                        c.getId(),
	                        c.getName(),
	                        c.getLocation(),
	                        c.getCategory(),
	                        c.getDescription(),
	                        c.getStatus(),
	                        c.getCreatedAt()
	                ))
	                .collect(Collectors.toList());
	    }

}
