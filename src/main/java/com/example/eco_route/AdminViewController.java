package com.example.eco_route;

import com.example.eco_route.model.Route;
import com.example.eco_route.service.RouteService;
import com.example.eco_route.service.CompanyService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminViewController {

    @FXML private TextField originField;
    @FXML private TextField destinationField;
    @FXML private TextField fuelField;
    @FXML private TextField timeField;
    @FXML private TextField ticketField;
    @FXML private TextField routePath;
    @FXML private Button homeButton;
    @FXML private Button manageRoute;

    private final RouteService routeService = RouteService.getInstance();
    private final CompanyService companyService = CompanyService.getInstance();

    @FXML
    public void initialize() {
        if (homeButton != null) {
            homeButton.setOnAction(e -> goHome());
        }

        if (manageRoute != null) {
            manageRoute.setOnAction(e -> manageRoutes());
        }
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

            if (originField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Origin").show();
                return;
            }
            if (destinationField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Destination").show();
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
            if (ticketField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Ticket Price").show();
                return;
            }

            double time = Double.parseDouble(timeField.getText().trim());
            double fuel = Double.parseDouble(fuelField.getText().trim());
            double ticket = Double.parseDouble(ticketField.getText().trim());

            Route route = new Route(
                    originField.getText().trim(),
                    destinationField.getText().trim(),
                    company,
                    120,
                    time,
                    fuel,
                    ticket,
                    routePath.getText().trim()
            );

            routeService.addRoute(route);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Route saved successfully!");
            alert.show();

            originField.clear();
            destinationField.clear();
            fuelField.clear();
            timeField.clear();
            ticketField.clear();
            routePath.clear();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Invalid number format! Please enter valid numbers for Fuel, Time, and Ticket Price.").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
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
            new Alert(Alert.AlertType.ERROR, "Unable to load home view.").show();
        }
    }
}