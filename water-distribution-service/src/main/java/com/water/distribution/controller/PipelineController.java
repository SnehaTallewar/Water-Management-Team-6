
package com.water.distribution.controller;

import com.water.distribution.entity.Pipeline;
import com.water.distribution.service.PipelineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pipelines")
public class PipelineController {
    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @PostMapping
    public Pipeline addPipeline(@RequestBody Pipeline pipeline) {
        return pipelineService.addPipeline(pipeline);
    }

    @GetMapping
    public List<Pipeline> getAllPipelines() {
        return pipelineService.getAllPipelines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pipeline> getPipelineById(@PathVariable Long id) {
        return pipelineService.getPipelineById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pipeline> updatePipeline(
            @PathVariable Long id,
            @RequestBody Pipeline pipeline) {
        try {
            return ResponseEntity.ok(
                pipelineService.updatePipeline(id, pipeline));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@PathVariable Long id) {
        try {
            pipelineService.deletePipeline(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
