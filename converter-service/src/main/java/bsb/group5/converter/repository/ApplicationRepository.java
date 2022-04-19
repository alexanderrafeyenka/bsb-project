package bsb.group5.converter.repository;

import bsb.group5.converter.repository.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Application findByUuid(UUID uuid);

    boolean existsByUuid(UUID applicationId);
}
