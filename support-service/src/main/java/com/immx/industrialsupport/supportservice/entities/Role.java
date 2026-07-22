package com.immx.industrialsupport.supportservice.entities;

import com.immx.industrialsupport.supportservice.dto.role.RoleName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Модель роли сотрудника.
 */
@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "name",
            nullable = false,
            unique = true,
            length = 50
    )
    private RoleName name;
}
