package com.cbielaszczuk.crm.controller.web;

import com.cbielaszczuk.crm.dto.TaskDTO;
import com.cbielaszczuk.crm.model.TaskStatusEnum;
import com.cbielaszczuk.crm.service.ProjectService;
import com.cbielaszczuk.crm.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
public class TaskWebController {

    private final TaskService taskService;
    private final ProjectService projectService;

    public TaskWebController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("task", new TaskDTO());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("statuses", TaskStatusEnum.values());
        model.addAttribute("isEdit", false);
        return "tasks/form";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("task") TaskDTO dto, RedirectAttributes ra) {
        try {
            taskService.createTask(dto);
            ra.addFlashAttribute("success", "Task created successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        TaskDTO task = taskService.getTaskById(id);
        if (task == null) return "redirect:/tasks";
        model.addAttribute("task", task);
        return "tasks/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        TaskDTO task = taskService.getTaskById(id);
        if (task == null) return "redirect:/tasks";
        model.addAttribute("task", task);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("statuses", TaskStatusEnum.values());
        model.addAttribute("isEdit", true);
        return "tasks/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute("task") TaskDTO dto,
                         RedirectAttributes ra) {
        try {
            dto.setId(id);
            taskService.updateTask(dto);
            ra.addFlashAttribute("success", "Task updated successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        taskService.deleteTask(id);
        ra.addFlashAttribute("success", "Task deleted successfully.");
        return "redirect:/tasks";
    }
}
