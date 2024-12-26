package com.epam.rd.autocode.spring.project.role;

import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.service.ClientBlockingService;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class ClientUserDetails implements UserDetails {

    private String email;
    private String password;

    private ClientBlockingService clientBlockingService;
    public ClientUserDetails(ClientBlockingService clientBlockingService, String email, String password) {
        this.clientBlockingService = clientBlockingService;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !clientBlockingService.isClientBlocked(email);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
