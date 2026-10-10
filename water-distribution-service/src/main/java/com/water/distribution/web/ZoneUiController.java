package com.water.distribution.web;

import java.util.Comparator;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.water.distribution.entity.SupplyZone;
import com.water.distribution.service.SupplyZoneService;

@Controller
@RequestMapping("/distribution/zones")
public class ZoneUiController {

    private static final List<String> STATUSES = List.of("ACTIVE", "INACTIVE");
    private static final String LIST = "redirect:/distribution/zones";

    private final SupplyZoneService zoneService;

    public ZoneUiController(SupplyZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @InitBinder("zone")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("active", "zones");
        model.addAttribute("zones", zoneService.getAllZones().stream()
                .sorted(Comparator.comparing(SupplyZone::getId)).toList());
        return "distribution/zones";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        SupplyZone zone = new SupplyZone();
        zone.setStatus("ACTIVE");
        return form(model, zone, "Add Supply Zone", "/distribution/zones");
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return zoneService.getZoneById(id)
                .map(z -> form(model, z, "Edit Supply Zone #" + id,
                        "/distribution/zones/" + id))
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Supply zone not found.");
                    return LIST;
                });
    }

    @PostMapping
    public String create(@ModelAttribute("zone") SupplyZone zone, BindingResult br,
                         Model model, RedirectAttributes ra) {
        return persist(null, zone, br, model, ra);
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("zone") SupplyZone zone,
                         BindingResult br, Model model, RedirectAttributes ra) {
        return persist(id, zone, br, model, ra);
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            zoneService.deleteZone(id);
            ra.addFlashAttribute("success", "Supply zone deleted.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "This zone is still used by pipelines, "
                    + "schedules or supply requests, so it cannot be deleted.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return LIST;
    }

    private String persist(Long id, SupplyZone zone, BindingResult br,
                           Model model, RedirectAttributes ra) {
        String problem = validate(zone, br);
        if (problem == null) {
            try {
                if (id == null) {
                    zoneService.addZone(zone);
                } else {
                    zoneService.updateZone(id, zone);
                }
                ra.addFlashAttribute("success", "Supply zone \"" + zone.getName()
                        + "\" was " + (id == null ? "added." : "updated."));
                return LIST;
            } catch (RuntimeException e) {
                problem = "Could not save the zone: " + e.getMessage();
            }
        }
        model.addAttribute("error", problem);
        return id == null
                ? form(model, zone, "Add Supply Zone", "/distribution/zones")
                : form(model, zone, "Edit Supply Zone #" + id, "/distribution/zones/" + id);
    }

    private String validate(SupplyZone zone, BindingResult br) {
        if (br.hasErrors()) {
            return "Population must be a whole number.";
        }
        if (zone.getName() == null || zone.getName().isBlank()) {
            return "Zone name is required.";
        }
        if (zone.getPopulation() != null && zone.getPopulation() < 0) {
            return "Population cannot be negative.";
        }
        return null;
    }

    private String form(Model model, SupplyZone zone, String title, String action) {
        model.addAttribute("active", "zones");
        model.addAttribute("zone", zone);
        model.addAttribute("pageTitle", title);
        model.addAttribute("formAction", action);
        model.addAttribute("statuses", UiHelper.withCurrent(STATUSES, zone.getStatus()));
        return "distribution/zone-form";
    }
}
