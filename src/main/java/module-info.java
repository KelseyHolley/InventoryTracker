module com.example.softwareproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.softwareproject to javafx.fxml;
    exports com.example.softwareproject;
    exports com.example.softwareproject.controllers;
    opens com.example.softwareproject.controllers to javafx.fxml;
    exports com.example.softwareproject.models;
    opens com.example.softwareproject.models to javafx.fxml;
}