CREATE KEYSPACE IF NOT EXISTS user_data_keyspace WITH replication = { 
    'class': 'SimpleStrategy', 'replication_factor': 1
};

