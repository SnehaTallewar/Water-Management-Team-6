package com.water.distribution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.water.distribution.entity.Pipeline;

public interface PipelineRepository extends JpaRepository<Pipeline, Long>{

}
