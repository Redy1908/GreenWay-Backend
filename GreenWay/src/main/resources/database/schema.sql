create table delivery_men
(
    id       bigserial primary key,
    username varchar(255) not null unique
);

create table vehicles
(
    id              bigserial primary key,
    model_name      varchar(255)     not null,
    max_autonomy_km double precision not null,
    max_capacity_kg double precision not null,
    created_at      timestamp(6),
    updated_at      timestamp(6),
    created_by      varchar(255),
    updated_by      varchar(255)
);

create table deliveries
(
    id                      bigserial primary key,
    deposit_address         varchar(255) not null,
    deposit_coordinates     geometry     not null,
    delivery_man_id         bigint constraint fkjubqdj7mnhpndfgdrjlyns8by references delivery_men,
    vehicle_id              bigint constraint fkgjj47cndyarbxrqimqu8q16n8 references vehicles,
    estimated_delivery_date date         not null,
    created_at              timestamp(6),
    updated_at              timestamp(6),
    created_by              varchar(255),
    updated_by              varchar(255)
);

create table delivery_packages
(
    id                   bigserial primary key,
    sender               varchar(255)     not null,
    sender_address       varchar(255)     not null,
    receiver             varchar(255)     not null,
    receiver_address     varchar(255)     not null,
    receiver_coordinates geography,
    weight               double precision not null,
    delivery_id          bigint constraint fk8u1il8kpkdit8glq10h206h9s references deliveries,
    created_at           timestamp(6),
    updated_at           timestamp(6),
    created_by           varchar(255),
    updated_by           varchar(255)
);
