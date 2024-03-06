create table vehicle
(
    battery_nominal_capacity double precision,
    charging_power           double precision,
    current_battery_charge   double precision,
    vehicle_consumption      double precision,
    id                       bigserial primary key,
    charge_port_type         varchar(255),
    model                    varchar(255) unique
);

create table delivery_path_charging_stations
(
    charging_station_id bigint not null constraint fkd2ntlvttjds2io5gol9f021vb references charging_station,
    delivery_path_id    bigint not null constraint fk171e39d3ue1wu5nua5pkotqjw references delivery_path,
    primary key (charging_station_id, delivery_path_id)
);

create table delivery_path
(
    distance         double precision,
    duration         double precision,
    id               bigserial primary key,
    encoded_polyline varchar(255),
    end_point        bytea,
    path             bytea,
    start_point      bytea
);

create table delivery_man
(
    id bigserial primary key,
    vehicle_id bigint unique constraint fkido13ara2m0u5p5bj654cqc2w references vehicle
);

create table delivery
(
    delivery_man     bigint constraint fk7eav2b9pe8f748ef7d9c72hjh references delivery_man,
    delivery_path_id bigint unique constraint fk9y3eml7rtca5to5sxyp81nyb1 references delivery_path,
    id               bigserial primary key
);

create table charging_station_charging_ports
(
    charging_port_id    bigint not null constraint fk377uv9jatcmfebvf58922fo90 references charging_port,
    charging_station_id bigint not null constraint fkigehudulkcci5tq7mh9k1xh1l references charging_station,
    primary key (charging_port_id, charging_station_id)
);

create table charging_station
(
    id       bigserial primary key,
    location bytea
);

create table charging_port
(
    charging_power double precision,
    id             bigserial primary key,
    type           varchar(255)
);
