package com.cbielaszczuk.crm.controller.web;

import com.cbielaszczuk.crm.service.ClientService;
import com.cbielaszczuk.crm.service.ProjectService;
import com.cbielaszczuk.crm.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardWebController {

    private final ClientService clientService;
    private final ProjectService projectService;
    private final TaskService taskService;

    public DashboardWebController(ClientService clientService,
                                  ProjectService projectService,
                                  TaskService taskService) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("totalClients", clientService.getAllClients().size());
        model.addAttribute("totalProjects", projectService.getAllProjects().size());
        model.addAttribute("totalTasks", taskService.getAllTasks().size());
        model.addAttribute("recentClients", clientService.getAllClients().stream().limit(5).toList());
        model.addAttribute("recentProjects", projectService.getAllProjects().stream().limit(5).toList());
        return "dashboard/index";
    }
}
