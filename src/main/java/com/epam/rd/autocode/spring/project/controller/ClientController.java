package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientBlockingService;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    ClientService clientService;
    ClientBlockingService clientBlockingService;

    public ClientController(ClientService clientService, ClientBlockingService clientBlockingService) {
        this.clientService = clientService;
        this.clientBlockingService = clientBlockingService;
    }

    @GetMapping("/all")
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{email}")
    public ClientDTO getClientByEmail(@PathVariable String email) {
        return clientService.getClientByEmail(email);
    }

    @GetMapping("/{email}/block")
    public String blockClient(@PathVariable(name = "email") String email) {
        String blockingResult = clientBlockingService.blockClient(email);
        return blockingResult;
    }

    @GetMapping("/{email}/unblock")
    public String unblockClient(@PathVariable(name = "email") String email) {
        clientBlockingService.unblockClient(email);
        return "Client was unblocked successfully!";
    }
}
