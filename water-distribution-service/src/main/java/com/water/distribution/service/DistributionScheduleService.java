
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

        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }
}
