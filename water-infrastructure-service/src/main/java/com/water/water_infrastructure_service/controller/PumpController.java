
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
import com.water.water_infrastructure_service.dto.PumpRequestDTO;
import com.water.water_infrastructure_service.dto.PumpResponseDTO;
import com.water.water_infrastructure_service.entity.Pump;
import com.water.water_infrastructure_service.repository.PumpRepository;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/pumps")
public class PumpController {
    private final PumpRepository pumpRepository;
    public PumpController(PumpRepository pumpRepository) {
        this.pumpRepository = pumpRepository;
    }
    private PumpResponseDTO convertToDTO(Pump pump) {
        return new PumpResponseDTO(
                pump.getId(),
                pump.getName(),
                pump.getLocation(),
                pump.getStatus(),
                pump.getCapacity()
        );
    }

    private void updatePump(Pump pump, PumpRequestDTO dto) {
        pump.setName(dto.getName());
        pump.setLocation(dto.getLocation());
        pump.setStatus(dto.getStatus());
        pump.setCapacity(dto.getCapacity());
    }
    @PostMapping
    public PumpResponseDTO addPump(@Valid @RequestBody PumpRequestDTO dto) {
        Pump pump = new Pump();
        updatePump(pump, dto);
        return convertToDTO(pumpRepository.save(pump));
    }
    @GetMapping
    public List<PumpResponseDTO> getAllPumps() {
        return pumpRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<PumpResponseDTO> getPump(@PathVariable Long id) {
        return pumpRepository.findById(id)
                .map(pump -> ResponseEntity.ok(convertToDTO(pump)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<PumpResponseDTO> updatePumpById(
            @PathVariable Long id,
            @Valid @RequestBody PumpRequestDTO dto) {
        return pumpRepository.findById(id).map(pump -> {
            updatePump(pump, dto);
            Pump updatedPump = pumpRepository.save(pump);
            return ResponseEntity.ok(convertToDTO(updatedPump));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePump(@PathVariable Long id) {
        if (!pumpRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pumpRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
