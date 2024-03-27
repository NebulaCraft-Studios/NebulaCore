package net.nebulacraft.nebulacore.util.db;

import com.google.common.net.HostAndPort;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import net.nebulacraft.nebulacore.NebulaCore;
import net.nebulacraft.nebulacore.config.ConfigTypes;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.sqlite.JDBC;

@Log4j2(topic = "NebulaCore")
public class DatabaseHandler {

    private transient HikariDataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void initialiseDatabase(String type) throws SQLException {
        LOGGER.info("Setting up SQLite...");
        createDataSource(new File(NebulaCore.getInstance().getDataFolder(), "data.sqlite3"), type);
        setupTables();
    }

    private void createDataSource(
            @NotNull File file, String type) {

        if (type.equals("sqlite")) {

            HikariConfig sqliteConfig = new HikariConfig();
            sqliteConfig.setDataSourceClassName("org.sqlite.SQLiteDataSource");
            sqliteConfig.addDataSourceProperty("url", JDBC.PREFIX + file.getAbsolutePath());
            sqliteConfig.addDataSourceProperty("encoding", "UTF-8");
            sqliteConfig.addDataSourceProperty("enforceForeignKeys", "true");
            sqliteConfig.addDataSourceProperty("synchronous", "NORMAL");
            sqliteConfig.addDataSourceProperty("journalMode", "WAL");
            sqliteConfig.setPoolName("SQLite");
            sqliteConfig.setMaximumPoolSize(1);

            dataSource = new HikariDataSource(sqliteConfig);

            LOGGER.info("Using SQLite Database.");
        } else if (type.equals("mysql")) {

            var config = NebulaCore.getConfiguration(ConfigTypes.SETTINGS);

            String rawHost = config.getString("Database.DATABASE_HOST");
            String databaseName = config.getString("Database.DATABASE_NAME");
            String username = config.getString("Database.DATABASE_USERNAME");
            String password = config.getString("Database.DATABASE_PASSWORD");

            List<String> missingProperties = new ArrayList<>();

            if (rawHost == null || rawHost.isBlank()) {
                missingProperties.add("DATABASE_HOST");
            }

            if (databaseName == null || databaseName.isBlank()) {
                missingProperties.add("DATABASE_NAME");
            }

            if (username == null || username.isBlank()) {
                missingProperties.add("DATABASE_USERNAME");
            }

            if (password == null || password.isBlank()) {
                missingProperties.add("DATABASE_PASSWORD");
            }

            if (!missingProperties.isEmpty()) {
                throw new IllegalStateException("Missing MySQL Configuration Properties: " + missingProperties);
            }

            HostAndPort host = HostAndPort.fromHost(rawHost);
            HikariConfig mysqlConfig = new HikariConfig();
            mysqlConfig.setPoolName("NebulaCore");
            mysqlConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s",
                    host.getHost(), host.getPortOrDefault(3306), databaseName));
            mysqlConfig.setMaxLifetime(TimeUnit.MINUTES.toMillis(30));
            mysqlConfig.setUsername(username);
            mysqlConfig.setPassword(password);
            mysqlConfig.setMaximumPoolSize(2);

            LOGGER.info("Using MySQL database '{}' on {}:{}",
                    databaseName, host.getHost(), host.getPortOrDefault(3306));
            dataSource = new HikariDataSource(mysqlConfig);
        }
    }

    public void setupTables(String type) throws SQLException {
        LOGGER.info("Setting up SQL Tables for [" + type + "].");

        if (type.equals("mysql")) {
            try (var statement = getConnection().createStatement();
                var result = statement.executeQuery("SELECT @@version, @@version_comment")) {
                if (result.next()) {
                    String version = result.getString(1);
                    String brand = result.getString(2);
                    LOGGER.info("MySQL Server: {} v{}", brand, version);
                } else {
                    LOGGER.info("Failed to get MySQL Server Version.");
                }
            }
        }

        try (Connection connection = dataSource.getConnection();
            ) {

        }
    }


}
