package edu.netcracker.backend.config;

import lombok.Setter;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties("postgres")
@EnableConfigurationProperties(PostgreSQLConfig.class)
@Setter
public class PostgreSQLConfig {

    @NotNull
    private String servername;
    @NotNull
    private Integer port;
    @NotNull
    private String database;
    @NotNull
    private String username;
    @NotNull
    private String password;

    @Bean
    @Profile("prod")
    DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName(servername);
        dataSource.setUser(username);
        dataSource.setDatabaseName(database);
        dataSource.setPassword(password);
        dataSource.setPortNumber(port);
        return dataSource;
    }
}