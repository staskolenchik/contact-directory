package by.kolenchik.core.jdbc;


import org.apache.log4j.Logger;

import java.sql.*;

public class ConnectionService {

    private static Logger log = Logger.getLogger(ConnectionService.class);

   public static java.sql.Connection getConnection() {

       Connection connection = null;

       try {
           connection = ConnTomcatJdbc.getConnection();
           log.debug("DB connection established");
       } catch (SQLException e) {
           log.error(e.getMessage());
       }
        return connection;
    }

    public static void rollback(java.sql.Connection connection) {

        try {
            connection.rollback();
            log.debug("DB connection rollback");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public static void close(java.sql.Connection connection) {

        try {
            connection.close();
            log.debug("DB connection close");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
