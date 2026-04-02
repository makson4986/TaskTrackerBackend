--liquibase formatted sql

--changeset makson:1

CREATE TABLE users
(
    id       INT PRIMARY KEY,
    email    VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL
);
--rollback DROP TABLE users;

--changeset makson:2
CREATE TABLE tasks
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(256) NOT NULL,
    text         TEXT         NOT NULL,
    owner        INT          NOT NULL,
    status       VARCHAR(32)  NOT NULL DEFAULT 'NOT_DONE',
    performed_at TIMESTAMP,
    FOREIGN KEY (owner) REFERENCES users (id)
);
--rollback DROP TABLE tasks;
