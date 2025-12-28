package com.example.eco_route.service;

import java.util.ArrayList;
import java.util.List;

public class PlaceService {

    private static final PlaceService instance = new PlaceService();

    private final List<String> allPlaces = new ArrayList<>();

    private PlaceService() {
        initializePlaces();
    }

    public static PlaceService getInstance() {
        return instance;
    }

    private void initializePlaces() {
        allPlaces.addAll(List.of(
                "Dhaka",
                "Chittagong",
                "Khulna",
                "Rajshahi",
                "Sylhet",
                "Barisal",
                "Rangpur",
                "Mymensingh",
                "Gazipur",
                "Narayanganj",
                "Tangail",
                "Pabna",
                "Bogra",
                "Dinajpur",
                "Jashore",
                "Comilla",
                "Noakhali",
                "Cox's Bazar",
                "Bhola",
                "Pirojpur"
        ));
    }

    public List<String> getAllPlaces() {
        return new ArrayList<>(allPlaces);
    }

    public List<String> getDestinationPlaces(String selectedOrigin) {
        List<String> destinations = new ArrayList<>(allPlaces);
        destinations.remove(selectedOrigin);
        return destinations;
    }

    public List<String> getRemainingPlaces(List<String> selectedPlaces) {
        List<String> remaining = new ArrayList<>(allPlaces);
        remaining.removeAll(selectedPlaces);
        return remaining;
    }
}