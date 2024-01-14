package net.rostkoff.simpletodoapp.controllers;

import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.formatters.IFormatDates;
import net.rostkoff.simpletodoapp.services.TaskService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {
    private final TaskService taskService;
    private final IFormatDates<TaskDto> taskDtoFormatter;

    public WebController(TaskService taskService, IFormatDates<TaskDto> taskDtoFormatter) {
        this.taskService = taskService;
        this.taskDtoFormatter = taskDtoFormatter;
    }

    @GetMapping("/")
    public String getMainView(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("module", "calendar");
        return "index";
    }
    
    @GetMapping("/tasks/add")
    public String getAddPageView(Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("task", new TaskDto());
        model.addAttribute("module", "addTask");
        return "addTask";
    }

    @PostMapping("/tasks/add")
    public String addPage(Model model, TaskDto taskDto, RedirectAttributes redirectAttributes) {
        var id = taskService.addTask(taskDto);
        redirectAttributes.addAttribute("message", "Task Created");
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/tasks/{id}")
    public String getTaskPageView(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
        var task = taskService.getTask(id);
        model.addAttribute("task", task);
        model.addAttribute("dates", taskDtoFormatter.getFormattedDates(task));
        return "taskView";
    }

    @DeleteMapping("/tasks/{id}")
    public String deleteTask(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
        taskService.deleteTask(id);
        redirectAttributes.addAttribute("message", "Task Deleted");
        return "redirect:/";
    }

    @GetMapping("/tasks/edit/{id}")
    public String getEditPageView(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
        var task = taskService.getTask(id);
        model.addAttribute("task", task);
        model.addAttribute("dates", taskDtoFormatter.getFormattedDates(task));
        return "editTask";
    }

    @PutMapping("/tasks/edit")
    public String updateTask(Model model, TaskDto taskDto, RedirectAttributes redirectAttributes) {
        taskService.updateTask(taskDto);
        redirectAttributes.addAttribute("message", "Task Updated");
        return "redirect:/tasks/" + taskDto.getId();
    }
}
