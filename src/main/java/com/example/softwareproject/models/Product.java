package com.example.softwareproject.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that defines Products.
 *
 * @author Kelsey Holley
 * */

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public static ObservableList<Part> tempAssociatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;


    /**
     * Constructor that creates a new Product.
     * @param id Product ID
     * @param name Name of the product
     * @param price Price of each product
     * @param stock Number of products in inventory
     * @param min Minimum number of products allowed in inventory
     * @param max Maximum number of products allowed in inventory
     * */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the ID number of the product.
     * @return ID number
     * */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID number of the product.
     * @param id New ID number
     * */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the product.
     * @return Name
     * */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     * @param name New name
     * */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product.
     * @return Price
     * */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     * @param price New price
     * */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the inventory level of the product.
     * @return Inventory level
     * */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the inventory level of the product.
     * @param stock New inventory level
     * */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the minimum inventory level.
     * @return Minimum inventory level
     * */
    public int getMin() {
        return min;
    }

    /**
     * Sets the minimum inventory level of the product.
     * @param min New minimum inventory level
     * */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets the maximum inventory level.
     * @return Maximum inventory level
     * */
    public int getMax() {
        return max;
    }

    /**
     * Sets the maximum inventory level of the product.
     * @param max New maximum inventory level
     * */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds a part to the temporary associated part list.
     * @param part Part to be added
     * */
    public static void addAssociatedPart(Part part){
        tempAssociatedParts.add(part);
    }

    /**
     * Deletes a part from the temporary associated part list.
     * @param selectedAssociatedPart Part to be deleted
     * @return True if delete was successful, False if it wasn't
     * */
    public static boolean deleteAssociatedPart(Part selectedAssociatedPart){
        Part removedPart = null;
        for(int i=0; i<tempAssociatedParts.size(); i++){
            if(tempAssociatedParts.get(i).getId() == selectedAssociatedPart.getId()){removedPart = tempAssociatedParts.remove(i);}
            if(removedPart != null){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of associated parts for the product.
     * @return List of associated parts
     * */
    public ObservableList<Part> getAllAssociatedParts(){
        return this.associatedParts;
    }

    /**
     * Saves the temporary list of associated parts to the product.
     * */
    public void saveAssociatedParts(){
        associatedParts.addAll(tempAssociatedParts);
        tempAssociatedParts.clear();
    }

    /**
     * Sets the list of temporary associated parts.
     * Clears the old list and replaces it with the new list. Used when switching between main and add/modify product.
     * @param currentAssociatedParts List of associated parts to add to temporary list, empty if new product
     * */
    public static void setTempAssociatedParts(ObservableList<Part> currentAssociatedParts){
        tempAssociatedParts.clear();
        tempAssociatedParts.addAll(currentAssociatedParts);
    }
}
