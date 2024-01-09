package net.rostkoff.simpletodoapp.controllers;

import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.services.TaskService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/tasks/{id}")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    public String getTaskPageView(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
        model.addAttribute("task", taskService.getTask(id));
        return "taskView";
    }

    @DeleteMapping("/tasks/{id}")
    public String deleteTask(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
        taskService.deleteTask(id);
        redirectAttributes.addAttribute("message", "Task Deleted");
        return "redirect:/";
    }

    @GetMapping("/tasks/edit/{id}")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public String getEditPageView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("task", taskService.getTask(id));
        return "editTask";
    }
}
