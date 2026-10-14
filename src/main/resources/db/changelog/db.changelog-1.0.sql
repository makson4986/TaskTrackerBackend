--liquibase formatted sql

--changeset makson:1

CREATE TYPE task_status AS ENUM ('NOT_DONE', 'IN_PROGRESS', 'DONE');

--rollback DROP TYPE task_status;

--changeset makson:2

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL
);
--rollback DROP TABLE users;

--changeset makson:3
CREATE TABLE tasks
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(256) NOT NULL,
    text         TEXT         NOT NULL,
    owner        INT          NOT NULL,
    status       task_status DEFAULT 'NOT_DONE',
    created_at   DATE,
    performed_at DATE,
    FOREIGN KEY (owner) REFERENCES users (id)
);
--rollback DROP TABLE tasks;
