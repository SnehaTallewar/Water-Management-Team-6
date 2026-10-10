package com.water.distribution.web;

import java.math.BigDecimal;
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

import com.water.distribution.entity.WaterTank;
import com.water.distribution.service.WaterTankService;

@Controller
@RequestMapping("/distribution/tanks")
public class TankUiController {

    private static final List<String> STATUSES =
            List.of("ACTIVE", "MAINTENANCE", "INACTIVE");
    private static final String LIST = "redirect:/distribution/tanks";

    private final WaterTankService tankService;

    public TankUiController(WaterTankService tankService) {
        this.tankService = tankService;
    }

    @InitBinder("tank")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("active", "tanks");
        model.addAttribute("tanks", tankService.getAllTanks().stream()
                .sorted(Comparator.comparing(WaterTank::getId)).toList());
        return "distribution/tanks";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        WaterTank tank = new WaterTank();
        tank.setStatus("ACTIVE");
        return form(model, tank, "Add Water Tank", "/distribution/tanks");
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return tankService.getTankById(id)
                .map(t -> form(model, t, "Edit Water Tank #" + id,
                        "/distribution/tanks/" + id))
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Water tank not found.");
                    return LIST;
                });
    }

    @PostMapping
    public String create(@ModelAttribute("tank") WaterTank tank, BindingResult br,
                         Model model, RedirectAttributes ra) {
        return persist(null, tank, br, model, ra);
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("tank") WaterTank tank,
                         BindingResult br, Model model, RedirectAttributes ra) {
        return persist(id, tank, br, model, ra);
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            tankService.deleteTank(id);
            ra.addFlashAttribute("success", "Water tank deleted.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "This tank is still connected to pipelines, "
                    + "so it cannot be deleted. Remove or re-assign those pipelines first.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return LIST;
    }

    private String persist(Long id, WaterTank tank, BindingResult br,
                           Model model, RedirectAttributes ra) {
        String problem = validate(tank, br);
        if (problem == null) {
            try {
                if (id == null) {
                    tankService.addTank(tank);
                } else {
                    tankService.updateTank(id, tank);
                }
                ra.addFlashAttribute("success", "Water tank \"" + tank.getName()
                        + "\" was " + (id == null ? "added." : "updated."));
                return LIST;
            } catch (RuntimeException e) {
                problem = "Could not save the tank: " + e.getMessage();
            }
        }
        model.addAttribute("error", problem);
        return id == null
                ? form(model, tank, "Add Water Tank", "/distribution/tanks")
                : form(model, tank, "Edit Water Tank #" + id, "/distribution/tanks/" + id);
    }

    private String validate(WaterTank tank, BindingResult br) {
        if (br.hasErrors()) {
            return "Capacity and current level must be valid numbers.";
        }
        if (tank.getName() == null || tank.getName().isBlank()) {
            return "Tank name is required.";
        }
        BigDecimal capacity = tank.getCapacityLitres();
        BigDecimal level = tank.getCurrentLevelLitres();
        if (capacity == null || capacity.signum() <= 0) {
            return "Capacity must be greater than zero.";
        }
        if (level == null || level.signum() < 0) {
            return "Current level must be zero or greater.";
        }
        if (level.compareTo(capacity) > 0) {
            return "Current level cannot be higher than the tank capacity.";
        }
        return null;
    }

    private String form(Model model, WaterTank tank, String title, String action) {
        model.addAttribute("active", "tanks");
        model.addAttribute("tank", tank);
        model.addAttribute("pageTitle", title);
        model.addAttribute("formAction", action);
        model.addAttribute("statuses", UiHelper.withCurrent(STATUSES, tank.getStatus()));
        return "distribution/tank-form";
    }
}
