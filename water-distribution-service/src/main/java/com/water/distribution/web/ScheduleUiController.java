package com.water.distribution.web;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.water.distribution.entity.DistributionSchedule;
import com.water.distribution.entity.SupplyZone;
import com.water.distribution.service.DistributionScheduleService;
import com.water.distribution.service.SupplyZoneService;

@Controller
@RequestMapping("/distribution/schedules")
public class ScheduleUiController {

    private static final List<String> STATUSES =
            List.of("DRAFT", "SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELLED");
    private static final String LIST = "redirect:/distribution/schedules";

    private final DistributionScheduleService scheduleService;
    private final SupplyZoneService zoneService;

    public ScheduleUiController(DistributionScheduleService scheduleService,
                                SupplyZoneService zoneService) {
        this.scheduleService = scheduleService;
        this.zoneService = zoneService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("active", "schedules");
        model.addAttribute("schedules", scheduleService.getAllSchedules().stream()
                .sorted(Comparator.comparing(DistributionSchedule::getDistributionDate)
                        .thenComparing(DistributionSchedule::getStartTime)
                        .reversed())
                .toList());
        return "distribution/schedules";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        DistributionSchedule schedule = new DistributionSchedule();
        schedule.setStatus("DRAFT");
        schedule.setDistributionDate(LocalDate.now());
        schedule.setAllocatedLitres(0);
        return form(model, schedule, "Add Distribution Schedule", "/distribution/schedules");
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return scheduleService.getScheduleById(id)
                .map(s -> form(model, s, "Edit Distribution Schedule #" + id,
                        "/distribution/schedules/" + id))
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Schedule not found.");
                    return LIST;
                });
    }

    @PostMapping
    public String create(
            @RequestParam(required = false) Long zoneId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate distributionDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer availableLitres,
            @RequestParam(required = false) Integer allocatedLitres,
            Model model, RedirectAttributes ra) {

        DistributionSchedule schedule = build(zoneId, distributionDate, startTime,
                endTime, status, availableLitres, allocatedLitres);
        return persist(null, schedule, model, ra);
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam(required = false) Long zoneId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate distributionDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer availableLitres,
            @RequestParam(required = false) Integer allocatedLitres,
            Model model, RedirectAttributes ra) {

        DistributionSchedule schedule = build(zoneId, distributionDate, startTime,
                endTime, status, availableLitres, allocatedLitres);
        return persist(id, schedule, model, ra);
    }

    /** Reserve more litres from a schedule (PATCH /api/schedules/{id}/allocate). */
    @PostMapping("/{id}/allocate")
    public String allocate(@PathVariable Long id,
                           @RequestParam(required = false) Integer litres,
                           RedirectAttributes ra) {
        try {
            DistributionSchedule s = scheduleService.allocateWater(id, litres);
            int remaining = s.getAvailableLitres() - s.getAllocatedLitres();
            ra.addFlashAttribute("success", litres + " litres allocated to schedule #"
                    + id + ". Remaining: " + remaining + " litres.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", "Schedule not found.");
        }
        return LIST;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            scheduleService.deleteSchedule(id);
            ra.addFlashAttribute("success", "Schedule deleted.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return LIST;
    }

    private DistributionSchedule build(Long zoneId, LocalDate date, LocalTime start,
                                       LocalTime end, String status,
                                       Integer available, Integer allocated) {
        DistributionSchedule s = new DistributionSchedule();
        if (zoneId != null) {
            zoneService.getZoneById(zoneId).ifPresent(s::setSupplyZone);
        }
        s.setDistributionDate(date);
        s.setStartTime(start);
        s.setEndTime(end);
        s.setStatus(status == null || status.isBlank() ? "DRAFT" : status);
        s.setAvailableLitres(available);
        s.setAllocatedLitres(allocated);
        return s;
    }

    private String persist(Long id, DistributionSchedule schedule,
                           Model model, RedirectAttributes ra) {
        String problem = validate(schedule);
        if (problem == null) {
            try {
                if (id == null) {
                    scheduleService.addSchedule(schedule);
                } else {
                    scheduleService.updateSchedule(id, schedule);
                }
                ra.addFlashAttribute("success",
                        "Schedule was " + (id == null ? "added." : "updated."));
                return LIST;
            } catch (IllegalArgumentException e) {
                problem = e.getMessage();
            } catch (RuntimeException e) {
                problem = "Could not save the schedule: " + e.getMessage();
            }
        }
        model.addAttribute("error", problem);
        return id == null
                ? form(model, schedule, "Add Distribution Schedule", "/distribution/schedules")
                : form(model, schedule, "Edit Distribution Schedule #" + id,
                        "/distribution/schedules/" + id);
    }

    private String validate(DistributionSchedule s) {
        if (s.getSupplyZone() == null) {
            return "Please choose a supply zone.";
        }
        if (s.getDistributionDate() == null) {
            return "Distribution date is required.";
        }
        if (s.getStartTime() == null || s.getEndTime() == null) {
            return "Start time and end time are required.";
        }
        if (!s.getEndTime().isAfter(s.getStartTime())) {
            return "End time must be after the start time.";
        }
        if (s.getAvailableLitres() == null || s.getAvailableLitres() < 0) {
            return "Available litres must be zero or greater.";
        }
        if (s.getAllocatedLitres() != null
                && (s.getAllocatedLitres() < 0
                    || s.getAllocatedLitres() > s.getAvailableLitres())) {
            return "Allocated litres must be between zero and the available litres.";
        }
        return null;
    }

    private String form(Model model, DistributionSchedule schedule,
                        String title, String action) {
        List<SupplyZone> zones = zoneService.getAllZones().stream()
                .sorted(Comparator.comparing(SupplyZone::getId)).toList();
        model.addAttribute("active", "schedules");
        model.addAttribute("schedule", schedule);
        model.addAttribute("zones", zones);
        model.addAttribute("pageTitle", title);
        model.addAttribute("formAction", action);
        model.addAttribute("statuses", UiHelper.withCurrent(STATUSES, schedule.getStatus()));
        return "distribution/schedule-form";
    }
}
