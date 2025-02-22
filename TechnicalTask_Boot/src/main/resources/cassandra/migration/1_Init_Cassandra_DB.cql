create table user_activity_data
(
    uuid      uuid,
    timestamp timestamp,
    activity  int,
    primary key (uuid, timestamp)
);

insert into user_activity_data(uuid, timestamp, activity)
    VALUES (123e4567-e89b-12d3-a456-556642440001, '2023-12-04 21:24:00.000000', 23)