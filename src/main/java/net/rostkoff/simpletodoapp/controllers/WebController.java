package net.rostkoff.simpletodoapp.controllers;

import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.services.ServiceCatalog;
import net.rostkoff.simpletodoapp.services.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {
    private final TaskService taskService;
    public WebController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/welcome")
    public String getWelcomeView() {
        return "welcome";
    }
    // TODO: Figure out how to add category.
    @GetMapping("/tasks/add")
    public String getAddPageView(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("task", new TaskDto());
        return "addTask";
    }

    @PostMapping("/tasks/add")
    public String addPage(Model model, TaskDto taskDto, RedirectAttributes redirectAttributes) {
        taskService.addTask(taskDto);
        redirectAttributes.addAttribute("message", "Task Created");
        return "redirect:/";
    }
}
