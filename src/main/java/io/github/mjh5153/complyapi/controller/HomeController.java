package io.github.mjh5153.complyapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Sends visitors landing on {@code /} straight to the Swagger UI so the
 * root of a deployment is a useful page instead of a 404.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui/index.html";
    }
}

