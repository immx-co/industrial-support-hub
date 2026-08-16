DO
$$
DECLARE
v_organization_id UUID;
    v_department_id   UUID;
    v_admin_id        UUID;
    v_admin_role_id   UUID;
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM users user_account
                 JOIN user_roles user_role
                      ON user_role.user_id = user_account.id
                 JOIN roles role
                      ON role.id = user_role.role_id
        WHERE role.name = 'ROLE_ADMIN'
          AND user_account.enabled = TRUE
    )
    THEN
        INSERT INTO organizations (
            external_id,
            name
        )
        VALUES (
            'SYSTEM',
            'System organization'
        )
        ON CONFLICT (external_id)
            DO UPDATE SET name = EXCLUDED.name
                          RETURNING id INTO v_organization_id;

INSERT INTO departments (
    organization_id,
    external_id,
    name
)
VALUES (
           v_organization_id,
           'SYSTEM-ADMINISTRATION',
           'System administration'
       )
    ON CONFLICT (organization_id, external_id)
            DO UPDATE SET name = EXCLUDED.name
                       RETURNING id INTO v_department_id;

SELECT user_account.id
INTO v_admin_id
FROM users user_account
WHERE user_account.department_id = v_department_id
  AND LOWER(user_account.username) = 'admin';

IF v_admin_id IS NULL
        THEN
            INSERT INTO users (
                department_id,
                external_id,
                username,
                email,
                password_hash,
                first_name,
                last_name,
                enabled
            )
            VALUES (
                v_department_id,
                'SYSTEM-ADMIN',
                'admin',
                'admin@industrial-support.local',
                '$2a$12$NompzkuDkbSW8r5kbLTFDeFgcbRLw/iZAwmaPI9tyCaKnU37zwi.2',
                'System',
                'Administrator',
                TRUE
            )
            RETURNING id INTO v_admin_id;
END IF;

SELECT role.id
INTO v_admin_role_id
FROM roles role
WHERE role.name = 'ROLE_ADMIN';

INSERT INTO user_roles (
    user_id,
    role_id
)
VALUES (
           v_admin_id,
           v_admin_role_id
       )
    ON CONFLICT DO NOTHING;
END IF;
END
$$;