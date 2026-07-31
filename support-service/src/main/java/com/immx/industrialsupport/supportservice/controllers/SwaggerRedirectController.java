package com.immx.industrialsupport.supportservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для редиректа в <code>Swagger</code>.
 */
@Controller
public class SwaggerRedirectController {

    /**
     * Редиректит в <code>Swagger</code>.
     * @return путь редиректа
     */
    @GetMapping("/swagger")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
