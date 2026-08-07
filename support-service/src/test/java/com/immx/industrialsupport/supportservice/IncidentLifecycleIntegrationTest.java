package com.immx.industrialsupport.supportservice;

import com.immx.industrialsupport.contracts.department.CreateDepartmentRequest;
import com.immx.industrialsupport.contracts.incident.*;
import com.immx.industrialsupport.contracts.organization.CreateOrganizationRequest;
import com.immx.industrialsupport.contracts.role.RoleName;
import com.immx.industrialsupport.contracts.user.CreateUserRequest;
import com.immx.industrialsupport.supportservice.entities.Incident;
import com.immx.industrialsupport.supportservice.repositories.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IncidentLifecycleIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Test
    @DisplayName("Успешный жизненный цикл обращения")
    void shouldCompleteIncidentLifecycle() throws Exception {
        UUID organizationId = createOrganization(
                "ORG-001",
                "ООО Промышленное предприятие");

        UUID productionDepartmentId = createDepartment(
                organizationId,
                "DEP-PRODUCTION",
                "Производство");

        UUID supportDepartmentId = createDepartment(
                organizationId,
                "DEP-SUPPORT",
                "Техническая поддержка");

        UUID employeeId = createUser(
                productionDepartmentId,
                "EMP-001",
                "employee",
                "employee@example.com",
                "Иван",
                "Иванов",
                RoleName.ROLE_EMPLOYEE);

        UUID dispatcherId = createUser(
                supportDepartmentId,
                "DSP-001",
                "dispatcher",
                "dispatcher@example.com",
                "Анна",
                "Диспетчеровна",
                RoleName.ROLE_DISPATCHER);

        UUID engineerId = createUser(
                supportDepartmentId,
                "ENG-001",
                "engineer",
                "engineer@example.com",
                "Петр",
                "Инженеров",
                RoleName.ROLE_ENGINEER);

        CreateIncidentRequest incidentRequest = new CreateIncidentRequest(
                productionDepartmentId,
                employeeId,
                "Не запускается CAD-система",
                "При запуске появляется сообщение об отсутствии лицензии",
                IncidentPriority.HIGH);

        MvcResult createIncidentResult = mockMvc.perform(post(
                        "/api/v1/organizations/{organizationId}/incidents",
                        organizationId).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(incidentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("SUCCESS"))
                .andExpect(jsonPath("$.data.status").value("NEW"))
                .andExpect(jsonPath("$.data.reporterId").value(employeeId.toString()))
                .andExpect(jsonPath("$.data.departmentId").value(productionDepartmentId.toString()))
                .andExpect(jsonPath("$.data.assignedEngineerId").value(nullValue()))
                .andReturn();

        UUID incidentId = readDataId(createIncidentResult);

        AssignIncidentRequest assignmentRequest = new AssignIncidentRequest(engineerId);

        mockMvc.perform(patch(
                        "/api/v1/organizations/{organizationId}/incidents/{incidentId}/assignment",
                        organizationId,
                        incidentId).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(assignmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.data.assignedEngineerId").value(engineerId.toString()));

        changeStatus(
                organizationId,
                incidentId,
                IncidentStatus.IN_PROGRESS);

        changeStatus(
                organizationId,
                incidentId,
                IncidentStatus.RESOLVED).andExpect(jsonPath("$.data.resolvedAt").isNotEmpty());

        changeStatus(
                organizationId,
                incidentId,
                IncidentStatus.CLOSED).andExpect(jsonPath("$.data.closedAt").isNotEmpty());

        mockMvc.perform(get(
                        "/api/v1/organizations/{organizationId}/incidents/{incidentId}",
                        organizationId,
                        incidentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CLOSED"))
                .andExpect(jsonPath("$.data.reporterId").value(employeeId.toString()))
                .andExpect(jsonPath("$.data.assignedEngineerId").value(engineerId.toString()));

        Incident storedIncident = incidentRepository.findByIdAndOrganization_Id(
                        incidentId,
                        organizationId)
                .orElseThrow();

        assertThat(storedIncident.getStatus()).isEqualTo(IncidentStatus.CLOSED);
        assertThat(storedIncident.getReporter()
                .getId()).isEqualTo(employeeId);
        assertThat(storedIncident.getAssignedEngineer()
                .getId()).isEqualTo(engineerId);
        assertThat(storedIncident.getResolvedAt()).isNotNull();
        assertThat(storedIncident.getClosedAt()).isNotNull();
        assertThat(organizationRepository.count()).isEqualTo(1);
        assertThat(departmentRepository.count()).isEqualTo(2);
        assertThat(userRepository.count()).isEqualTo(3);
        assertThat(incidentRepository.count()).isEqualTo(1);
        assertThat(dispatcherId).isNotNull();
        assertThat(outboxEventRepository.count()).isEqualTo(5);
    }

    private UUID createOrganization(String externalId,
                                    String name) throws Exception {
        CreateOrganizationRequest request = new CreateOrganizationRequest(
                externalId,
                name);

        MvcResult result = mockMvc.perform(post("/api/v1/organizations").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("SUCCESS"))
                .andExpect(jsonPath("$.data.externalId").value(externalId))
                .andReturn();

        return readDataId(result);
    }

    private UUID createDepartment(UUID organizationId,
                                  String externalId,
                                  String name) throws Exception {
        CreateDepartmentRequest request = new CreateDepartmentRequest(
                externalId,
                name);

        MvcResult result = mockMvc.perform(post(
                        "/api/v1/organizations/{organizationId}/departments",
                        organizationId).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("SUCCESS"))
                .andExpect(jsonPath("$.data.organizationId").value(organizationId.toString()))
                .andExpect(jsonPath("$.data.externalId").value(externalId))
                .andReturn();

        return readDataId(result);
    }

    private UUID createUser(UUID departmentId,
                            String externalId,
                            String username,
                            String email,
                            String firstName,
                            String lastName,
                            RoleName role) throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                externalId,
                username,
                email,
                "123",
                firstName,
                lastName,
                Set.of(role),
                null,
                null);

        MvcResult result = mockMvc.perform(post(
                        "/api/v1/users/departments/{departmentId}",
                        departmentId).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("SUCCESS"))
                .andExpect(jsonPath("$.data.departmentId").value(departmentId.toString()))
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath(
                        "$.data.roles",
                        hasItem(role.name())))
                .andReturn();

        return readDataId(result);
    }

    private ResultActions changeStatus(UUID organizationId,
                                       UUID incidentId,
                                       IncidentStatus newStatus) throws Exception {
        ChangeIncidentStatusRequest request = new ChangeIncidentStatusRequest(newStatus);

        return mockMvc.perform(patch(
                        "/api/v1/organizations/{organizationId}/incidents/{incidentId}/status",
                        organizationId,
                        incidentId).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("SUCCESS"))
                .andExpect(jsonPath("$.data.status").value(newStatus.name()));
    }

    private UUID readDataId(MvcResult result) throws Exception {
        String json = result.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String id = jsonMapper.readTree(json)
                .path("data")
                .path("id")
                .asText();

        return UUID.fromString(id);
    }
}
