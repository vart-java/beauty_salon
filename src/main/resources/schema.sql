drop schema if exists bs cascade;
create schema if not exists bs;

create table if not exists bs.roles
(
    id           bigserial primary key,
    name         varchar   not null
);

create table if not exists bs.users
(
    id           bigserial primary key,
    login        varchar not null,
    password     varchar not null,
    rating       double precision,
    recall_count integer
);

create table if not exists bs.procedures
(
    id       bigserial primary key ,
    name     varchar not null,
    duration interval
);

create table if not exists bs.appointments
(
    id           bigserial primary key,
    procedure_id bigint    not null,
    master_id    bigint    not null,
    client_id    bigint    not null,
    start_time   timestamp not null,
    end_time     timestamp not null,
    status       varchar   not null
);

alter table bs.appointments
    add foreign key (master_id)
        references bs.users (id);

alter table bs.appointments
    add foreign key (client_id)
        references bs.users (id);

alter table bs.appointments
    add foreign key (procedure_id)
        references bs.procedures (id);

create table if not exists bs.users_procedures
(
    user_id      bigint not null,
    procedure_id bigint not null,
    primary key (user_id, procedure_id)
);
alter table bs.users_procedures
    add foreign key (user_id)
        references bs.users (id);

alter table bs.users_procedures
    add foreign key (procedure_id)
        references bs.procedures (id);

create table if not exists bs.users_roles
(
    user_id      bigint not null,
    role_id      bigint not null,
    primary key (user_id, role_id)
);

alter table bs.users_roles
    add foreign key (user_id)
        references bs.users (id);

alter table bs.users_roles
    add foreign key (role_id)
        references bs.roles (id);