package com.kasv.gunda.bootcamp.services;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService {

    private final DataSource dataSource;

    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String connectToDb() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn != null) {
                return "Connected to database";
            } else {
                return "Failed to connect to database";
            }
        } catch (SQLException e) {
            return "Failed to connect to database: " + e.getMessage();
        }
    }
}
