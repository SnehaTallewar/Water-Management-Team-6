package com.water.distribution.service;

import com.water.distribution.entity.DistributionSchedule;
import com.water.distribution.repository.DistributionScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        validateQuantity(schedule.getAvailableLitres(),
                         schedule.getAllocatedLitres());

        if (schedule.getAllocatedLitres() == null) {
            schedule.setAllocatedLitres(0);
        }

        if (schedule.getStatus() == null) {
            schedule.setStatus("DRAFT");
        }

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

        validateQuantity(updatedSchedule.getAvailableLitres(),
                         updatedSchedule.getAllocatedLitres());

        schedule.setSupplyZone(updatedSchedule.getSupplyZone());
        schedule.setDistributionDate(
            updatedSchedule.getDistributionDate());
        schedule.setStartTime(updatedSchedule.getStartTime());
        schedule.setEndTime(updatedSchedule.getEndTime());
        schedule.setStatus(updatedSchedule.getStatus());
        schedule.setAvailableLitres(updatedSchedule.getAvailableLitres());
        schedule.setAllocatedLitres(
            updatedSchedule.getAllocatedLitres() == null
                ? 0 : updatedSchedule.getAllocatedLitres());

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public DistributionSchedule allocateWater(
            Long id, Integer litres) {

        if (litres == null || litres <= 0) {
            throw new IllegalArgumentException(
                "Allocation must be greater than zero");
        }

        DistributionSchedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Schedule not found"));

        int available = schedule.getAvailableLitres() == null
            ? 0 : schedule.getAvailableLitres();

        int allocated = schedule.getAllocatedLitres() == null
            ? 0 : schedule.getAllocatedLitres();

        if (litres > available - allocated) {
            throw new IllegalArgumentException(
                "Allocation exceeds the remaining available water");
        }

        schedule.setAllocatedLitres(allocated + litres);

        return scheduleRepository.save(schedule);
    }

    private void validateQuantity(Integer available, Integer allocated) {
        if (available == null || available < 0) {
            throw new IllegalArgumentException(
                "Available litres must be zero or greater");
        }

        if (allocated != null &&
            (allocated < 0 || allocated > available)) {
            throw new IllegalArgumentException(
                "Allocated litres must be between zero and available litres");
        }
    }

    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found");
        }

        scheduleRepository.deleteById(id);
    }
}

