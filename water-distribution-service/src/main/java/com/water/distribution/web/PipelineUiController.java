package com.water.distribution.web;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.water.distribution.entity.Pipeline;
import com.water.distribution.entity.SupplyZone;
import com.water.distribution.entity.WaterTank;
import com.water.distribution.service.PipelineService;
import com.water.distribution.service.SupplyZoneService;
import com.water.distribution.service.WaterTankService;

@Controller
@RequestMapping("/distribution/pipelines")
public class PipelineUiController {

    private static final List<String> STATUSES =
            List.of("ACTIVE", "UNDER_MAINTENANCE", "INACTIVE");
    private static final String LIST = "redirect:/distribution/pipelines";

    private final PipelineService pipelineService;
    private final WaterTankService tankService;
    private final SupplyZoneService zoneService;

    public PipelineUiController(PipelineService pipelineService,
                                WaterTankService tankService,
                                SupplyZoneService zoneService) {
        this.pipelineService = pipelineService;
        this.tankService = tankService;
        this.zoneService = zoneService;
    }

    @InitBinder("pipeline")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "waterTank*", "supplyZone*");
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("active", "pipelines");
        model.addAttribute("pipelines", pipelineService.getAllPipelines().stream()
                .sorted(Comparator.comparing(Pipeline::getId)).toList());
        return "distribution/pipelines";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Pipeline pipeline = new Pipeline();
        pipeline.setStatus("ACTIVE");
        return form(model, pipeline, "Add Pipeline", "/distribution/pipelines");
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return pipelineService.getPipelineById(id)
                .map(p -> form(model, p, "Edit Pipeline #" + id,
                        "/distribution/pipelines/" + id))
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Pipeline not found.");
                    return LIST;
                });
    }

    @PostMapping
    public String create(@ModelAttribute("pipeline") Pipeline pipeline, BindingResult br,
                         @RequestParam(required = false) Long tankId,
                         @RequestParam(required = false) Long zoneId,
                         Model model, RedirectAttributes ra) {
        return persist(null, pipeline, br, tankId, zoneId, model, ra);
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("pipeline") Pipeline pipeline, BindingResult br,
                         @RequestParam(required = false) Long tankId,
                         @RequestParam(required = false) Long zoneId,
                         Model model, RedirectAttributes ra) {
        return persist(id, pipeline, br, tankId, zoneId, model, ra);
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            pipelineService.deletePipeline(id);
            ra.addFlashAttribute("success", "Pipeline deleted.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return LIST;
    }

    private String persist(Long id, Pipeline pipeline, BindingResult br,
                           Long tankId, Long zoneId,
                           Model model, RedirectAttributes ra) {
        // Resolve the optional tank / zone selections from the dropdowns.
        pipeline.setWaterTank(tankId == null ? null
                : tankService.getTankById(tankId).orElse(null));
        pipeline.setSupplyZone(zoneId == null ? null
                : zoneService.getZoneById(zoneId).orElse(null));

        String problem = validate(pipeline, br);
        if (problem == null) {
            try {
                if (id == null) {
                    pipelineService.addPipeline(pipeline);
                } else {
                    pipelineService.updatePipeline(id, pipeline);
                }
                ra.addFlashAttribute("success", "Pipeline \"" + pipeline.getName()
                        + "\" was " + (id == null ? "added." : "updated."));
                return LIST;
            } catch (RuntimeException e) {
                problem = "Could not save the pipeline: " + e.getMessage();
            }
        }
        model.addAttribute("error", problem);
        return id == null
                ? form(model, pipeline, "Add Pipeline", "/distribution/pipelines")
                : form(model, pipeline, "Edit Pipeline #" + id,
                        "/distribution/pipelines/" + id);
    }

    private String validate(Pipeline pipeline, BindingResult br) {
        if (br.hasErrors()) {
            return "Length must be a valid number.";
        }
        if (pipeline.getName() == null || pipeline.getName().isBlank()) {
            return "Pipeline name is required.";
        }
        if (pipeline.getLengthKm() != null && pipeline.getLengthKm().signum() < 0) {
            return "Length cannot be negative.";
        }
        return null;
    }

    private String form(Model model, Pipeline pipeline, String title, String action) {
        List<WaterTank> tanks = tankService.getAllTanks().stream()
                .sorted(Comparator.comparing(WaterTank::getId)).toList();
        List<SupplyZone> zones = zoneService.getAllZones().stream()
                .sorted(Comparator.comparing(SupplyZone::getId)).toList();
        model.addAttribute("active", "pipelines");
        model.addAttribute("pipeline", pipeline);
        model.addAttribute("tanks", tanks);
        model.addAttribute("zones", zones);
        model.addAttribute("pageTitle", title);
        model.addAttribute("formAction", action);
        model.addAttribute("statuses", UiHelper.withCurrent(STATUSES, pipeline.getStatus()));
        return "distribution/pipeline-form";
    }
}
