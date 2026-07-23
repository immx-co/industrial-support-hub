package com.immx.industrialsupport.supportservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Модель пользователя.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
public class User {

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
            name = "department_id",
            nullable = false
    )
    private Department department;

    @Column(
            name = "external_id",
            length = 100
    )
    @ToString.Include
    private String externalId;

    @Column(
            name = "username",
            nullable = false,
            length = 100
    )
    @ToString.Include
    private String username;

    @Column(
            name = "email",
            nullable = false,
            length = 255
    )
    private String email;

    @Column(
            name = "password_hash",
            nullable = false,
            length = 255
    )
    private String passwordHash;

    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    @ToString.Include
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    @ToString.Include
    private String lastName;

    @Column(
            name = "enabled",
            nullable = false
    )
    private boolean enabled = true;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    /**
     * ctor класса <code>User</code>.
     * @param department подразделение сотрудника
     * @param externalId внутренний идентификатор сотрудника
     * @param username имя пользователя сотрудника
     * @param email адрес электронной почты сотрудника
     * @param passwordHash хэшированный пароль сотрудника
     * @param firstName имя сотрудника
     * @param lastName фамилия сотрудника
     */
    public User(Department department,
                String externalId,
                String username,
                String email,
                String passwordHash,
                String firstName,
                String lastName) {
        this.department = department;
        this.externalId = externalId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
