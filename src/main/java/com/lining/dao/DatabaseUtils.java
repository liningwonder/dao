package com.lining.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * description:
 * date 2018-03-01
 *
 * @author lining1
 * @version 1.0.0
 */
public class DatabaseUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);

    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private static final String URL = "jdbc:mysql://localhost:3306/lining?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "123456";

    private static final BasicDataSource DATA_SOURCE;

    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    static {
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
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

}
