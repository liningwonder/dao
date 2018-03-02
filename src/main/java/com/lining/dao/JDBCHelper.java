package com.lining.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

/**
 * description:
 * date 2018-03-02
 *
 * @author lining1
 * @version 1.0.0
 */
public class JDBCHelper {

    private static final Logger logger = LoggerFactory.getLogger(JDBCHelper.class);

    private static final String DRIVER ;
    private static final String URL ;
    private static final String USERNAME ;
    private static final String PASSWORD ;

    static {
        Properties properties = PropsUtil.loadProps("config.properties");
        DRIVER = properties.getProperty("jdbc.driver");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.username");
        PASSWORD = properties.getProperty("jdbc.password");
    }

    private static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            logger.warn("get connection error");
        }
        return conn;
    }

    public static User getUser(int id) {
        Connection conn = getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        User user = new User();
        try {
            statement = conn.prepareStatement("select id , email, name from user where id = ?");
            statement.setInt(1, id);
            rs = statement.executeQuery();
            while (rs.next()) {
                int roleId = rs.getInt("id");
                String email = rs.getString("email");
                String name = rs.getString("name");
                user.setId(roleId);
                user.setEmail(email);
                user.setName(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("query error");
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {rs.close();}
                if (statement !=null && !statement.isClosed()) {statement.close();}
                if (conn != null && !conn.isClosed()) {conn.close();}
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public static boolean insertUser(User user) {
        boolean result = false;
        Connection conn = getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.prepareStatement("insert into user(id , email, name) VALUES(?,?,?) ");
            statement.setInt(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getName());
            int i = statement.executeUpdate();
            if (i != 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("insert error");
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {rs.close();}
                if (statement !=null && !statement.isClosed()) {statement.close();}
                if (conn != null && !conn.isClosed()) {conn.close();}
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("close connection error");
            }
        }
        return result;
    }

    public static boolean updateUser(User user) {
        boolean result = false;
        Connection conn = getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.prepareStatement("update user set email=?,name = ? where id=? ");
            statement.setInt(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getName());
            int row = statement.executeUpdate();
            if (row != 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("update error");
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {rs.close();}
                if (statement !=null && !statement.isClosed()) {statement.close();}
                if (conn != null && !conn.isClosed()) {conn.close();}
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("close connection error");
            }
        }
        return result;
    }

    public static boolean deleteUser(int id) {
        boolean result = false;
        Connection conn = getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.prepareStatement("delete from user where id=? ");
            statement.setInt(1, id);
            int row = statement.executeUpdate();
            if (row != 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("delete error");
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {rs.close();}
                if (statement !=null && !statement.isClosed()) {statement.close();}
                if (conn != null && !conn.isClosed()) {conn.close();}
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("close connection error");
            }
        }
        return result;
    }

    public static void main (String[] args) {
        User user = new User(1, "liningwonder@163.com", "lining");
        User user2 = new User(2, "new@163.com","new");
        JDBCHelper.insertUser(user);
        JDBCHelper.insertUser(user2);

        logger.info(getUser(user.getId()).toString());
        logger.info(getUser(user2.getId()).toString());



        User newUser = new User(1, "haha@153.com", "haha");
        updateUser(newUser);
        logger.info(getUser(newUser.getId()).toString());

        JDBCHelper.deleteUser(newUser.getId());
    }
}
