package com.example.softwareproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of the inventory management program.
 * <p>
 * FUTURE ENHANCEMENT: If the application was to be updated, the biggest enhancement would be the ability to store data
 * for more than one run. It would also be nice to store pictures of the parts and products, as well as locations of
 * the parts within the warehouse or storage facility. Additionally, the application could implement some sharing
 * capabilities like being able to print the inventory, export as a pdf, or share digitally in some way.
 * <p>
 * RUNTIME ERROR: Many errors were experienced throughout the course of this project, but one that kept reoccurring was
 * the NullPointerException. Much of the functionality depends on arrays and when it tried to access the elements of an
 * empty or null array the program would crash. To solve the issue, I would first try reading through the code and trying
 * to find the place where a null array was trying to be accessed. If that didn't work, I used the debugger to step
 * through the code and see the value of the list at each step. Once I figured out where the issue was occurring, usually
 * the solution was to add a null check before completing that section of code. In one specific instance, I had done a
 * null check but then reassigned the array before actually accessing the element that I needed. By assigning the element
 * to a variable after checking if the array was null but before reassigning the array, I was able to get the element
 * I needed.
 * <p>
 * JavaDocs location: Resources directory of the project
 *
 * @author Kelsey Holley
 * */

public class Main extends Application {

    public static int nextPartId = 1;
    public static int nextProductId = 1;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 818, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}