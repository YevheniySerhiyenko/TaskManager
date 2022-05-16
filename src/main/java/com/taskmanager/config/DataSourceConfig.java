package com.taskmanager.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DataSourceConfig {
    @Bean
    InitializingBean createTableTasks(DataSource dataSource) {
        return () -> {
            String sql = ""
                    + " CREATE TABLE tasks ("
                    + "   id          INTEGER       NOT NULL AUTO_INCREMENT,"
                    + "   name        VARCHAR(255)  NOT NULL,"
                    + "   index       INTEGER       NOT NULL,"
                    + "   description TEXT          NOT NULL,"
                    + "   createdAt   DATE          NOT NULL,"
                    + "   columnId    INTEGER       NOT NULL"
                    + " )";

            try (Statement statement = dataSource.getConnection().createStatement()) {
                statement.execute(sql);
            } catch (SQLException e) {
                throw new RuntimeException("Table not created", e);
            }
        };
    }

    @Bean
    InitializingBean createTableColumns(DataSource dataSource) {
        return () -> {
            String sql = ""
                    + " CREATE TABLE columns ("
                    + "   id          INTEGER       NOT NULL AUTO_INCREMENT,"
                    + "   index       INTEGER       NOT NULL,"
                    + "   name        VARCHAR(255)  NOT NULL"
                    + " )";

            try (Statement statement = dataSource.getConnection().createStatement()) {
                statement.execute(sql);
            } catch (SQLException e) {
                throw new RuntimeException("Table not created", e);
            }
        };
    }
}
