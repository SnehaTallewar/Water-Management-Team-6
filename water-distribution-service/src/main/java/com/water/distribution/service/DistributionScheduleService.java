
package com.water.distribution.service;

import com.water.distribution.entity.DistributionSchedule;
import com.water.distribution.repository.DistributionScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DistributionScheduleService {
    private final DistributionScheduleRepository scheduleRepository;

    public DistributionScheduleService(
            DistributionScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public DistributionSchedule addSchedule(
            DistributionSchedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<DistributionSchedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<DistributionSchedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public DistributionSchedule updateSchedule(
            Long id, DistributionSchedule updatedSchedule) {
        DistributionSchedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Schedule not found"));

        schedule.setSupplyZone(updatedSchedule.getSupplyZone());
        schedule.setDistributionDate(
            updatedSchedule.getDistributionDate());
        schedule.setStartTime(updatedSchedule.getStartTime());
        schedule.setEndTime(updatedSchedule.getEndTime());
        schedule.setStatus(updatedSchedule.getStatus());
        schedule.setStatus(updatedSchedule.getStatus());
        schedule.setAvailableLitres(updatedSchedule.getAvailableLitres());
        schedule.setAllocatedLitres(updatedSchedule.getAllocatedLitres());
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }
    public DistributionSchedule allocateWater(Long id, Integer litres) {
        if (litres == null || litres <= 0) {
            throw new IllegalArgumentException(
                "Litres to allocate must be greater than zero.");
        }

        DistributionSchedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Schedule not found"));

        int available = schedule.getAvailableLitres() == null
            ? 0 : schedule.getAvailableLitres();
        int allocated = schedule.getAllocatedLitres() == null
            ? 0 : schedule.getAllocatedLitres();

        if (allocated + litres > available) {
            throw new IllegalArgumentException(
                "Cannot allocate " + litres + " litres. Only "
                + (available - allocated) + " litres remaining.");
        }

        schedule.setAllocatedLitres(allocated + litres);
        return scheduleRepository.save(schedule);
    }
}
