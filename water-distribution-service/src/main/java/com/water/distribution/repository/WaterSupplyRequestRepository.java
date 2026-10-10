package com.water.distribution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.water.distribution.entity.WaterSupplyRequest;

public interface WaterSupplyRequestRepository extends JpaRepository<WaterSupplyRequest, Long>{

}
