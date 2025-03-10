package ru.CarX.TechnicalTask.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("cassandra")
@PropertySource("classpath:application-cassandra.properties")
public class CassandraConfig {
}
