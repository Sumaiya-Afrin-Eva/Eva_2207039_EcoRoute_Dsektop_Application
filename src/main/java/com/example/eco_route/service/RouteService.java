package com.example.eco_route.service;
import com.example.eco_route.model.Route;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RouteService {

    private static final RouteService instance = new RouteService();
    private final List<Route> routes = new ArrayList<>();

    private RouteService() {}

    public static RouteService getInstance() {
        return instance;
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public List<Route> getRoutes(String origin, String destination) {
        return routes.stream()
                .filter(r -> r.getOrigin().equalsIgnoreCase(origin)
                        && r.getDestination().equalsIgnoreCase(destination))
                .sorted(Comparator.comparingDouble(Route::getEcoScore).reversed())
                .collect(Collectors.toList());
    }
}
