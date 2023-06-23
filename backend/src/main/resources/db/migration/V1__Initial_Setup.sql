CREATE TABLE IF NOT EXISTS sender
(
    id    BIGSERIAL PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS taker(
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    age INT,
    gender VARCHAR(6) NOT NULL default 'MALE',
    sender_id BIGINT
);

ALTER TABLE taker
    ADD CONSTRAINT fk_taker_sender
        FOREIGN KEY (sender_id) REFERENCES sender(id);
