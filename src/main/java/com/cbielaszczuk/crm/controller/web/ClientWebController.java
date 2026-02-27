package com.cbielaszczuk.crm.controller.web;

import com.cbielaszczuk.crm.dto.ClientDTO;
import com.cbielaszczuk.crm.service.ClientService;
import com.cbielaszczuk.crm.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
public class ClientWebController {

    private final ClientService clientService;
    private final ProjectService projectService;

    public ClientWebController(ClientService clientService, ProjectService projectService) {
        this.clientService = clientService;
        this.projectService = projectService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("client", new ClientDTO());
        model.addAttribute("isEdit", false);
        return "clients/form";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("client") ClientDTO dto, RedirectAttributes ra) {
        try {
            clientService.createClient(dto);
            ra.addFlashAttribute("success", "Client created successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clients";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ClientDTO client = clientService.getClientById(id);
        if (client == null) return "redirect:/clients";
        model.addAttribute("client", client);
        model.addAttribute("projects", projectService.getProjectsByClientId(id));
        return "clients/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ClientDTO client = clientService.getClientById(id);
        if (client == null) return "redirect:/clients";
        model.addAttribute("client", client);
        model.addAttribute("isEdit", true);
        return "clients/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute("client") ClientDTO dto,
                         RedirectAttributes ra) {
        try {
            dto.setId(id);
            clientService.updateClient(dto);
            ra.addFlashAttribute("success", "Client updated successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        clientService.deleteClient(id);
        ra.addFlashAttribute("success", "Client deleted successfully.");
        return "redirect:/clients";
    }
}
