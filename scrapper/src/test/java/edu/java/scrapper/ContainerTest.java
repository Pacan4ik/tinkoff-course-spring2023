package edu.java.scrapper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainerTest extends IntegrationTest {

    private static final Connection CONNECTION;

    static {
        try {
            CONNECTION =
                DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void isRunning() {
        assertTrue(POSTGRES.isRunning());
    }

    @Test
    public void chatsTableExists() throws SQLException {
        //given
        DatabaseMetaData metaData = CONNECTION.getMetaData();

        //when
        ResultSet rs = metaData.getTables(
            CONNECTION.getCatalog(),
            null,
            "chat",
            new String[] {"TABLE"}
        );

        //then
        assertTrue(rs.next());
        rs.close();

    }

    @Test
    public void linksTableExists() throws SQLException {
        //given
        DatabaseMetaData metaData = CONNECTION.getMetaData();

        //when
        ResultSet rs = metaData.getTables(
            CONNECTION.getCatalog(),
            null,
            "link",
            new String[] {"TABLE"}
        );

        //then
        assertTrue(rs.next());
        rs.close();

    }

    @Test
    public void linkChatAssignmentTableExists() throws SQLException {
        //given
        DatabaseMetaData metaData = CONNECTION.getMetaData();

        //when
        ResultSet rs =
            metaData.getTables(
                CONNECTION.getCatalog(),
                null,
                "link_chat_assignment",
                new String[] {"TABLE"}
            );

        //then
        assertTrue(rs.next());
        rs.close();

    }

}
