CREATE TABLE roles
(
    id   UUID        NOT NULL DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,

    CONSTRAINT pk_roles
        PRIMARY KEY (id),

    CONSTRAINT uq_roles_name
        UNIQUE (name)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,

    CONSTRAINT pk_user_roles
        PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE RESTRICT
);

CREATE INDEX idx_user_roles_role_id
    ON user_roles (role_id);

INSERT INTO roles (name)
VALUES ('ROLE_EMPLOYEE'),
       ('ROLE_DISPATCHER'),
       ('ROLE_ENGINEER'),
       ('ROLE_MANAGER'),
       ('ROLE_ADMIN'),
       ('ROLE_ROBOT');