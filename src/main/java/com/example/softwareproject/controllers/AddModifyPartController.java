/**
 * Class that controls the part screen used to add or modify parts.
 *
 * @author Kelsey Holley
 * */
package com.example.softwareproject.controllers;

import com.example.softwareproject.Main;
import com.example.softwareproject.models.InHouse;
import com.example.softwareproject.models.Inventory;
import com.example.softwareproject.models.Outsourced;
import com.example.softwareproject.models.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddModifyPartController implements Initializable {
    public Label partTitle;
    public RadioButton inHouseRadio;
    public ToggleGroup partType;
    public RadioButton outsourcedRadio;
    public Label machineCompanyLabel;
    public TextField idTextField;
    public TextField nameTextField;
    public TextField inventoryTextField;
    public TextField priceTextField;
    public TextField maxTextField;
    public TextField minTextField;
    public StackPane textFieldStack;
    public TextField machineIdTextField;
    public TextField companyNameTextField;

    /**
     * Initializes components for the add/modify part screen.
     * Sets header text to "Add Part" or "Modify Part" depending on which button is clicked.
     * If modifying, sets text fields to contain the already existing data.
     * @param url
     * @param resourceBundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(MainScreenController.modify){
            partTitle.setText("Modify Part");
            idTextField.setText(String.valueOf(MainScreenController.selectedPart.getId()));
            nameTextField.setText(MainScreenController.selectedPart.getName());
            inventoryTextField.setText(String.valueOf(MainScreenController.selectedPart.getStock()));
            priceTextField.setText(String.valueOf(MainScreenController.selectedPart.getPrice()));
            maxTextField.setText(String.valueOf(MainScreenController.selectedPart.getMax()));
            minTextField.setText(String.valueOf(MainScreenController.selectedPart.getMin()));
            if(MainScreenController.selectedPart instanceof InHouse){
                inHouseRadio.setSelected(true);
                onInHouseSelect(new ActionEvent());
                machineIdTextField.setText(String.valueOf(((InHouse) MainScreenController.selectedPart).getMachineId()));
            } else{
                outsourcedRadio.setSelected(true);
                onOutsourcedSelect(new ActionEvent());
                companyNameTextField.setText(((Outsourced) MainScreenController.selectedPart).getCompanyName());
            }
        } else{
            machineIdTextField.setVisible(true);
            companyNameTextField.setVisible(false);
            companyNameTextField.toBack();
        }
    }

    /**
     * Saves the entered part information when the user clicks the save button.
     * Creates a new part if adding, and replaces old part with new information if modifying.
     * Input validation performed before saving.
     * @param actionEvent Event caused by user clicking the save button
     * */
    public void onSave(ActionEvent actionEvent) throws IOException {
        String numFormatError="";
        int partId;
        try {
            String name = checkIfBlank("Name", nameTextField.getText());
            String inventoryS = checkIfBlank("Inventory", inventoryTextField.getText());
            String priceS = checkIfBlank("Price", priceTextField.getText());
            String maxS = checkIfBlank("Max", maxTextField.getText());
            String minS = checkIfBlank("Min", minTextField.getText());
            Part newPart;

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
                partId = MainScreenController.selectedPart.getId();
            } else{
                partId = Main.nextPartId;
            }

            if (inHouseRadio.isSelected()) {
                String machineIdS = checkIfBlank("Machine ID", machineIdTextField.getText());
                numFormatError = "Machine ID";
                int machineId = Integer.parseInt(machineIdS);

                newPart = new InHouse(partId, name, price, inventory, min, max, machineId);
            } else{
                String companyName = checkIfBlank("Company Name", companyNameTextField.getText());
                newPart = new Outsourced(partId, name, price, inventory, min, max, companyName);
            }

            if(MainScreenController.modify){
                for(int i=0; i<Inventory.getAllParts().size(); i++){
                    if(Inventory.getAllParts().get(i).getId() == partId){Inventory.updatePart(i, newPart);}
                }
            }else{
                Inventory.addPart(newPart);
                Main.nextPartId++;
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
     * Sends user back to main screen when the cancel button is clicked.
     * @param actionEvent Event cause by clicking the cancel button
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        MainScreenController.modify = false;
        this.backToMain();
    }

    /**
     * Swaps the label and text field when the in-house radio button is selected.
     * @param actionEvent Event caused by the user selecting the in-house radio button
     * */
    public void onInHouseSelect(ActionEvent actionEvent) {
        machineCompanyLabel.setText("Machine ID");
        machineIdTextField.setVisible(true);
        companyNameTextField.setVisible(false);
        companyNameTextField.toBack();
    }

    /**
     * Swaps the label and text field when the outsourced radio button is selected.
     * @param actionEvent Event caused by the user selecting the outsourced radio button
     * */
    public void onOutsourcedSelect(ActionEvent actionEvent) {
        machineCompanyLabel.setText("Company Name");
        machineIdTextField.setVisible(false);
        companyNameTextField.setVisible(true);
        machineIdTextField.toBack();
    }

    /**
     * Loads the main screen.
     * */
    private void backToMain() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
        Stage stage = (Stage) partTitle.getScene().getWindow();
        Scene scene = new Scene(root, 818, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
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
}
