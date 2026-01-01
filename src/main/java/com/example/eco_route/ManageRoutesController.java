package com.example.eco_route;

import com.example.eco_route.database.RouteDAO;
import com.example.eco_route.model.Route;
import com.example.eco_route.service.CompanyService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ManageRoutesController {

    @FXML private ComboBox<String> routeComboBox;
    @FXML private TextField originField;
    @FXML private TextField destinationField;
    @FXML private TextField fuelField;
    @FXML private TextField timeField;
    @FXML private TextField ticketField;

    private final RouteDAO routeDAO = RouteDAO.getInstance();
    private final CompanyService companyService = CompanyService.getInstance();
    private List<Route> allRoutes;

    @FXML
    public void initialize() {
        loadRoutesIntoComboBox();
    }

    private void loadRoutesIntoComboBox() {
        allRoutes = routeDAO.getAllRoutes();
        routeComboBox.getItems().clear();

        for (Route route : allRoutes) {
            String displayText = route.getOrigin() + " → " + route.getDestination() + " (" + route.getCompany() + ")";
            routeComboBox.getItems().add(displayText);
        }
    }

    @FXML
    public void loadSelectedRoute() {
        int selectedIndex = routeComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < allRoutes.size()) {
            Route route = allRoutes.get(selectedIndex);
            originField.setText(route.getOrigin());
            destinationField.setText(route.getDestination());
            fuelField.setText(String.valueOf(route.getFuel()));
            timeField.setText(String.valueOf(route.getTime()));
            ticketField.setText(String.valueOf(route.getTicket()));
        }
    }

    @FXML
    public void updateRoute() {
        try {
            int selectedIndex = routeComboBox.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0) {
                showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a route first.");
                return;
            }

            Route route = allRoutes.get(selectedIndex);
            double time = Double.parseDouble(timeField.getText().trim());
            double fuel = Double.parseDouble(fuelField.getText().trim());
            double ticket = Double.parseDouble(ticketField.getText().trim());

            routeDAO.updateRoute(route.getOrigin(), route.getDestination(), route.getCompany(), time, fuel, ticket);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Route updated successfully!");
            loadRoutesIntoComboBox();
            resetFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for Fuel, Time, and Ticket.");
        }
    }

    @FXML
    public void resetFields() {
        originField.clear();
        destinationField.clear();
        fuelField.clear();
        timeField.clear();
        ticketField.clear();
        routeComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    public void back() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("admin_view.fxml")
            );

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) routeComboBox.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load admin view.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}