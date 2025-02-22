package ru.CarX.TechnicalTask.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.cognitor.cassandra.migration.spring.CassandraMigrationAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.cassandra.config.DefaultCqlBeanNames;

import java.net.InetSocketAddress;

@Configuration
@Profile("cassandraMigration")
@PropertySources({
        @PropertySource("classpath:application-cassandra-migration.properties"),
        @PropertySource("classpath:application-cassandra.properties")
})
public class CassandraMigrationConfig {

    @Value("${spring.data.cassandra.contact-points}")
    private String cassandraHost;

    // создаем сессию cassandra для миграции
    @Bean(CassandraMigrationAutoConfiguration.CQL_SESSION_BEAN_NAME)
    public CqlSession cassandraMigrationCqlSession() {
        return CqlSession.builder()
                .withKeyspace("user_data_keyspace")
                .addContactPoint(new InetSocketAddress(cassandraHost, 9042))
                .withLocalDatacenter("datacenter1")
                .build();
    }

    @Bean(DefaultCqlBeanNames.SESSION)
    @Primary
    // ensure that the keyspace is created if needed before initializing spring-data session
    // создаем дополнительную сессию для spring data cassandra, для избежания конфликта бинов
    @DependsOn(CassandraMigrationAutoConfiguration.MIGRATION_TASK_BEAN_NAME)
    public CqlSession cassandraSession(CqlSessionBuilder cqlSessionBuilder) {
        return cqlSessionBuilder.build();
    }
}