CREATE TABLE users (
    id          BIGSERIAL       NOT NULL,
    username    VARCHAR(100)    NOT NULL,
    email       VARCHAR(255)    NOT NULL,
    created_at  TIMESTAMPTZ     NOT NULL,
    CONSTRAINT pk_users          PRIMARY KEY (id),
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email    UNIQUE (email)
);