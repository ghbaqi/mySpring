package com.bcc.helper;

import com.bcc.util.ConfigConstant;
import com.bcc.util.PropsUtil;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public final class DatabaseHelper {

    private static final Logger      LOGGER       = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final BasicDataSource BASIC_DATA_SOURCE;

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();


    static {
        Properties conf = PropsUtil.loadProps("smart.properties");
        DRIVER = conf.getProperty(ConfigConstant.JDBC_DRIVER);
        URL = conf.getProperty(ConfigConstant.JDBC_URL);
        USERNAME = conf.getProperty(ConfigConstant.JDBC_USERNAME);
        PASSWORD = conf.getProperty(ConfigConstant.JDBC_PASSWORD);
        BASIC_DATA_SOURCE = new BasicDataSource();
        BASIC_DATA_SOURCE.setDriverClassName(DRIVER);
        BASIC_DATA_SOURCE.setUrl(URL);
        BASIC_DATA_SOURCE.setUsername(USERNAME);
        BASIC_DATA_SOURCE.setPassword(PASSWORD);

        //        try {
        //            Class.forName("com.mysql.jdbc.Driver");
        //        } catch (Exception e) {
        //            LOGGER.error("数据库驱动错误", e);
        //        }
    }

    private DatabaseHelper() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection == null) {
            connection = BASIC_DATA_SOURCE.getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            threadLocal.remove();
        } else {
            LOGGER.error("连接为 null");
        }
    }

    public static <T> List<T> getEntityList(Class<T> clazz, String sql, Object... params) {
        List<T> list = null;
        Connection connection = null;
        try {
            connection = getConnection();
            list = QUERY_RUNNER.query(connection, sql, new BeanListHandler<>(clazz), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static int insert(String sql, Object... params) {
        Connection connection = null;
        Object[] insert;
        try {
            connection = getConnection();
            insert = QUERY_RUNNER.insert(connection, sql, new ArrayHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("getConnection 异常 ", e);
            throw new RuntimeException("getConnection 异常");
        }
        return insert.length;
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection connection;
        try {
            connection = getConnection();
        } catch (SQLException e) {
            LOGGER.error("getConnection 异常 ", e);
            throw new RuntimeException("getConnection 异常");
        }
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("connection.setAutoCommit 异常 ", e);
                throw new RuntimeException("connection.setAutoCommit 异常");
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection connection;
        try {
            connection = getConnection();
        } catch (SQLException e) {
            LOGGER.error("getConnection 异常 ", e);
            throw new RuntimeException("getConnection 异常");
        }
        if (connection == null) {
            LOGGER.error("connection = null ");
            throw new RuntimeException("connection = null 异常");
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("connection.commit 异常");
            throw new RuntimeException("connection.commit 异常");
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * 事务回滚
     */
    public static void rollBack() {
        Connection connection;
        try {
            connection = getConnection();
        } catch (SQLException e) {
            LOGGER.error("getConnection 异常 ", e);
            throw new RuntimeException("getConnection 异常");
        }
        if (connection == null) {
            LOGGER.error("connection = null ");
            throw new RuntimeException("connection = null 异常");
        }
        try {
            connection.rollback();
        } catch (SQLException e) {
            LOGGER.error("connection.rollback 异常");
            throw new RuntimeException("connection.rollback 异常");
        } finally {
            closeConnection(connection);
        }
    }

}
