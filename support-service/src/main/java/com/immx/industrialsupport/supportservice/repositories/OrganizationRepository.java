package com.immx.industrialsupport.supportservice.repositories;

import com.immx.industrialsupport.supportservice.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    public Optional<Organization> findByName(String name);
}
