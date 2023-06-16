package com.example.softwareproject.controllers;

import com.example.softwareproject.Main;
import com.example.softwareproject.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class that controls the main screen.
 *
 * @author Kelsey Holley
 * */

public class MainScreenController implements Initializable {

    public TextField partSearch;
    public TableView<Part> partTable;
    public TableView<Product> productTable;
    public TextField productSearch;
    public static boolean modify;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partInvLevelCol;
    public TableColumn partPriceCol;
    public TableColumn productIDCol;
    public TableColumn productNameCol;
    public TableColumn productInvLevelCol;
    public TableColumn productPriceCol;
    public static Part selectedPart;
    public static Product selectedProduct;

    /**
     * Initializes components for the main screen.
     * Sets up the tables containing all parts and products in inventory, sets to a blank table if inventory is empty
     * @param url
     * @param resourceBundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Inventory.getAllParts().isEmpty()){
            partTable.setMouseTransparent(true);
            ObservableList<Part> temp = FXCollections.observableArrayList();
            temp.add(new InHouse(1,"",2,2,2,2,2));
            partTable.setItems(temp);
        } else{
            partTable.setMouseTransparent(false);
            partTable.setItems(Inventory.getAllParts());
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
            partInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("Stock"));
            partPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        }


        if(Inventory.getAllProducts().isEmpty()){
            productTable.setMouseTransparent(true);
            ObservableList<Product> temp = FXCollections.observableArrayList();
            temp.add(new Product(1,"",2,2,2,2));
            productTable.setItems(temp);
        } else{
            productTable.setMouseTransparent(false);
            productTable.setItems(Inventory.getAllProducts());
            productIDCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
            productNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
            productInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("Stock"));
            productPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        }
    }

    /**
     * Searches for a part when the user enters an ID or partial name and presses enter.
     * Checks for parts that match either the ID or partial name and sets the items in the table to the results.
     * An input of an empty string resets the table to all parts in inventory.
     * @param actionEvent Event caused by user pressing enter in search field on the part side
     * */
    public void onPartSearch(ActionEvent actionEvent) {
        String searchTerm = partSearch.getText();
        ObservableList<Part> results;
        Part idSearchResult = null;

        try{
            int searchID = Integer.parseInt(searchTerm);
            idSearchResult = Inventory.lookupPart(searchID);
        } catch(NumberFormatException e){
            //do nothing
        }
        finally{
            results = Inventory.lookupPart(searchTerm);
            boolean idSearchAdd = true;
            for(Part part : results){
                if(idSearchResult != null && part.getId() == idSearchResult.getId()){
                    idSearchAdd = false;
                }
            }
            if(idSearchResult != null && idSearchAdd){results.add(idSearchResult);}
        }
        if(results.size() == 0){partTable.setPlaceholder(new Label("There are no parts that match your search."));}
        partTable.setItems(results);
    }

    /**
     * Loads the Add Part screen when the user clicks on the Add button.
     * @param actionEvent Event cause by user clicking on Add button on the part side
     * */
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("AddModifyPart.fxml"));
        Stage stage = (Stage) productTable.getScene().getWindow();
        Scene scene = new Scene(root, 501, 446);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the Modify Part screen when the user clicks on the Modify button.
     * If no item is selected, shows error dialog box.
     * @param actionEvent Event cause by user clicking on Modify button on the part side
     * */
    public void onModifyPart(ActionEvent actionEvent) throws IOException {
        if(partTable.getSelectionModel().getSelectedItem() == null){
            Alert warning = new Alert(Alert.AlertType.ERROR, "You must select a part!");
            warning.showAndWait();
        } else{
            modify = true;
            selectedPart = partTable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(Main.class.getResource("AddModifyPart.fxml"));
            Stage stage = (Stage) productTable.getScene().getWindow();
            Scene scene = new Scene(root, 501, 446);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onDeletePart(ActionEvent actionEvent) {
        if(partTable.getSelectionModel().getSelectedItem() == null){
            Alert warning = new Alert(Alert.AlertType.ERROR, "You must select a part!");
            warning.showAndWait();
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.get() == ButtonType.OK) {
                selectedPart = partTable.getSelectionModel().getSelectedItem();
                boolean success = Inventory.deletePart(selectedPart);
                if (!success) {
                    Alert fail = new Alert(Alert.AlertType.ERROR, "Part could not be deleted.");
                    fail.showAndWait();
                }
                if (Inventory.getAllParts().isEmpty()) {
                    partTable.setMouseTransparent(true);
                    ObservableList<Part> temp = FXCollections.observableArrayList();
                    temp.add(new InHouse(1, "", 2, 2, 2, 2, 2));
                    partIDCol.setCellValueFactory(null);
                    partNameCol.setCellValueFactory(null);
                    partInvLevelCol.setCellValueFactory(null);
                    partPriceCol.setCellValueFactory(null);
                    partTable.setItems(temp);
                }
            }
        }
    }

    /**
     * Searches for a product when the user enters an ID or partial name and presses enter.
     * Checks for products that match either the ID or partial name and sets the items in the table to the results.
     * An input of an empty string resets the table to all products in inventory.
     * @param actionEvent Event caused by user pressing enter in search field on the product side
     * */
    public void onProductSearch(ActionEvent actionEvent) {
        String searchTerm = productSearch.getText();
        ObservableList<Product> results;
        Product idSearchResult = null;

        try{
            int searchID = Integer.parseInt(searchTerm);
            idSearchResult = Inventory.lookupProduct(searchID);
        } catch(NumberFormatException e){
            //do nothing
        }
        finally{
            results = Inventory.lookupProduct(searchTerm);
            boolean idSearchAdd = true;
            for(Product product : results){
                if(idSearchResult != null && product.getId() == idSearchResult.getId()){
                    idSearchAdd = false;
                }
            }
            if(idSearchResult != null && idSearchAdd){results.add(idSearchResult);}
        }

        if(results.size() == 0){productTable.setPlaceholder(new Label("There are no products that match your search."));}
        productTable.setItems(results);
    }

    /**
     * Loads the Add Product screen when the user clicks on the Add button.
     * @param actionEvent Event caused by user clicking on Add button on the product side
     * */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("AddModifyProduct.fxml"));
        Stage stage = (Stage) productTable.getScene().getWindow();
        Scene scene = new Scene(root, 770, 493);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the Modify Product screen when the user clicks on the Modify button.
     * If no item is selected, shows error dialog box.
     * @param actionEvent Event caused by user clicking on Modify button on the product side
     * */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        if(productTable.getSelectionModel().getSelectedItem() == null){
            Alert warning = new Alert(Alert.AlertType.ERROR, "You must select a product!");
            warning.showAndWait();
        } else{
            modify = true;
            selectedProduct = productTable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(Main.class.getResource("AddModifyProduct.fxml"));
            Stage stage = (Stage) productTable.getScene().getWindow();
            Scene scene = new Scene(root, 770, 493);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Deletes the selected product when the user clicks on the Delete button.
     * Displays error dialogs if deletion is not successful or product has associated parts.
     * If the deletion results in an empty product inventory, resets table to empty.
     * @param actionEvent Event caused by user clicking on Delete button on the product side
     * */
    public void onDeleteProduct(ActionEvent actionEvent) {
        if(productTable.getSelectionModel().getSelectedItem() == null){
            Alert warning = new Alert(Alert.AlertType.ERROR, "You must select a product!");
            warning.showAndWait();
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    selectedProduct = productTable.getSelectionModel().getSelectedItem();
                    boolean success = Inventory.deleteProduct(selectedProduct);
                    if (!success) {
                        Alert fail = new Alert(Alert.AlertType.ERROR, "Product could not be deleted.");
                        fail.showAndWait();
                    }
                } catch (Exception e) {
                    Alert hasParts = new Alert(Alert.AlertType.ERROR, "You may not delete products with associated parts!");
                    hasParts.showAndWait();
                }
                if (Inventory.getAllProducts().isEmpty()) {
                    productTable.setMouseTransparent(true);
                    ObservableList<Product> temp = FXCollections.observableArrayList();
                    temp.add(new Product(1, "", 2, 2, 2, 2));
                    productIDCol.setCellValueFactory(null);
                    productNameCol.setCellValueFactory(null);
                    productInvLevelCol.setCellValueFactory(null);
                    productPriceCol.setCellValueFactory(null);
                    productTable.setItems(temp);
                }
            }
        }
    }

    /**
     * Closes the application when the Exit button is clicked.
     * @param actionEvent Event cause by user clicking the exit button
     * */
    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) productTable.getScene().getWindow();
        stage.close();
    }
}