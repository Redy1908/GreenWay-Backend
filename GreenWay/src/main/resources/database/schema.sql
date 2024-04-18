CREATE TABLE vehicle_deposit
(
    id                  SERIAL PRIMARY KEY,
    deposit_address     VARCHAR(255) NOT NULL,
    deposit_coordinates GEOMETRY NOT NULL
);

CREATE TABLE delivery_vehicles
(
    id              SERIAL PRIMARY KEY,
    created_at      TIMESTAMP(6),
    created_by      VARCHAR(255),
    updated_at      TIMESTAMP(6),
    updated_by      VARCHAR(255),
    current_load_kg INTEGER NOT NULL,
    max_autonomy_km INTEGER NOT NULL,
    max_capacity_kg INTEGER NOT NULL,
    model_name      VARCHAR(255) NOT NULL
);

CREATE TABLE deliveries
(
    id                      SERIAL PRIMARY KEY,
    created_at              TIMESTAMP(6),
    created_by              VARCHAR(255),
    updated_at              TIMESTAMP(6),
    updated_by              VARCHAR(255),
    estimated_delivery_date DATE,
    receiver                VARCHAR(255) NOT NULL,
    receiver_address        VARCHAR(255) NOT NULL,
    receiver_coordinates    GEOGRAPHY,
    sender                  VARCHAR(255) NOT NULL,
    sender_address          VARCHAR(255) NOT NULL,
    weight_kg               INTEGER NOT NULL,
    vehicle_id              INTEGER,
    deliveries_order        INTEGER,
    CONSTRAINT deliveries_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES delivery_vehicles(id)
);

CREATE TABLE delivery_men
(
    id                  BIGSERIAL PRIMARY KEY,
    username            VARCHAR(255) NOT NULL,
    delivery_vehicle_id INTEGER,
    CONSTRAINT delivery_men_username_key UNIQUE (username),
    CONSTRAINT delivery_men_vehicle_id_key UNIQUE (delivery_vehicle_id),
    CONSTRAINT delivery_men_vehicle_id_fkey FOREIGN KEY (delivery_vehicle_id) REFERENCES delivery_vehicles(id)
);