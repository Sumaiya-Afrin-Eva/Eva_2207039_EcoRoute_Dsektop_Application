package com.example.eco_route.database;

import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:ecoroute.db";
    private static final DatabaseManager instance = new DatabaseManager();

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String routesTable = "CREATE TABLE IF NOT EXISTS routes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "origin TEXT NOT NULL," +
                    "destination TEXT NOT NULL," +
                    "company TEXT NOT NULL," +
                    "routePath TEXT," +
                    "distance REAL NOT NULL," +
                    "time REAL NOT NULL," +
                    "fuel REAL NOT NULL," +
                    "ticket REAL NOT NULL," +
                    "ecoScore REAL NOT NULL," +
                    "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            stmt.execute(routesTable);

            String searchHistoryTable = "CREATE TABLE IF NOT EXISTS search_history (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId TEXT NOT NULL," +
                    "origin TEXT NOT NULL," +
                    "destination TEXT NOT NULL," +
                    "selectedRoute_id INTEGER," +
                    "selectedCompany TEXT," +
                    "distance REAL," +
                    "time REAL," +
                    "fuel REAL," +
                    "ticket REAL," +
                    "ecoScore REAL," +
                    "searchedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(selectedRoute_id) REFERENCES routes(id)" +
                    ");";

            stmt.execute(searchHistoryTable);

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}