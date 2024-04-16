create table deliveries
(
    id                      serial primary key,
    created_at              timestamp(6),
    created_by              varchar(255),
    updated_at              timestamp(6),
    updated_by              varchar(255),
    deposit_address         varchar(255) not null,
    deposit_coordinates     geometry     not null,
    estimated_delivery_date date,
    receiver                varchar(255) not null,
    receiver_address        varchar(255) not null,
    receiver_coordinates    geography,
    sender                  varchar(255) not null,
    sender_address          varchar(255) not null,
    weight_kg               integer      not null,
    vehicle_id              integer      constraint fkgjj47cndyarbxrqimqu8q16n8 references vehicles,
    deliveries_order        integer
);

create table delivery_men
(
    id                  bigserial primary key,
    username            varchar(255) not null constraint uk_mdmis23uvd7ma04egeq65i3cw unique,
    delivery_vehicle_id integer constraint uk_hm2urbq6fuxymhkct0qj7hcef unique constraint fkbre6fb6j0p867w6k7w46wo539 references vehicles
);

create table vehicles
(
    id                  serial primary key,
    created_at          timestamp(6),
    created_by          varchar(255),
    updated_at          timestamp(6),
    updated_by          varchar(255),
    deposit_address     varchar(255) not null,
    deposit_coordinates geography    not null,
    max_autonomy_km     integer      not null,
    max_capacity_kg     integer      not null,
    model_name          varchar(255) not null
);