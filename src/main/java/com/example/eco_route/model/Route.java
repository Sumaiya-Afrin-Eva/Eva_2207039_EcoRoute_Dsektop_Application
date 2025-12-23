package com.example.eco_route.model;

public class Route {

    private String origin;
    private String destination;
    private String company;
    private String routePath;
    private double distance;
    private double time;
    private double fuel;
    private double ticket;
    private double ecoScore;

    public Route(String origin, String destination, String company,
                 double distance, double time, double fuel, double ticket) {
        this.origin = origin;
        this.destination = destination;
        this.company = company;
        this.distance = distance;
        this.time = time;
        this.fuel = fuel;
        this.ticket = ticket;
        this.routePath = null;
        calculateEcoScore();
    }

    public Route(String origin, String destination, String company,
                 double distance, double time, double fuel, double ticket, String routePath) {
        this.origin = origin;
        this.destination = destination;
        this.company = company;
        this.distance = distance;
        this.time = time;
        this.fuel = fuel;
        this.ticket = ticket;
        this.routePath = routePath;
        calculateEcoScore();
    }

    private void calculateEcoScore() {
        this.ecoScore = 1000 / (fuel * 2 + time + distance);
    }

    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getCompany() { return company; }
    public String getRoutePath() { return routePath; }
    public double getDistance() { return distance; }
    public double getTime() { return time; }
    public double getFuel() { return fuel; }
    public double getTicket() { return ticket; }
    public double getEcoScore() { return ecoScore; }
}