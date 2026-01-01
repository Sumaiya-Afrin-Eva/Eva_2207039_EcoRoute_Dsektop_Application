package com.example.eco_route.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryDAO {

    private static final SearchHistoryDAO instance = new SearchHistoryDAO();

    private SearchHistoryDAO() {}

    public static SearchHistoryDAO getInstance() {
        return instance;
    }

    public void addSearchHistory(String userId, String origin, String destination,
                                 String selectedCompany, double distance, double time,
                                 double fuel, double ticket, double ecoScore) {
        String sql = "INSERT INTO search_history (userId, origin, destination, selectedCompany, distance, time, fuel, ticket, ecoScore) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, origin);
            pstmt.setString(3, destination);
            pstmt.setString(4, selectedCompany);
            pstmt.setDouble(5, distance);
            pstmt.setDouble(6, time);
            pstmt.setDouble(7, fuel);
            pstmt.setDouble(8, ticket);
            pstmt.setDouble(9, ecoScore);

            pstmt.executeUpdate();
            System.out.println("Search history recorded!");

        } catch (SQLException e) {
            System.err.println("Error recording search history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<SearchHistoryRecord> getUserSearchHistory(String userId) {
        List<SearchHistoryRecord> history = new ArrayList<>();
        String sql = "SELECT * FROM search_history WHERE userId = ? ORDER BY searchedAt DESC";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SearchHistoryRecord record = new SearchHistoryRecord(
                        rs.getInt("id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("selectedCompany"),
                        rs.getDouble("distance"),
                        rs.getDouble("time"),
                        rs.getDouble("fuel"),
                        rs.getDouble("ticket"),
                        rs.getDouble("ecoScore"),
                        rs.getTimestamp("searchedAt")
                );
                history.add(record);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching search history: " + e.getMessage());
            e.printStackTrace();
        }

        return history;
    }

    public static class SearchHistoryRecord {
        public int id;
        public String origin;
        public String destination;
        public String selectedCompany;
        public double distance;
        public double time;
        public double fuel;
        public double ticket;
        public double ecoScore;
        public Timestamp searchedAt;

        public SearchHistoryRecord(int id, String origin, String destination, String selectedCompany,
                                   double distance, double time, double fuel, double ticket, double ecoScore, Timestamp searchedAt) {
            this.id = id;
            this.origin = origin;
            this.destination = destination;
            this.selectedCompany = selectedCompany;
            this.distance = distance;
            this.time = time;
            this.fuel = fuel;
            this.ticket = ticket;
            this.ecoScore = ecoScore;
            this.searchedAt = searchedAt;
        }
    }
}