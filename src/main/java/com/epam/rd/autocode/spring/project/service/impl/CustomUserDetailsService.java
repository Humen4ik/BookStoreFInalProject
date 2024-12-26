package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.role.ClientUserDetails;
import com.epam.rd.autocode.spring.project.role.EmployeeUserDetails;
import com.epam.rd.autocode.spring.project.service.ClientBlockingService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientBlockingService clientBlockingService;

    public CustomUserDetailsService(ClientRepository clientRepository, EmployeeRepository employeeRepository, ClientBlockingService clientBlockingService) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.clientBlockingService = clientBlockingService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.getClientByEmail(email);
        if (client.isPresent()) {
            Client clientObj = client.get();
            return new ClientUserDetails(clientBlockingService, clientObj.getEmail(), clientObj.getPassword());
        }

        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isPresent()) {
            Employee employeeObj = employee.get();
            return new EmployeeUserDetails(employeeObj.getEmail(), employeeObj.getPassword());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}