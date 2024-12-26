package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.model.BlockedClient;
import com.epam.rd.autocode.spring.project.repo.BlockedClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientBlockingService;
import org.springframework.stereotype.Service;

@Service
public class ClientBlockingServiceImpl implements ClientBlockingService {

    private BlockedClientRepository blockedClientRepository;

    public ClientBlockingServiceImpl(BlockedClientRepository blockedClientRepository) {
        this.blockedClientRepository = blockedClientRepository;
    }

    @Override
    public String blockClient(String clientEmail) {
        if (!blockedClientRepository.existsByClientEmail(clientEmail)) {
            BlockedClient blockedClient = new BlockedClient();
            blockedClient.setClientEmail(clientEmail);
            blockedClientRepository.save(blockedClient);
            return "Client was successfully blocked";
        }
        return "Client was already blocked";
    }

    @Override
    public void unblockClient(String clientEmail) {
        blockedClientRepository.deleteByClientEmail(clientEmail);
    }

    @Override
    public boolean isClientBlocked(String clientEmail) {
        return blockedClientRepository.existsByClientEmail(clientEmail);
    }
}
