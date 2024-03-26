package edu.java.scrapper.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainerTest extends IntegrationTest {
    @Test
    public void isRunning() {
        assertTrue(POSTGRES.isRunning());
    }

    static Arguments[] tableNames() {
        return new Arguments[] {
            Arguments.of("chat"),
            Arguments.of("link"),
            Arguments.of("link_chat_assignment")
        };
    }

    @ParameterizedTest
    @MethodSource("tableNames")
    void tableExists(String tableName) throws SQLException {
        try (
            Connection connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
            );
            ResultSet rs = connection.getMetaData().getTables(
                connection.getCatalog(),
                "public",
                tableName,
                new String[] {"TABLE"}
            )
        ) {
            assertTrue(rs.next());
        }
    }
}
