package com.immx.industrialsupport.supportservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    @ToString.Include
    private UUID id;

    @Column(
            name = "externalId",
            length = 100,
            unique = true
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
            name = "createdAt",
            nullable = false,
            updatable = false
    )
    @ToString.Include
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(
            name = "updatedAt",
            nullable = false
    )
    @ToString.Include
    private OffsetDateTime updatedAt;

    public Organization(String externalId, String name) {
        this.externalId = externalId;
        this.name = name;
    }
}
