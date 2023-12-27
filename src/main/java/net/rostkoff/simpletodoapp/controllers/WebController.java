package net.rostkoff.simpletodoapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/welcome")
    public String getWelcomeView() {
        return "welcome";
    }
}
