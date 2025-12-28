package com.example.eco_route.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CompanyService {

    private static final CompanyService instance = new CompanyService();
    private final ObservableList<String> companies = FXCollections.observableArrayList();

    private String currentCompany;

    private CompanyService() {}

    public static CompanyService getInstance() {
        return instance;
    }

    public void addCompany(String companyName) {
        if (companyName != null && !companyName.isBlank()) {
            currentCompany = companyName;
            if (!companies.contains(companyName)) {
                companies.add(companyName);
            }
        }
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public ObservableList<String> getCompanies() {
        return companies;
    }
}
