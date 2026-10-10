package com.water.complaint_page_service.dto;
import java.time.LocalDateTime;

public class ComplaintResponse {
       
	 private Long id;
	    private String name;
	    private String location;
	    private String category;
	    private String description;
	    private String status;
	    private LocalDateTime createdAt;

	    public ComplaintResponse() {
	    }

	    public ComplaintResponse(Long id, String name,
	            String location, String category,
	            String description, String status,
	            LocalDateTime createdAt) {

	        this.id = id;
	        this.name = name;
	        this.location = location;
	        this.category = category;
	        this.description = description;
	        this.status = status;
	        this.createdAt = createdAt;
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getLocation() {
	        return location;
	    }

	    public String getCategory() {
	        return category;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }
}
