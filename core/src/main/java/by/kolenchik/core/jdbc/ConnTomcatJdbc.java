package by.kolenchik.core.jdbc;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnTomcatJdbc {

    private static DataSource dataSource;

    static {
        setupConnection();

    }


    public static Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        return connection;
    }

    public static void setupConnection() {

        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://localhost:3307/contacts");
        p.setDriverClassName("com.mysql.cj.jdbc.Driver");
        p.setUsername("root");
        p.setPassword("root");
        p.setDefaultAutoCommit(true);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

        Properties properties = new Properties();
        properties.put("characterEncoding", "utf-8");
        properties.put("serverTimezone", "Europe/Helsinki");
        properties.put("useSSL", "false");
        p.setDbProperties(properties);

        dataSource = new DataSource();
        dataSource.setPoolProperties(p);

    }
}
