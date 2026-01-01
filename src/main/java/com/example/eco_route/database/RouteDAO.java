package com.example.eco_route.database;

import com.example.eco_route.model.Route;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {

    private static final RouteDAO instance = new RouteDAO();

    private RouteDAO() {}

    public static RouteDAO getInstance() {
        return instance;
    }

    public void addRoute(Route route) {
        String sql = "INSERT INTO routes (origin, destination, company, routePath, distance, time, fuel, ticket, ecoScore) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, route.getOrigin());
            pstmt.setString(2, route.getDestination());
            pstmt.setString(3, route.getCompany());
            pstmt.setString(4, route.getRoutePath());
            pstmt.setDouble(5, route.getDistance());
            pstmt.setDouble(6, route.getTime());
            pstmt.setDouble(7, route.getFuel());
            pstmt.setDouble(8, route.getTicket());
            pstmt.setDouble(9, route.getEcoScore());

            pstmt.executeUpdate();
            System.out.println("Route added to database successfully!");

        } catch (SQLException e) {
            System.err.println("Error adding route: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Route> getRoutesByOriginAndDestination(String origin, String destination) {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT * FROM routes WHERE LOWER(origin) = LOWER(?) AND LOWER(destination) = LOWER(?) ORDER BY ecoScore DESC";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, origin);
            pstmt.setString(2, destination);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Route route = new Route(
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("company"),
                        rs.getDouble("distance"),
                        rs.getDouble("time"),
                        rs.getDouble("fuel"),
                        rs.getDouble("ticket"),
                        rs.getString("routePath")
                );
                routes.add(route);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching routes: " + e.getMessage());
            e.printStackTrace();
        }

        return routes;
    }

    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT * FROM routes ORDER BY createdAt DESC";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Route route = new Route(
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("company"),
                        rs.getDouble("distance"),
                        rs.getDouble("time"),
                        rs.getDouble("fuel"),
                        rs.getDouble("ticket"),
                        rs.getString("routePath")
                );
                routes.add(route);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all routes: " + e.getMessage());
            e.printStackTrace();
        }

        return routes;
    }

    public void updateRoute(String origin, String destination, String company, double time, double fuel, double ticket) {
        String sql = "UPDATE routes SET time = ?, fuel = ?, ticket = ? " +
                "WHERE LOWER(origin) = LOWER(?) AND LOWER(destination) = LOWER(?) AND LOWER(company) = LOWER(?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, time);
            pstmt.setDouble(2, fuel);
            pstmt.setDouble(3, ticket);
            pstmt.setString(4, origin);
            pstmt.setString(5, destination);
            pstmt.setString(6, company);

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Routes updated: " + affectedRows);

        } catch (SQLException e) {
            System.err.println("Error updating route: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteRoute(String origin, String destination, String company) {
        String sql = "DELETE FROM routes WHERE LOWER(origin) = LOWER(?) AND LOWER(destination) = LOWER(?) AND LOWER(company) = LOWER(?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, origin);
            pstmt.setString(2, destination);
            pstmt.setString(3, company);

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Routes deleted: " + affectedRows);

        } catch (SQLException e) {
            System.err.println("Error deleting route: " + e.getMessage());
            e.printStackTrace();
        }
    }
}