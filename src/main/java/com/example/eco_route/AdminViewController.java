package com.example.eco_route;

import com.example.eco_route.model.Route;
import com.example.eco_route.service.RouteService;
import com.example.eco_route.service.CompanyService;
import com.example.eco_route.service.PlaceService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminViewController {

    @FXML private ComboBox<String> originCombo;
    @FXML private ComboBox<String> destinationCombo;
    @FXML private ComboBox<String> routeCombo;
    @FXML private TextField fuelField;
    @FXML private TextField timeField;
    @FXML private TextField distanceField;
    @FXML private TextField priceField;
    @FXML private Button homeButton;
    @FXML private Button manageRouteButton;
    @FXML private VBox selectedPlacesBox;

    private final RouteService routeService = RouteService.getInstance();
    private final CompanyService companyService = CompanyService.getInstance();
    private final PlaceService placeService = PlaceService.getInstance();

    private List<String> selectedPlaces = new ArrayList<>();

    @FXML
    public void initialize() {
        loadOriginPlaces();
        setupOriginComboListener();
        setupDestinationComboListener();
        setupRouteComboListener();

        if (homeButton != null) {
            homeButton.setOnAction(e -> goHome());
        }
        if (manageRouteButton != null) {
            manageRouteButton.setOnAction(e -> manageRoutes());
        }
    }

    private void loadOriginPlaces() {
        List<String> places = placeService.getAllPlaces();
        originCombo.getItems().addAll(places);
    }

    private void setupOriginComboListener() {
        originCombo.setOnAction(e -> updateDestinationPlaces());
    }

    private void setupDestinationComboListener() {
        destinationCombo.setOnAction(e -> {
            String destination = destinationCombo.getValue();
            if (destination != null && !destination.isEmpty()) {
                updateRouteCombo();
            }
        });
    }

    private void setupRouteComboListener() {
        updateRouteCombo();
        routeCombo.setOnAction(e -> handleRouteSelection());
    }

    private void updateDestinationPlaces() {
        String selectedOrigin = originCombo.getValue();

        if (selectedOrigin == null || selectedOrigin.isEmpty()) {
            destinationCombo.getItems().clear();
            return;
        }

        List<String> destinations = placeService.getDestinationPlaces(selectedOrigin);
        destinationCombo.getItems().clear();
        destinationCombo.getItems().addAll(destinations);
    }

    private void updateRouteCombo() {
        String origin = originCombo.getValue();
        String destination = destinationCombo.getValue();

        if (origin == null || origin.isEmpty() ||
                destination == null || destination.isEmpty()) {
            routeCombo.getItems().clear();
            selectedPlaces.clear();
            displaySelectedPlaces();
            return;
        }

        List<String> availablePlaces = placeService.getRemainingPlaces(selectedPlaces);
        routeCombo.getItems().clear();
        routeCombo.getItems().addAll(availablePlaces);
    }

    private void handleRouteSelection() {
        String selectedPlace = routeCombo.getValue();

        if (selectedPlace == null || selectedPlace.isEmpty()) {
            return;
        }

        if (!selectedPlaces.contains(selectedPlace)) {
            selectedPlaces.add(selectedPlace);
            updateRouteCombo();
            displaySelectedPlaces();

            routeCombo.getSelectionModel().clearSelection();
        }
    }

    private void displaySelectedPlaces() {
        selectedPlacesBox.getChildren().clear();

        if (selectedPlaces.isEmpty()) {
            return;
        }

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setPrefWrapLength(500);
        flowPane.setStyle("-fx-padding: 5;");

        for (String place : selectedPlaces) {
            HBox placeBox = createPlaceBoxWithDeleteButton(place);
            flowPane.getChildren().add(placeBox);
        }

        selectedPlacesBox.getChildren().add(flowPane);
    }

    private HBox createPlaceBoxWithDeleteButton(String place) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(8);
        box.setStyle(
                "-fx-border-color: #049029; " +
                        "-fx-border-radius: 15; " +
                        "-fx-background-color: #e8f5e9; "
        );

        Label placeLabel = new Label(place);
        placeLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #049029;");

        Button deleteButton = new Button("✕");
        deleteButton.setStyle(
                "-fx-font-size: 13; " +
                        "-fx-background-color: transparent; " +
                        "-fx-text-fill: #d32f2f; " +
                        "-fx-cursor: hand;"
        );

        deleteButton.setOnAction(e -> removeSelectedPlace(place));

        box.getChildren().addAll(placeLabel, deleteButton);
        return box;
    }

    private void removeSelectedPlace(String place) {
        selectedPlaces.remove(place);
        updateRouteCombo();
        displaySelectedPlaces();
    }

    @FXML
    private void handleSaveRoute() {
        try {
            String company = companyService.getCurrentCompany();

            if (company == null || company.isBlank()) {
                new Alert(Alert.AlertType.ERROR,
                        "Company not found. Please login again.").show();
                return;
            }

            if (originCombo.getValue() == null || originCombo.getValue().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select Origin").show();
                return;
            }
            if (destinationCombo.getValue() == null || destinationCombo.getValue().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select Destination").show();
                return;
            }
            if (fuelField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Fuel amount").show();
                return;
            }
            if (timeField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Time").show();
                return;
            }
            if (distanceField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Distance").show();
                return;
            }
            if (priceField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Ticket Price").show();
                return;
            }

            double time = Double.parseDouble(timeField.getText().trim());
            double fuel = Double.parseDouble(fuelField.getText().trim());
            double distance = Double.parseDouble(distanceField.getText().trim());
            double ticket = Double.parseDouble(priceField.getText().trim());

            StringBuilder routePath = new StringBuilder();
            routePath.append(originCombo.getValue());
            for (String place : selectedPlaces) {
                routePath.append(" → ").append(place);
            }
            routePath.append(" → ").append(destinationCombo.getValue());

            Route route = new Route(
                    originCombo.getValue(),
                    destinationCombo.getValue(),
                    company,
                    distance,
                    time,
                    fuel,
                    ticket,
                    routePath.toString()
            );

            routeService.addRoute(route);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Route saved successfully!\nRoute: " + routePath.toString());
            alert.show();

            resetFields();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Invalid number format! Please enter valid numbers for Fuel, Time, Distance, and Ticket Price.").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    private void resetFields() {
        originCombo.getSelectionModel().clearSelection();
        destinationCombo.getSelectionModel().clearSelection();
        routeCombo.getSelectionModel().clearSelection();
        fuelField.clear();
        timeField.clear();
        distanceField.clear();
        priceField.clear();
        selectedPlaces.clear();
        displaySelectedPlaces();
    }

    private void goHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("home_view.fxml")
            );

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) homeButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to load home view.").show();
        }
    }

    private void manageRoutes() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("manage_route_view.fxml")
            );

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) homeButton.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to load manage route view.").show();
        }
    }
}