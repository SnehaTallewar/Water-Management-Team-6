
package com.water.water_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModulePageController {

    @GetMapping("/resources")
    public String resources() {
        return "resources";
    }

    @GetMapping("/quality")
    public String quality() {
        return "quality";
    }

    @GetMapping("/infrastructure")
    public String infrastructure() {
        return "infrastructure";
    }
    @GetMapping("/distribution")
    public String distribution() {
        return "distribution";
    }

    @GetMapping("/complaints")
    public String complaints() {
        return "complaints";
    }
}
