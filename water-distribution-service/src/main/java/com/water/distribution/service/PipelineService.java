
package com.water.distribution.service;

import com.water.distribution.entity.Pipeline;
import com.water.distribution.repository.PipelineRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PipelineService {
    private final PipelineRepository pipelineRepository;

    public PipelineService(PipelineRepository pipelineRepository) {
        this.pipelineRepository = pipelineRepository;
    }

    public Pipeline addPipeline(Pipeline pipeline) {
        return pipelineRepository.save(pipeline);
    }

    public List<Pipeline> getAllPipelines() {
        return pipelineRepository.findAll();
    }

    public Optional<Pipeline> getPipelineById(Long id) {
        return pipelineRepository.findById(id);
    }

    public Pipeline updatePipeline(Long id, Pipeline updatedPipeline) {
        Pipeline pipeline = pipelineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pipeline not found"));

        pipeline.setName(updatedPipeline.getName());
        pipeline.setLengthKm(updatedPipeline.getLengthKm());
        pipeline.setMaterial(updatedPipeline.getMaterial());
        pipeline.setStatus(updatedPipeline.getStatus());
        pipeline.setWaterTank(updatedPipeline.getWaterTank());
        pipeline.setSupplyZone(updatedPipeline.getSupplyZone());

        return pipelineRepository.save(pipeline);
    }

    public void deletePipeline(Long id) {
        if (!pipelineRepository.existsById(id)) {
            throw new RuntimeException("Pipeline not found");
        }
        pipelineRepository.deleteById(id);
    }
}