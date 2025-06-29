package com.medcare.dao;

import com.medcare.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {
    protected Connection getConnection() throws SQLException {
        return DatabaseConfig.getConnection();
    }
}