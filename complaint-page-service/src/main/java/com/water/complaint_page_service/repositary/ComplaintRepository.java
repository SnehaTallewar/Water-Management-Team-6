package com.water.complaint_page_service.repositary;
import com.water.complaint_page_service.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository 
        extends JpaRepository<Complaint, Long> {
}

