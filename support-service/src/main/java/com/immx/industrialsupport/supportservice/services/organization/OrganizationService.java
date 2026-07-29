package com.immx.industrialsupport.supportservice.services.organization;

import com.immx.industrialsupport.contracts.organization.CreateOrganizationRequest;
import com.immx.industrialsupport.supportservice.entities.Organization;
import com.immx.industrialsupport.supportservice.exception_handling.organization.DeletedOrganizationException;
import com.immx.industrialsupport.supportservice.exception_handling.organization.NotFoundOrganizationException;
import com.immx.industrialsupport.supportservice.repositories.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с организациями.
 */
@Slf4j
@Service
public class OrganizationService implements IOrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization getById(UUID id) {
        Optional<Organization> organization = organizationRepository.findById(id);

        if(organization.isEmpty())
            throw new NotFoundOrganizationException("This is no such organization with ID = " + id);

        log.info(
                "Организация с идентификатором {} получена.",
                organization.get()
                        .getId());

        return organization.get();
    }

    @Override
    public Organization getByName(String organizationName) {
        Optional<Organization> organization = organizationRepository.findByName(organizationName);

        if(organization.isEmpty())
            throw new NotFoundOrganizationException("This is no such organization with Name = " + organizationName);

        log.info(
                "Организация с именем {} получена.",
                organizationName);

        return organization.get();
    }

    @Override
    public Organization save(CreateOrganizationRequest createOrganizationRequest) {
        Organization organization = new Organization(
                createOrganizationRequest.getExternalId(),
                createOrganizationRequest.getName());

        log.info(
                "Организация с идентификатором {} успешно сохранена.",
                organization.getId());

        return organizationRepository.save(organization);
    }

    @Override
    public Organization deleteById(UUID id) {
        Optional<Organization> deletedOrganization = organizationRepository.findById(id);

        if(deletedOrganization.isEmpty())
            throw new DeletedOrganizationException("This is no such organization with ID = " + id);

        organizationRepository.deleteById(id);

        log.info(
                "Организация с идентификатором {} успешно удалена.",
                id);

        return deletedOrganization.get();
    }
}
