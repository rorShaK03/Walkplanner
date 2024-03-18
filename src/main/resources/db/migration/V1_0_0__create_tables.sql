create table if not exists "users" (
    id            UUID,
    username      varchar(100) not null,
    email         varchar(100) not null,
    password_hash varchar(255) not null,
    created_at    timestamp    not null default now(),
    updated_at    timestamp,
    primary key (id)
);

create table if not exists "tracks" (
    id                   UUID,
    creator_id           UUID,
    name                 varchar(100) not null,
    description          varchar(500) not null,
    rating               int          not null,
    rated_users          int          not null,
    walked_users         int          not null,
    distance_meters      int          not null,
    walk_minutes         int          not null,
    created_at           timestamp    not null default now(),
    updated_at           timestamp,
    primary key (id)
);

create table if not exists "points" (
    id                   UUID,
    order_number          int               not null,
    latitude             double precision  not null,
    longitude            double precision  not null,
    created_at           timestamp         not null default now(),
    primary key (id)
);

create table if not exists "key_points" (
    id                   UUID,
    name                 varchar(100)      not null,
    description          varchar(500)      not null,
    latitude             double precision  not null,
    longitude            double precision  not null,
    created_at           timestamp         not null default now(),
    primary key (id)
);

create table if not exists "tracks_key_points" (
    track_id             UUID                not null,
    key_points_id        UUID                not null
);

create table if not exists "tracks_points" (
    track_id             UUID                not null,
    points_id            UUID                not null
);






