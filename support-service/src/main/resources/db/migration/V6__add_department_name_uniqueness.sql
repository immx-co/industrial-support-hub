CREATE UNIQUE INDEX uq_departments_organization_name_lower
    ON departments (
                    organization_id,
                    LOWER(BTRIM(name))
        );