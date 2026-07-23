package com.immx.industrialsupport.supportservice.services.incident;

import com.immx.industrialsupport.supportservice.dto.incident.*;
import com.immx.industrialsupport.supportservice.dto.role.RoleName;
import com.immx.industrialsupport.supportservice.entities.Department;
import com.immx.industrialsupport.supportservice.entities.Incident;
import com.immx.industrialsupport.supportservice.entities.Organization;
import com.immx.industrialsupport.supportservice.entities.User;
import com.immx.industrialsupport.supportservice.exception_handling.department.NotFoundDepartmentException;
import com.immx.industrialsupport.supportservice.exception_handling.incident.InvalidIncidentOperationException;
import com.immx.industrialsupport.supportservice.exception_handling.incident.NotFoundIncidentException;
import com.immx.industrialsupport.supportservice.exception_handling.organization.NotFoundOrganizationException;
import com.immx.industrialsupport.supportservice.exception_handling.user.NotFoundUserException;
import com.immx.industrialsupport.supportservice.repositories.DepartmentRepository;
import com.immx.industrialsupport.supportservice.repositories.IncidentRepository;
import com.immx.industrialsupport.supportservice.repositories.OrganizationRepository;
import com.immx.industrialsupport.supportservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с обращениями.
 */
@Service
@Transactional
public class IncidentService implements IIncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Incident create(UUID organizationId,
                           CreateIncidentRequest createIncidentRequest) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundOrganizationException(
                        "There is no organization with ID = " + organizationId));

        Department department = departmentRepository.findByIdAndOrganizationId(
                        organizationId,
                        createIncidentRequest.getDepartmentId())
                .orElseThrow(() -> new NotFoundDepartmentException(
                        "There is not department with ID = " + createIncidentRequest.getDepartmentId()));

        User reporter = userRepository.findByIdWithRoles(createIncidentRequest.getReporterId())
                .orElseThrow(() -> new NotFoundUserException(
                        "There is no user with ID = " + createIncidentRequest.getReporterId()));

        if(!reporter.getDepartment()
                .getId()
                .equals(department.getId()))
            throw new InvalidIncidentOperationException("Reporter does not belong to the specified department");

        if(!reporter.isEnabled())
            throw new InvalidIncidentOperationException("Disabled user cannot create an incident");

        OffsetDateTime slaDeadline = calculateSlaDeadline(createIncidentRequest.getPriority());

        Incident incident = new Incident(
                organization,
                department,
                reporter,
                createIncidentRequest.getTitle(),
                createIncidentRequest.getDescription(),
                createIncidentRequest.getPriority(),
                slaDeadline);

        return incidentRepository.save(incident);
    }

    @Override
    public Incident getById(UUID organizationId,
                            UUID incidentId) {
        return incidentRepository.findByIdAndOrganization_Id(
                        incidentId,
                        organizationId)
                .orElseThrow(() -> new NotFoundIncidentException(
                        "There is no incident with ID = " + incidentId + " in organization with ID = "
                                + organizationId));
    }

    @Override
    public List<Incident> getAllByOrganization(UUID organizationId) {
        if(!organizationRepository.existsById(organizationId))
            throw new NotFoundOrganizationException("There is no organization with ID = " + organizationId);

        return incidentRepository.findAllByOrganization_Id(organizationId);
    }

    @Override
    public Incident assignEngineer(UUID organizationId,
                                   UUID incidentId,
                                   AssignIncidentRequest assignIncidentRequest) {
        Incident incident = getById(
                organizationId,
                incidentId);

        User engineer = userRepository.findByIdWithRoles(assignIncidentRequest.getEngineerId())
                .orElseThrow(() -> new NotFoundUserException(
                        "There is no user with ID = " + assignIncidentRequest.getEngineerId()));

        if(!engineer.isEnabled())
            throw new InvalidIncidentOperationException("Disabled user cannot be assigned to an incident");

        boolean hasEngineerRole = engineer.getRoles()
                .stream()
                .anyMatch(role -> role.getName() == RoleName.ROLE_ENGINEER);

        if(!hasEngineerRole)
            throw new InvalidIncidentOperationException("Selected user does not have ROLE_ENGINEER");

        boolean belongsToOrganization = engineer.getDepartment()
                .getOrganization()
                .getId()
                .equals(organizationId);

        if(!belongsToOrganization)
            throw new InvalidIncidentOperationException("Engineer belongs to another organization");

        if(incident.getStatus() == IncidentStatus.CLOSED || incident.getStatus() == IncidentStatus.CANCELLED)
            throw new InvalidIncidentOperationException("Closed or cancelled incident cannot be assigned");

        incident.setAssignedEngineer(engineer);
        incident.setStatus(IncidentStatus.ASSIGNED);

        return incidentRepository.save(incident);
    }

    @Override
    public Incident changeStatus(UUID organizationId,
                                 UUID incidentId,
                                 ChangeIncidentStatusRequest changeIncidentStatusRequest) {
        Incident incident = getById(
                organizationId,
                incidentId);
        IncidentStatus newStatus = changeIncidentStatusRequest.status();

        if(!isTransitionAllowed(
                incident.getStatus(),
                newStatus))
            throw new InvalidIncidentOperationException(
                    "Transition from " + incident.getStatus() + " to " + newStatus + " is not allowed");

        if(newStatus == IncidentStatus.IN_PROGRESS && incident.getAssignedEngineer() == null)
            throw new InvalidIncidentOperationException("Incident cannot be started without an assigned engineer");

        incident.setStatus(newStatus);

        if(newStatus == IncidentStatus.RESOLVED)
            incident.setResolvedAt(OffsetDateTime.now());

        if(newStatus == IncidentStatus.CLOSED)
            incident.setClosedAt(OffsetDateTime.now());

        if(newStatus == IncidentStatus.IN_PROGRESS) {
            incident.setResolvedAt(null);
            incident.setClosedAt(null);
        }

        return incidentRepository.save(incident);
    }

    private OffsetDateTime calculateSlaDeadline(IncidentPriority priority) {
        OffsetDateTime now = OffsetDateTime.now();

        return switch(priority) {
            case CRITICAL -> now.plusHours(1);
            case HIGH -> now.plusHours(4);
            case MEDIUM -> now.plusHours(8);
            case LOW -> now.plusHours(24);
        };
    }

    private boolean isTransitionAllowed(IncidentStatus current,
                                        IncidentStatus target) {
        if(current == target)
            return false;

        return switch(current) {
            case NEW -> target == IncidentStatus.ASSIGNED || target == IncidentStatus.CANCELLED;
            case ASSIGNED -> target == IncidentStatus.IN_PROGRESS || target == IncidentStatus.CANCELLED;
            case IN_PROGRESS -> target == IncidentStatus.RESOLVED || target == IncidentStatus.CANCELLED;
            case RESOLVED -> target == IncidentStatus.CLOSED || target == IncidentStatus.IN_PROGRESS;
            case CLOSED, CANCELLED -> false;
        };
    }
}
