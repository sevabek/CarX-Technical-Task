CREATE KEYSPACE IF NOT EXISTS user_data_keyspace
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE user_data_keyspace;

create table if not exists user_data_keyspace.user_activity_data
(
    uuid      uuid,
    timestamp timestamp,
    activity  int,
    primary key (uuid, timestamp)
);

INSERT INTO user_activity_data (uuid, timestamp, activity)
VALUES (70e9f5a6-8f12-48d1-a2ea-4e8d481f0001, '2025-01-20 13:00:00+03:00', 55);

INSERT INTO user_activity_data (uuid, timestamp, activity)
VALUES (70e9f5a6-8f12-48d1-a2ea-4e8d481f0001, '2025-01-20 13:05:00+03:00', 36);

INSERT INTO user_activity_data (uuid, timestamp, activity)
VALUES (70e9f5a6-8f12-48d1-a2ea-4e8d481f0001, '2025-01-20 13:10:00+03:00', 12);

INSERT INTO user_activity_data (uuid, timestamp, activity)
VALUES (83d1f2c9-9b34-47e8-a4cd-7f4e98e00002, '2025-01-20 13:30:00+03:00', 51);

INSERT INTO user_activity_data (uuid, timestamp, activity)
VALUES (83d1f2c9-9b34-47e8-a4cd-7f4e98e00002, '2025-01-20 13:35:00+03:00', 16);