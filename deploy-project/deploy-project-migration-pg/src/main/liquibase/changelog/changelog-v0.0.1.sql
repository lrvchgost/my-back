--liquibase formatted sql

--changeset r.lonskiy:1 labels:v0.0.1
CREATE TYPE "payment_types_type" AS ENUM ('free', 'prepaid', 'license');
CREATE TYPE "speed_type" AS ENUM ('100', '150', '200');

CREATE TABLE "storages" (
    "id" text primary key constraint storages_id_length_ctr check (length("id") < 64),
    "title" text constraint storages_title_length_ctr check (length(title) < 128),
    "description" text constraint storages_description_length_ctr check (length(title) < 4096),
    "payment_type" payment_types_type not null,
    "read_speed" speed_type not null,
    "write_speed" speed_type not null,
    "optimize_enabled" boolean default false,
    "capacity" text constraint storages_capacity_length_ctr check (length(title) < 128),
    "availability" text constraint storages_availability_length_ctr check (length(title) < 128),
    "lock" text not null constraint storages_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX storages_payment_type_idx on "storages" using hash ("payment_type");
CREATE INDEX storages_read_speed_idx on "storages" using hash ("read_speed");
CREATE INDEX storages_write_speed_idx on "storages" using hash ("write_speed");
CREATE INDEX storages_capacity_idx on "storages" using hash ("capacity");
CREATE INDEX storages_availability_idx on "storages" using hash ("availability");
