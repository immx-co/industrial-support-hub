ALTER TABLE departments
DROP CONSTRAINT fk_departments_organization;

ALTER TABLE departments
    ADD CONSTRAINT fk_departments_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE;

ALTER TABLE users
DROP CONSTRAINT fk_users_department;

ALTER TABLE users
    ADD CONSTRAINT fk_users_department
        FOREIGN KEY (department_id)
            REFERENCES departments (id)
            ON DELETE CASCADE;