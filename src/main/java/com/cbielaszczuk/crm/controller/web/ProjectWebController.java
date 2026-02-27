package com.cbielaszczuk.crm.controller.web;

import com.cbielaszczuk.crm.dto.ProjectDTO;
import com.cbielaszczuk.crm.model.ProjectStatusEnum;
import com.cbielaszczuk.crm.service.ClientService;
import com.cbielaszczuk.crm.service.ProjectService;
import com.cbielaszczuk.crm.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
public class ProjectWebController {

    private final ProjectService projectService;
    private final ClientService clientService;
    private final TaskService taskService;

    public ProjectWebController(ProjectService projectService, ClientService clientService, TaskService taskService) {
        this.projectService = projectService;
        this.clientService = clientService;
        this.taskService = taskService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("project", new ProjectDTO());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("statuses", ProjectStatusEnum.values());
        model.addAttribute("isEdit", false);
        return "projects/form";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("project") ProjectDTO dto, RedirectAttributes ra) {
        try {
            projectService.createProject(dto);
            ra.addFlashAttribute("success", "Project created successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ProjectDTO project = projectService.getProjectById(id);
        if (project == null) return "redirect:/projects";
        model.addAttribute("project", project);
        model.addAttribute("tasks", taskService.getTasksByProjectId(id));
        return "projects/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ProjectDTO project = projectService.getProjectById(id);
        if (project == null) return "redirect:/projects";
        model.addAttribute("project", project);
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("statuses", ProjectStatusEnum.values());
        model.addAttribute("isEdit", true);
        return "projects/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute("project") ProjectDTO dto,
                         RedirectAttributes ra) {
        try {
            dto.setId(id);
            projectService.updateProject(dto);
            ra.addFlashAttribute("success", "Project updated successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/projects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        projectService.deleteProject(id);
        ra.addFlashAttribute("success", "Project deleted successfully.");
        return "redirect:/projects";
    }
}
