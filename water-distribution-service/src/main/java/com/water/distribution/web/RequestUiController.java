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

import com.water.distribution.entity.SupplyZone;
import com.water.distribution.entity.WaterSupplyRequest;
import com.water.distribution.service.SupplyZoneService;
import com.water.distribution.service.WaterSupplyRequestService;

@Controller
@RequestMapping("/distribution/requests")
public class RequestUiController {

    private static final List<String> STATUSES =
            List.of("PENDING", "APPROVED", "REJECTED", "COMPLETED");
    private static final List<String> PRIORITIES = List.of("HIGH", "MEDIUM", "LOW");
    private static final String LIST = "redirect:/distribution/requests";

    private final WaterSupplyRequestService requestService;
    private final SupplyZoneService zoneService;

    public RequestUiController(WaterSupplyRequestService requestService,
                               SupplyZoneService zoneService) {
        this.requestService = requestService;
        this.zoneService = zoneService;
    }

    @InitBinder("supplyRequest")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "requestDate", "supplyZone*");
    }

    /** List with optional search filters (GET /api/distributions/search). */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String priority,
                       @RequestParam(required = false) Long zoneId,
                       Model model) {

        List<WaterSupplyRequest> requests = requestService
                .searchRequests(keyword, status, priority, zoneId).stream()
                .sorted(Comparator.comparing(WaterSupplyRequest::getId).reversed())
                .toList();

        boolean filtering = (keyword != null && !keyword.isBlank())
                || (status != null && !status.isBlank())
                || (priority != null && !priority.isBlank())
                || zoneId != null;

        model.addAttribute("active", "requests");
        model.addAttribute("requests", requests);
        model.addAttribute("zones", sortedZones());
        model.addAttribute("statuses", STATUSES);
        model.addAttribute("priorities", PRIORITIES);
        model.addAttribute("keyword", keyword);
        model.addAttribute("fStatus", status);
        model.addAttribute("fPriority", priority);
        model.addAttribute("fZoneId", zoneId);
        model.addAttribute("filtering", filtering);
        return "distribution/requests";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        WaterSupplyRequest r = new WaterSupplyRequest();
        r.setStatus("PENDING");
        r.setPriority("MEDIUM");
        return form(model, r, "New Water Supply Request", "/distribution/requests");
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return requestService.getRequestById(id)
                .map(r -> form(model, r, "Edit Supply Request #" + id,
                        "/distribution/requests/" + id))
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Supply request not found.");
                    return LIST;
                });
    }

    @PostMapping
    public String create(@ModelAttribute("supplyRequest") WaterSupplyRequest request,
                         BindingResult br,
                         @RequestParam(required = false) Long zoneId,
                         Model model, RedirectAttributes ra) {
        return persist(null, request, br, zoneId, model, ra);
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("supplyRequest") WaterSupplyRequest request,
                         BindingResult br,
                         @RequestParam(required = false) Long zoneId,
                         Model model, RedirectAttributes ra) {
        return persist(id, request, br, zoneId, model, ra);
    }

    /** Quick priority change (PATCH /api/distributions/{id}/priority). */
    @PostMapping("/{id}/priority")
    public String changePriority(@PathVariable Long id,
                                 @RequestParam String priority,
                                 RedirectAttributes ra) {
        try {
            requestService.updatePriority(id, priority);
            ra.addFlashAttribute("success",
                    "Priority of request #" + id + " set to " + priority + ".");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", "Supply request not found.");
        }
        return LIST;
    }

    /** Quick status change, e.g. approve or reject a pending request. */
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        try {
            WaterSupplyRequest existing = requestService.getRequestById(id)
                    .orElseThrow(() -> new RuntimeException("Supply request not found."));
            existing.setStatus(status);
            requestService.updateRequest(id, existing);
            ra.addFlashAttribute("success",
                    "Status of request #" + id + " set to " + status + ".");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return LIST;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            requestService.deleteRequest(id);
            ra.addFlashAttribute("success", "Supply request deleted.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return LIST;
    }

    private String persist(Long id, WaterSupplyRequest request, BindingResult br,
                           Long zoneId, Model model, RedirectAttributes ra) {
        request.setSupplyZone(zoneId == null ? null
                : zoneService.getZoneById(zoneId).orElse(null));

        String problem = validate(request, br);
        if (problem == null) {
            try {
                if (id == null) {
                    requestService.addRequest(request);
                } else {
                    requestService.updateRequest(id, request);
                }
                ra.addFlashAttribute("success", "Supply request from \""
                        + request.getRequesterName() + "\" was "
                        + (id == null ? "submitted." : "updated."));
                return LIST;
            } catch (RuntimeException e) {
                problem = "Could not save the request: " + e.getMessage();
            }
        }
        model.addAttribute("error", problem);
        return id == null
                ? form(model, request, "New Water Supply Request", "/distribution/requests")
                : form(model, request, "Edit Supply Request #" + id,
                        "/distribution/requests/" + id);
    }

    private String validate(WaterSupplyRequest r, BindingResult br) {
        if (br.hasErrors()) {
            return "Requested litres must be a whole number.";
        }
        if (r.getRequesterName() == null || r.getRequesterName().isBlank()) {
            return "Requester name is required.";
        }
        if (r.getAddress() == null || r.getAddress().isBlank()) {
            return "Address is required.";
        }
        if (r.getPurpose() == null || r.getPurpose().isBlank()) {
            return "Purpose is required.";
        }
        if (r.getRequestedLitres() == null || r.getRequestedLitres() <= 0) {
            return "Requested litres must be greater than zero.";
        }
        return null;
    }

    private List<SupplyZone> sortedZones() {
        return zoneService.getAllZones().stream()
                .sorted(Comparator.comparing(SupplyZone::getId)).toList();
    }

    private String form(Model model, WaterSupplyRequest request, String title, String action) {
        model.addAttribute("active", "requests");
        model.addAttribute("supplyRequest", request);
        model.addAttribute("zones", sortedZones());
        model.addAttribute("pageTitle", title);
        model.addAttribute("formAction", action);
        model.addAttribute("statuses", UiHelper.withCurrent(STATUSES, request.getStatus()));
        model.addAttribute("priorities", UiHelper.withCurrent(PRIORITIES, request.getPriority()));
        return "distribution/request-form";
    }
}
