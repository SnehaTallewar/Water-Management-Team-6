
package com.water.water_infrastructure_service.controller;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.water.water_infrastructure_service.dto.PipelineRequestDTO;
import com.water.water_infrastructure_service.dto.PipelineResponseDTO;
import com.water.water_infrastructure_service.entity.Pipeline;
import com.water.water_infrastructure_service.repository.PipelineRepository;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/pipelines")
public class PipelineController {
    private final PipelineRepository pipelineRepository;
    public PipelineController(PipelineRepository pipelineRepository) {
        this.pipelineRepository = pipelineRepository;
    }
    private PipelineResponseDTO convertToDTO(Pipeline pipeline) {
        return new PipelineResponseDTO(
                pipeline.getId(),
                pipeline.getName(),
                pipeline.getStartLocation(),
                pipeline.getEndLocation(),
                pipeline.getLength(),
                pipeline.getStatus()
        );
    }
    private void updatePipeline(Pipeline pipeline, PipelineRequestDTO dto) {
        pipeline.setName(dto.getName());
        pipeline.setStartLocation(dto.getStartLocation());
        pipeline.setEndLocation(dto.getEndLocation());
        pipeline.setLength(dto.getLength());
        pipeline.setStatus(dto.getStatus());
    }
    @PostMapping
    public PipelineResponseDTO addPipeline(@Valid @RequestBody PipelineRequestDTO dto) {
        Pipeline pipeline = new Pipeline();
        updatePipeline(pipeline, dto);
        return convertToDTO(pipelineRepository.save(pipeline));
    }
    @GetMapping
    public List<PipelineResponseDTO> getAllPipelines() {
        return pipelineRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<PipelineResponseDTO> getPipeline(@PathVariable Long id) {
        return pipelineRepository.findById(id)
                .map(pipeline -> ResponseEntity.ok(convertToDTO(pipeline)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<PipelineResponseDTO> updatePipelineById(
            @PathVariable Long id,
            @Valid @RequestBody PipelineRequestDTO dto) {
        return pipelineRepository.findById(id).map(pipeline -> {
            updatePipeline(pipeline, dto);
            Pipeline updatedPipeline = pipelineRepository.save(pipeline);
            return ResponseEntity.ok(convertToDTO(updatedPipeline));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@PathVariable Long id) {
        if (!pipelineRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pipelineRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
