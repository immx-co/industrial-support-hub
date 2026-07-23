package com.immx.industrialsupport.supportservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Модель подразделения организации.
 */
@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    @ToString.Include
    private UUID id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "organization_id",
            nullable = false
    )
    private Organization organization;

    @Column(
            name = "external_id",
            length = 100
    )
    @ToString.Include
    private String externalId;

    @Column(
            name = "name",
            nullable = false,
            length = 255
    )
    @ToString.Include
    private String name;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    @ToString.Include
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    /**
     * ctor класса <code>Department</code>.
     * @param organization организация подразделения
     * @param externalId внутренний идентификатор подразделения организации
     * @param name название подразделения организации
     */
    public Department(Organization organization,
                      String externalId,
                      String name) {
        this.organization = organization;
        this.externalId = externalId;
        this.name = name;
    }
}
