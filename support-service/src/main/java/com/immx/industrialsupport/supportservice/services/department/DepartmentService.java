package com.immx.industrialsupport.supportservice.services.department;

import com.immx.industrialsupport.supportservice.dto.department.CreateDepartmentRequest;
import com.immx.industrialsupport.supportservice.entities.Department;
import com.immx.industrialsupport.supportservice.entities.Organization;
import com.immx.industrialsupport.supportservice.exception_handling.department.DepartmentAlreadyExistsException;
import com.immx.industrialsupport.supportservice.exception_handling.department.NotFoundDepartmentException;
import com.immx.industrialsupport.supportservice.exception_handling.organization.NotFoundOrganizationException;
import com.immx.industrialsupport.supportservice.repositories.DepartmentRepository;
import com.immx.industrialsupport.supportservice.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с подразделениями организации.
 */
@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Department getById(UUID organizationId,
                              UUID departmentId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);

        if(organization.isEmpty())
            throw new NotFoundOrganizationException("There is no organization with ID = " + organizationId);

        Optional<Department> department = departmentRepository.findByIdAndOrganizationId(
                organizationId,
                departmentId);

        if(department.isEmpty())
            throw new NotFoundDepartmentException("There is no department with ID = " + departmentId);

        return department.get();
    }

    @Override
    public List<Department> getAllByOrganizationId(UUID organizationId) {
        if(!organizationRepository.existsById(organizationId))
            throw new NotFoundOrganizationException("There is not organization with ID = " + organizationId);

        return departmentRepository.findAllByOrganizationId(organizationId);
    }

    @Override
    public Department create(UUID organizationId,
                             CreateDepartmentRequest createDepartmentRequest) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);

        if(organization.isEmpty())
            throw new NotFoundOrganizationException("There is not organization with ID = " + organizationId);

        String departmentName = createDepartmentRequest.getName()
                .trim();

        boolean departmentAlreadyExists = departmentRepository.existsByOrganization_IdAndNameIgnoreCase(
                organizationId,
                departmentName);

        if(departmentAlreadyExists)
            throw new DepartmentAlreadyExistsException(
                    "Department with name = " + departmentName + " already exists in organization with ID = "
                            + organizationId);

        Department department = new Department(
                organization.get(),
                createDepartmentRequest.getExternalId(),
                departmentName);

        return departmentRepository.save(department);
    }
}
