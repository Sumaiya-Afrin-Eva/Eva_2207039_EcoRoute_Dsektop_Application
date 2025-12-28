package com.example.eco_route.service;

import com.example.eco_route.model.Route;
import com.example.eco_route.database.RouteDAO;

import java.util.List;

public class RouteService {

    private static final RouteService instance = new RouteService();
    private final RouteDAO routeDAO = RouteDAO.getInstance();

    private RouteService() {}

    public static RouteService getInstance() {
        return instance;
    }

    public void addRoute(Route route) {
        routeDAO.addRoute(route);
    }

    public List<Route> getRoutes(String origin, String destination) {
        return routeDAO.getRoutesByOriginAndDestination(origin, destination);
    }

    public List<Route> getAllRoutes() {
        return routeDAO.getAllRoutes();
    }

    public void updateRoute(String origin, String destination, String company, double time, double fuel, double ticket) {
        routeDAO.updateRoute(origin, destination, company, time, fuel, ticket);
    }

    public void deleteRoute(String origin, String destination, String company) {
        routeDAO.deleteRoute(origin, destination, company);
    }
}