package com.water.distribution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.water.distribution.entity.DistributionSchedule;

public interface DistributionScheduleRepository extends JpaRepository<DistributionSchedule, Long>{

}
