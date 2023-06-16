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
 * Class that controls the product screen used to add or modify products.
 *
 * @author Kelsey Holley
 * */
public class AddModifyProductController implements Initializable {
    public Label productTitle;
    public TextField idTextField;
    public TextField nameTextField;
    public TextField inventoryTextField;
    public TextField priceTextField;
    public TextField maxTextField;
    public TextField minTextField;
    public TableView<Part> associatedPartsTable;
    public TableColumn idColAssoc;
    public TableColumn nameColAssoc;
    public TableColumn invColAssoc;
    public TableColumn priceColAssoc;
    public TableView<Part> allPartsTable;
    public TableColumn idColAll;
    public TableColumn nameColAll;
    public TableColumn invColAll;
    public TableColumn priceColAll;
    public TextField partSearch;

    /**
     * Initializes components for the add/modify product screen.
     * Sets up the tables containing all parts and associated parts, sets to blank tables if empty.
     * Sets header text to "Add Product" or "Modify Product" depending on which button is clicked.
     * If modifying, sets text fields and tables to contain the already existing data.
     * @param url
     * @param resourceBundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Inventory.getAllParts().isEmpty()){
            allPartsTable.setMouseTransparent(true);
            ObservableList<Part> temp = FXCollections.observableArrayList();
            temp.add(new InHouse(1,"",2,2,2,2,2));
            allPartsTable.setItems(temp);
        } else{
            allPartsTable.setMouseTransparent(false);
            allPartsTable.setItems(Inventory.getAllParts());
            idColAll.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColAll.setCellValueFactory(new PropertyValueFactory<>("Name"));
            invColAll.setCellValueFactory(new PropertyValueFactory<>("Stock"));
            priceColAll.setCellValueFactory(new PropertyValueFactory<>("Price"));
        }

        if(!MainScreenController.modify || MainScreenController.selectedProduct == null || MainScreenController.selectedProduct.getAllAssociatedParts().isEmpty()){
            associatedPartsTable.setMouseTransparent(true);
            ObservableList<Part> temp = FXCollections.observableArrayList();
            temp.add(new InHouse(1,"",2,2,2,2,2));
            associatedPartsTable.setItems(temp);
        } else{
            associatedPartsTable.setMouseTransparent(false);
            Product.setTempAssociatedParts(MainScreenController.selectedProduct.getAllAssociatedParts());
            associatedPartsTable.setItems(Product.tempAssociatedParts);
            idColAssoc.setCellValueFactory(new PropertyValueFactory<>("Id"));
            nameColAssoc.setCellValueFactory(new PropertyValueFactory<>("Name"));
            invColAssoc.setCellValueFactory(new PropertyValueFactory<>("Stock"));
            priceColAssoc.setCellValueFactory(new PropertyValueFactory<>("Price"));
        }

        if(MainScreenController.modify){
            productTitle.setText("Modify Product");
            idTextField.setText(String.valueOf(MainScreenController.selectedProduct.getId()));
            nameTextField.setText(MainScreenController.selectedProduct.getName());
            inventoryTextField.setText(String.valueOf(MainScreenController.selectedProduct.getStock()));
            priceTextField.setText(String.valueOf(MainScreenController.selectedProduct.getPrice()));
            minTextField.setText(String.valueOf(MainScreenController.selectedProduct.getMin()));
            maxTextField.setText(String.valueOf(MainScreenController.selectedProduct.getMax()));
        }
    }

    /**
     * Removes the selected associated part when Remove button is clicked.
     * Implements a confirmation dialog box when button is clicked. If user confirms, performs deletion and sets table.
     * @param actionEvent Event caused by user clicking the remove button
     * */
    public void onRemovePart(ActionEvent actionEvent) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?");
        Optional<ButtonType> result = confirm.showAndWait();

        if(result.get() == ButtonType.OK) {
            Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
            if (selectedPart == null) {
                Alert warning = new Alert(Alert.AlertType.ERROR, "You must select a part!");
                warning.showAndWait();
            } else {
                boolean success = Product.deleteAssociatedPart(selectedPart);
                if (!success) {
                    Alert fail = new Alert(Alert.AlertType.ERROR, "Part could not be deleted.");
                    fail.showAndWait();
                }
            }
            if (Product.tempAssociatedParts.isEmpty()) {
                associatedPartsTable.setMouseTransparent(true);
                ObservableList<Part> temp = FXCollections.observableArrayList();
                temp.add(new InHouse(1, "", 2, 2, 2, 2, 2));
                idColAssoc.setCellValueFactory(null);
                nameColAssoc.setCellValueFactory(null);
                invColAssoc.setCellValueFactory(null);
                priceColAssoc.setCellValueFactory(null);
                associatedPartsTable.setItems(temp);
            }
        }
    }

    /**
     * Adds part to the associated parts list when user clicks the add button.
     * Alerts the user if trying to add without selecting a part.
     * @param actionEvent Event caused by user clicking the add button
     * */
    public void onAddPart(ActionEvent actionEvent) {
        if(Product.tempAssociatedParts.size()==0){
            idColAssoc.setCellValueFactory(new PropertyValueFactory<>("Id"));
            nameColAssoc.setCellValueFactory(new PropertyValueFactory<>("Name"));
            invColAssoc.setCellValueFactory(new PropertyValueFactory<>("Stock"));
            priceColAssoc.setCellValueFactory(new PropertyValueFactory<>("Price"));
        }
        associatedPartsTable.setMouseTransparent(false);
        if(allPartsTable.getSelectionModel().getSelectedItem() == null) {
            Alert warning = new Alert(Alert.AlertType.ERROR, "You must select a part!");
            warning.showAndWait();
        } else{
            Product.addAssociatedPart(allPartsTable.getSelectionModel().getSelectedItem());
            associatedPartsTable.setItems(Product.tempAssociatedParts);
        }

    }

    /**
     * Saves the entered product information when the user clicks the save button.
     * Creates a new product if adding, and replaces old product with new information if modifying.
     * Input validation performed before saving.
     * @param actionEvent Event caused by user clicking the save button
     * */
    public void onSave(ActionEvent actionEvent) throws IOException {
        String numFormatError="";
        int productId;
        try {
            String name = checkIfBlank("Name", nameTextField.getText());
            String inventoryS = checkIfBlank("Inventory", inventoryTextField.getText());
            String priceS = checkIfBlank("Price", priceTextField.getText());
            String maxS = checkIfBlank("Max", maxTextField.getText());
            String minS = checkIfBlank("Min", minTextField.getText());
            Product newProduct;

            numFormatError = "Inventory";
            int inventory = Integer.parseInt(inventoryS);
            numFormatError = "Price";
            double price = Double.parseDouble(priceS);
            numFormatError = "Max";
            int max = Integer.parseInt(maxS);
            numFormatError = "Min";
            int min = Integer.parseInt(minS);

            if(!(min<max)){throw new Exception("Min must be less than max!");}
            if(inventory<min || inventory>max){throw new Exception("Inventory must be between min and max!");}

            if(MainScreenController.modify){
                productId = MainScreenController.selectedProduct.getId();
                newProduct = new Product(productId,name,price,inventory,min,max);
                for(int i=0; i<Inventory.getAllProducts().size(); i++){
                    if(Inventory.getAllProducts().get(i).getId() == productId){Inventory.updateProduct(i, newProduct);}
                }
            } else{
                productId = Main.nextProductId;
                newProduct = new Product(productId,name,price,inventory,min,max);
                newProduct.saveAssociatedParts();
                Inventory.addProduct(newProduct);
                Main.nextProductId++;
            }

            MainScreenController.modify = false;
            this.backToMain();

        }  catch(NumberFormatException e){
            Alert typeException = new Alert(Alert.AlertType.ERROR, numFormatError + " must be a number!!");
            typeException.showAndWait();
        } catch(Exception e){
            Alert blankWarning = new Alert(Alert.AlertType.ERROR, e.getMessage());
            blankWarning.showAndWait();
        }
    }

    /**
     * Loads the main screen.
     * */
    private void backToMain() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
        Stage stage = (Stage) productTitle.getScene().getWindow();
        Scene scene = new Scene(root, 818, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends user back to main screen when the cancel button is clicked.
     * @param actionEvent Event cause by clicking the cancel button
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        MainScreenController.modify = false;
        this.backToMain();
    }

    /**
     * Checks if user input is blank and displays dialog box if blank. Used for validating inputs when saving.
     * @param input Name of input being checked, will be displayed to user in error message if needed
     * @param inputValue Value of user input being checked
     * */
    private String checkIfBlank(String input, String inputValue) throws Exception {
        if(inputValue.isBlank()){
            throw new Exception(input + " must not be blank!");
        }
        return inputValue;
    }


    /**
     * Searches parts for part ID or partial name entered by user and displays the results in the table.
     * If no search term entered, will display all parts in inventory.
     * @param actionEvent Event caused by user pressing enter while in the search box
     * */
    public void onSearch(ActionEvent actionEvent) {
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
        if(results.size() == 0){allPartsTable.setPlaceholder(new Label("There are no parts that match your search."));}
        allPartsTable.setItems(results);
    }
}
