package com.water.distribution.service;

import com.water.distribution.entity.WaterTank;
import com.water.distribution.repository.WaterTankRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WaterTankService {
	private final WaterTankRepository waterTankRepository;
	public WaterTankService(WaterTankRepository waterTankRepository) {
        this.waterTankRepository = waterTankRepository;
    }
	
	public WaterTank addTank(WaterTank tank) {
		return waterTankRepository.save(tank);
	}
	
	public List<WaterTank> getAllTanks(){
		return waterTankRepository.findAll();
	}
	
	public Optional<WaterTank> getTankById(Long id){
		return waterTankRepository.findById(id);
	}
	
	public WaterTank updateTank(Long id,WaterTank updatedTank) {
		WaterTank tank = waterTankRepository.findById(id).orElseThrow(()-> new RuntimeException("Tank not found"));
		tank.setName(updatedTank.getName());
        tank.setLocation(updatedTank.getLocation());
        tank.setCapacityLitres(updatedTank.getCapacityLitres());
        tank.setCurrentLevelLitres(updatedTank.getCurrentLevelLitres());
        tank.setStatus(updatedTank.getStatus());

        return waterTankRepository.save(tank);
	}
	
	public void deleteTank(Long id) {
        if (!waterTankRepository.existsById(id)) {
            throw new RuntimeException("Tank not found");
        }
        waterTankRepository.deleteById(id);
    }
}
