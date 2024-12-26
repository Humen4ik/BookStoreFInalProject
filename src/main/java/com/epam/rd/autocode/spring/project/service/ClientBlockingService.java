package com.epam.rd.autocode.spring.project.service;

import org.springframework.stereotype.Service;


public interface ClientBlockingService {
    String blockClient(String clientEmail);
    void unblockClient(String clientEmail);
    boolean isClientBlocked(String clientEmail);
}
