ALTER TABLE users
DROP CONSTRAINT IF EXISTS uq_users_external_id;

DROP INDEX IF EXISTS uq_users_username_lower;

DROP INDEX IF EXISTS uq_users_email_lower;

CREATE UNIQUE INDEX uq_users_department_username_lower
    ON users (department_id, LOWER(username));