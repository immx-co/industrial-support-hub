package com.immx.industrialsupport.supportservice.entities;

import com.immx.industrialsupport.supportservice.dto.incident.IncidentPriority;
import com.immx.industrialsupport.supportservice.dto.incident.IncidentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Модель обращения.
 */
@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    @ToString.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organization_id",
            nullable = false
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "department_id",
            nullable = false
    )
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reporter_id",
            nullable = false
    )
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_engineer_id")
    @ToString.Include
    private User assignedEngineer;

    @ToString.Include
    private String title;

    @ToString.Include
    private String description;

    @Enumerated(EnumType.STRING)
    @ToString.Include
    private IncidentPriority priority;

    @Enumerated(EnumType.STRING)
    @ToString.Include
    private IncidentStatus status;

    @ToString.Include
    private OffsetDateTime slaDeadline;

    @ToString.Include
    private OffsetDateTime resolvedAt;

    @ToString.Include
    private OffsetDateTime closedAt;

    @CreationTimestamp
    @ToString.Include
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @ToString.Include
    private OffsetDateTime updatedAt;

    @Version
    @ToString.Include
    private Long version;

    /**
     * ctor класса <code>Incident</code>.
     * @param organization организация
     * @param department подразделение организации
     * @param reporter пользователь, создавший обращение
     * @param title оглавление обращения
     * @param description описание обращения
     * @param priority приоритет обращения
     * @param slaDeadline срок выполнения обращения
     */
    public Incident(Organization organization,
                    Department department,
                    User reporter,
                    String title,
                    String description,
                    IncidentPriority priority,
                    OffsetDateTime slaDeadline) {
        this.organization = organization;
        this.department = department;
        this.reporter = reporter;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.slaDeadline = slaDeadline;
        this.status = IncidentStatus.NEW;
    }
}
