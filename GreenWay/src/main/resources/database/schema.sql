create table delivery_man
(
    id       bigserial primary key,
    username varchar(255) not null unique
);

create table vehicle
(
    battery_nominal_capacity double precision,
    current_battery_charge   double precision,
    vehicle_consumption      double precision,
    created_at               timestamp(6),
    id                       bigserial primary key,
    updated_at               timestamp(6),
    created_by               varchar(255),
    model                    varchar(255),
    updated_by               varchar(255)
);

create table delivery
(
    created_at       timestamp(6),
    delivery_man_id  bigint unique constraint fk6pnmflviqbuwdahnt235e3aqi references delivery_man,
    id               bigserial primary key,
    updated_at       timestamp(6),
    vehicle_id       bigint unique constraint fkr85isw6qn62sn3flu0ycx3dav references vehicle,
    created_by       varchar(255),
    updated_by       varchar(255)
);

create table delivery_package
(
    weight      double precision,
    created_at  timestamp(6),
    delivery_id bigint constraint fkf7jb0pxgy1qrxk2m5iv3y7vtb references delivery,
    id          bigserial primary key,
    updated_at  timestamp(6),
    created_by  varchar(255),
    updated_by  varchar(255),
    destination geography
);
