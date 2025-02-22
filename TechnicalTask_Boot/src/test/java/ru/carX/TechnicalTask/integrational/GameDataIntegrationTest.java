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
import org.springframework.http.MediaType;
import ru.CarX.TechnicalTask.TechnicalTaskBootApplication;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.profiles.active="
}, classes = {TechnicalTaskBootApplication.class})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@PropertySource("classpath:application-test.properties")
public class GameDataIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:13.2");

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
    void receiveSyncData_ShouldReturnOk() {
        String userSyncDataJson =
                        "{\n" +
                        "  \"uuid\": \"123e4567-e89b-12d3-a456-556642440000\",\n" +
                        "  \"money\": 100,\n" +
                        "  \"country\": \"RU\",\n" +
                        "  \"createdAt\": \"2023-12-04T21:24:00\"\n" +
                        "}";

        try {
            mockMvc.perform(post("/api/sync")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userSyncDataJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Data received"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    void getUserData_ShouldReturnUserSyncDataDTO() throws Exception {
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        String expected = "{\"uuid\":\"123e4567-e89b-12d3-a456-556642440000\",\"money\":100,\"country\":\"RU\",\"createdAt\":\"2023-12-04T21:24:00\"}";

        MvcResult result = mockMvc.perform(get("/api/sync/" + uuid))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(expected);
    }

    @Test
    @Order(3)
    void receiveActivityData_ShouldReturnOk() {
        String userActivityDataJson =
                        "{\n" +
                        "  \"uuid\": \"123e4567-e89b-12d3-a456-556642440000\",\n" +
                        "  \"timestamp\": \"2023-12-04T21:24:00\",\n" +
                        "  \"activity\": 100\n" +
                        "}";

        try {
            mockMvc.perform(post("/api/stats")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userActivityDataJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Activity recorded"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
