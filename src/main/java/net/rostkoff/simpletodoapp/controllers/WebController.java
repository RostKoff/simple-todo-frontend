package net.rostkoff.simpletodoapp.controllers;

import net.rostkoff.simpletodoapp.contract.CategoryDto;
import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.services.ServiceCatalog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {
    private final ServiceCatalog serviceCatalog;

    public WebController(ServiceCatalog serviceCatalog) {
        this.serviceCatalog = serviceCatalog;
    }

    @GetMapping("/welcome")
    public String getWelcomeView() {
        return "welcome";
    }
    // TODO: Figure out how to add category.
    @GetMapping("/tasks/add")
    public String getAddPageView(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("task", new TaskDto());
//        model.addAttribute("categories", serviceCatalog.getCategory().getAllCategories());
        redirectAttributes.addAttribute("categories", model.getAttribute("categories"));
        return "addTask";
    }

    @PostMapping("/tasks/add")
    public String addPage(Model model, TaskDto taskDto, RedirectAttributes redirectAttributes) {
        serviceCatalog.getTask().addTask(taskDto);
        redirectAttributes.addAttribute("message", "Task Created");
        return "redirect:/";
    }
}
