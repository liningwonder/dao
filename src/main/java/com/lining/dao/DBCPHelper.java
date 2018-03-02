package com.lining.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * description:
 * date 2018-03-01
 *
 * @author lining1
 * @version 1.0.0
 */
public class DBCPHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBCPHelper.class);

    private static final String DRIVER ;
    private static final String URL ;
    private static final String USERNAME ;
    private static final String PASSWORD ;

    private static final BasicDataSource DATA_SOURCE;
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    static {
        Properties properties = PropsUtil.loadProps("config.properties");
        DRIVER = properties.getProperty("jdbc.driver");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.username");
        PASSWORD = properties.getProperty("jdbc.password");
        LOGGER.info(DRIVER + URL + USERNAME + PASSWORD);

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);

        CONNECTION_HOLDER = new ThreadLocal<Connection>();
    }

    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    public static void main(String[] args) {
        LOGGER.info(DBCPHelper.getConnection().toString());
    }
}
