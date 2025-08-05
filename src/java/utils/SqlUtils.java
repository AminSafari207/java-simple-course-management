package utils;

import config.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtils {
    public static void truncateTables(String... tables) {
        String sqlQuery = "truncate table ";

        for (int i = 0; i < tables.length; i++) {
            sqlQuery += (tables[i]);
            if (i < tables.length - 1) sqlQuery += (", ");
        }

        sqlQuery += (" restart identity cascade");

        try (
                Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Truncate error: " + e);
        }
    }
}
