package com.water.distribution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.water.distribution.entity.WaterTank;

public interface WaterTankRepository extends JpaRepository<WaterTank, Long>{

}
