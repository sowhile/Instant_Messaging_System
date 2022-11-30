package server.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 使用Druid连接池的JDBC连接数据库工具类，提供：
 * getConnection()和 close方法
 */
public class JDBCDruidUtils {
    public static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(new FileReader("src/server/utils/druid.properties"));
        } catch (IOException e) {
            //这里将IO编译时异常转成运行时异常抛出去
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
