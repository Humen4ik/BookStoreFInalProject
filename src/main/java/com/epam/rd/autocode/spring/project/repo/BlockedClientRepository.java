package com.epam.rd.autocode.spring.project.repo;

import com.epam.rd.autocode.spring.project.model.BlockedClient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedClientRepository extends JpaRepository<BlockedClient, Long> {
    boolean existsByClientEmail(String clientEmail);
    @Transactional
    void deleteByClientEmail(String clientEmail);
}
