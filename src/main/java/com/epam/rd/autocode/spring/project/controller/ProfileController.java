package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final EmployeeService employeeService;
    private final ClientService clientService;

    public ProfileController(EmployeeService employeeService, ClientService clientService) {
        this.employeeService = employeeService;
        this.clientService = clientService;
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_EMPLOYEE"))) {
            EmployeeDTO employeeDTO = employeeService.getEmployeeByEmail(userDetails.getUsername());
            if (employeeDTO != null) {
                model.addAttribute("employees", List.of(employeeDTO));
                return "employees";
            }
        } else if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT"))) {
            ClientDTO clientDTO = clientService.getClientByEmail(userDetails.getUsername());
            if (clientDTO != null) {
                model.addAttribute("clients", List.of(clientDTO));
                return "clients";
            }
        }
        return "error";
    }

    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_EMPLOYEE"))) {
            employeeService.deleteEmployeeByEmail(userDetails.getUsername());
            return "redirect:/login";
        } else if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT"))) {
            clientService.deleteClientByEmail(userDetails.getUsername());
            return "redirect:/login";
        }
        return "error";
    }

    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_EMPLOYEE"))) {
            EmployeeDTO employeeDTO = employeeService.getEmployeeByEmail(userDetails.getUsername());
            model.addAttribute("employee", employeeDTO);
            return "employee-edit";
        } else if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT"))) {
            ClientDTO clientDTO = clientService.getClientByEmail(userDetails.getUsername());
            model.addAttribute("client", clientDTO);
            return "client-edit";
        }

        return "error";
    }

    @PostMapping("/edit/{email}/employee")
    public String editEmployee(
            @PathVariable(name = "email") String email,
            @Valid @ModelAttribute(name = "employee") EmployeeDTO employee,
            BindingResult result, Model model)
    {
        if (result.hasErrors()) {
            return "employee-edit";
        }
        employeeService.updateEmployeeByEmail(email, employee);
        return "redirect:/profile";
    }

    @PostMapping("/edit/{email}/client")
    public String editClient(
            @PathVariable(name = "email") String email,
            @Valid @ModelAttribute(name = "client") ClientDTO client,
            BindingResult result)
    {
        if (result.hasErrors()) {
            return "client-edit";
        }
        clientService.updateClientByEmail(email, client);
        return "redirect:/profile";
    }

}
