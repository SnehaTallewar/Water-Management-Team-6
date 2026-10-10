package com.water.distribution.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.water.distribution.entity.DistributionSchedule;
import com.water.distribution.entity.Pipeline;
import com.water.distribution.entity.SupplyZone;
import com.water.distribution.entity.WaterSupplyRequest;
import com.water.distribution.entity.WaterTank;
import com.water.distribution.service.DistributionScheduleService;
import com.water.distribution.service.PipelineService;
import com.water.distribution.service.SupplyZoneService;
import com.water.distribution.service.WaterSupplyRequestService;
import com.water.distribution.service.WaterTankService;

@Controller
public class HomeUiController {

    private final SupplyZoneService zoneService;
    private final WaterTankService tankService;
    private final PipelineService pipelineService;
    private final DistributionScheduleService scheduleService;
    private final WaterSupplyRequestService requestService;
    private final UiHelper ui;

    public HomeUiController(SupplyZoneService zoneService,
                            WaterTankService tankService,
                            PipelineService pipelineService,
                            DistributionScheduleService scheduleService,
                            WaterSupplyRequestService requestService,
                            UiHelper ui) {
        this.zoneService = zoneService;
        this.tankService = tankService;
        this.pipelineService = pipelineService;
        this.scheduleService = scheduleService;
        this.requestService = requestService;
        this.ui = ui;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/distribution";
    }

    @GetMapping("/distribution")
    public String home(Model model) {
        List<SupplyZone> zones = zoneService.getAllZones();
        List<WaterTank> tanks = tankService.getAllTanks();
        List<Pipeline> pipelines = pipelineService.getAllPipelines();
        List<DistributionSchedule> schedules = scheduleService.getAllSchedules();
        List<WaterSupplyRequest> requests = requestService.getAllRequests();

        List<WaterSupplyRequest> pending = requests.stream()
                .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus()))
                .toList();
        long highPending = pending.stream()
                .filter(r -> "HIGH".equalsIgnoreCase(r.getPriority()))
                .count();

        List<WaterSupplyRequest> recentRequests = requests.stream()
                .sorted(Comparator.comparing(WaterSupplyRequest::getId).reversed())
                .limit(5)
                .toList();

        LocalDate today = LocalDate.now();
        List<DistributionSchedule> upcoming = schedules.stream()
                .filter(s -> s.getDistributionDate() != null
                        && !s.getDistributionDate().isBefore(today))
                .sorted(Comparator.comparing(DistributionSchedule::getDistributionDate)
                        .thenComparing(DistributionSchedule::getStartTime))
                .limit(5)
                .toList();

        BigDecimal totalCapacity = tanks.stream()
                .map(WaterTank::getCapacityLitres)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalStored = tanks.stream()
                .map(WaterTank::getCurrentLevelLitres)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long lowTanks = tanks.stream()
                .filter(t -> ui.percent(t.getCurrentLevelLitres(),
                        t.getCapacityLitres()) <= 25)
                .count();

        model.addAttribute("active", "home");
        model.addAttribute("zoneCount", zones.size());
        model.addAttribute("tankCount", tanks.size());
        model.addAttribute("pipelineCount", pipelines.size());
        model.addAttribute("scheduleCount", schedules.size());
        model.addAttribute("requestCount", requests.size());
        model.addAttribute("pendingCount", pending.size());
        model.addAttribute("highPendingCount", highPending);
        model.addAttribute("lowTankCount", lowTanks);
        model.addAttribute("totalCapacity", totalCapacity);
        model.addAttribute("totalStored", totalStored);
        model.addAttribute("recentRequests", recentRequests);
        model.addAttribute("upcomingSchedules", upcoming);
        model.addAttribute("tanks", tanks.stream()
                .sorted(Comparator.comparing(WaterTank::getId)).toList());
        return "distribution/home";
    }
}
