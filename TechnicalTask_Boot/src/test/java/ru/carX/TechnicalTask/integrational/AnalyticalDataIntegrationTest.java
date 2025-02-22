package ru.carX.TechnicalTask.integrational;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.CarX.TechnicalTask.TechnicalTaskBootApplication;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.profiles.active="
}, classes = {TechnicalTaskBootApplication.class})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@PropertySource("classpath:application-test.properties")
public class AnalyticalDataIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:13.2")
                    .withInitScript("testcontainers-migration/postgres/schema.sql");

    @Container
    @SuppressWarnings("resource")
    private static final CassandraContainer<?> cassandraContainer =
            new CassandraContainer<>("cassandra:4.0.15")
                    .withExposedPorts(9042)
                    .withInitScript("testcontainers-migration/cassandra/schema.cql");

    @DynamicPropertySource
    static void configureCassandraProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.cassandra.contact-points",
                () -> cassandraContainer.getHost() + ":" + cassandraContainer.getMappedPort(9042));
        registry.add("spring.data.cassandra.keyspace-name", () -> "user_data_keyspace");
        registry.add("spring.data.cassandra.local-datacenter", () -> "datacenter1");
        registry.add("spring.data.cassandra.username", cassandraContainer::getUsername);
        registry.add("spring.data.cassandra.password", cassandraContainer::getPassword);
    }

    @DynamicPropertySource
    static void configurePostgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @Test
    @Order(1)
    void getUsersMoneyTop_ShouldReturnMap() {
        try {
            String expected = "{\"BR\":[{\"uuid\":\"550e8400-e29b-41d4-a716-446655440007\",\"money\":412,\"country\":\"BR\",\"createdAt\":\"2023-08-18T16:20:30\"}],\"RU\":[{\"uuid\":\"550e8400-e29b-41d4-a716-44665544000a\",\"money\":291,\"country\":\"RU\",\"createdAt\":\"2023-04-05T10:10:10\"},{\"uuid\":\"550e8400-e29b-41d4-a716-446655440005\",\"money\":112,\"country\":\"RU\",\"createdAt\":\"2025-09-14T03:07:55\"}],\"JP\":[{\"uuid\":\"550e8400-e29b-41d4-a716-446655440006\",\"money\":631,\"country\":\"JP\",\"createdAt\":\"2024-12-01T12:00:00\"}],\"IT\":[{\"uuid\":\"550e8400-e29b-41d4-a716-44665544000b\",\"money\":456,\"country\":\"IT\",\"createdAt\":\"2025-03-20T17:30:00\"}],\"GB\":[{\"uuid\":\"550e8400-e29b-41d4-a716-446655440004\",\"money\":385,\"country\":\"GB\",\"createdAt\":\"2024-07-30T22:37:45\"}],\"CN\":[{\"uuid\":\"550e8400-e29b-41d4-a716-446655440009\",\"money\":788,\"country\":\"CN\",\"createdAt\":\"2024-10-31T20:15:00\"}],\"FR\":[{\"uuid\":\"550e8400-e29b-41d4-a716-446655440003\",\"money\":919,\"country\":\"FR\",\"createdAt\":\"2023-05-22T18:05:10\"}],\"US\":[{\"uuid\":\"550e8400-e29b-41d4-a716-446655440000\",\"money\":754,\"country\":\"US\",\"createdAt\":\"2024-03-15T08:24:00\"},{\"uuid\":\"550e8400-e29b-41d4-a716-446655440002\",\"money\":532,\"country\":\"US\",\"createdAt\":\"2025-01-10T09:15:20\"}],\"NL\":[{\"uuid\":\"550e8400-e29b-41d4-a716-44665544000e\",\"money\":879,\"country\":\"NL\",\"createdAt\":\"2025-08-11T21:45:33\"}],\"ES\":[{\"uuid\":\"550e8400-e29b-41d4-a716-44665544000c\",\"money\":674,\"country\":\"ES\",\"createdAt\":\"2024-06-12T14:55:22\"}],\"CA\":[{\"uuid\":\"550e8400-e29b-41d4-a716-446655440001\",\"money\":267,\"country\":\"CA\",\"createdAt\":\"2023-11-02T14:45:30\"}],\"KZ\":[{\"uuid\":\"550e8400-e29b-41d4-a716-446655440008\",\"money\":540,\"country\":\"KZ\",\"createdAt\":\"2025-02-25T11:40:25\"},{\"uuid\":\"550e8400-e29b-41d4-a716-44665544000d\",\"money\":305,\"country\":\"KZ\",\"createdAt\":\"2023-12-20T09:00:00\"}]}";

            MvcResult result = mockMvc.perform(get("/analytics/moneyTopPerCountry?usersAmount=10"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(result.getResponse().getContentAsString())
                    .isEqualTo(expected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void getNewUsersCount_ShouldReturnList() {
        try {
            LocalDateTime toTime = LocalDateTime.of(2025, 1, 22, 20, 20);
            LocalDateTime fromTime = toTime.minusMonths(2);
            String expected = "{\"JP\":1,\"US\":1}";

            MvcResult result = mockMvc.perform(get("/analytics/newUsers?fromTime=" +
                            fromTime + "&toTime=" + toTime))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(result.getResponse().getContentAsString())
                    .isEqualTo(expected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    void getActivityByPeriod_ShouldReturnUserActivities() {
        try {
            UUID uuid = UUID.fromString("70e9f5a6-8f12-48d1-a2ea-4e8d481f0001");
            LocalDateTime toTime = LocalDateTime.of(2025, 1, 22, 20, 20);
            LocalDateTime fromTime = toTime.minusDays(3);
            String expected = "[{\"activity\":55,\"timestamp\":\"2025-01-20T13:00:00\"},{\"activity\":36,\"timestamp\":\"2025-01-20T13:05:00\"},{\"activity\":12,\"timestamp\":\"2025-01-20T13:10:00\"}]";

            MvcResult result = mockMvc.perform(get("/analytics/userActivity/" +
                            uuid + "?fromTime=" + fromTime + "&toTime=" + toTime))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(result.getResponse().getContentAsString())
                    .isEqualTo(expected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}