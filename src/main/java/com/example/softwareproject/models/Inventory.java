package com.example.softwareproject.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Class that defines the inventory held by the company.
 *
 * @author Kelsey Holley
 * */

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a part to the inventory.
     * @param newPart New part to be added to the inventory.
     * */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * Adds a product to the inventory.
     * @param newProduct New product to be added to the inventory.
     * */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * Returns a part given the part ID number.
     * @param partId ID number of the part to be found
     * @return Part matching the provided part ID number
     * */
    public static Part lookupPart(int partId){
        for (Part allPart : allParts) {
            if (allPart.getId() == partId) {
                return allPart;
            }
        }
        return null;
    }

    /**
     * Returns a product given the product ID number.
     * @param productId ID number of the product to be found
     * @return Product matching the provided product ID number
     * */
    public static Product lookupProduct(int productId){
        for (Product allProduct : allProducts) {
            if (allProduct.getId() == productId) {
                return allProduct;
            }
        }
        return null;
    }

    /**
     * Returns a list of parts that contain the search term in their name.
     * @param partName String to be searched for
     * @return List of parts that contain the given string within their name
     * */
    public static ObservableList<Part> lookupPart(String partName){
        if(partName.isBlank()){
            return allParts;
        } else{
            ObservableList<Part> results = FXCollections.observableArrayList();
            for(Part part : allParts){
                if(part.getName().contains(partName)){
                    results.add(part);
                }
            }
            return results;
        }
    }

    /**
     * Returns a list of products that contain the search term in their name.
     * @param productName String to be searched for
     * @return List of products that contain the given string within their name
     * */
    public static ObservableList<Product> lookupProduct(String productName){
        if(productName.isBlank()){
            return allProducts;
        } else{
            ObservableList<Product> results = FXCollections.observableArrayList();
            for(Product product : allProducts){
                if(product.getName().contains(productName)){
                    results.add(product);
                }
            }
            return results;
        }
    }

    /**
     * Updates the part in the inventory at the given index by replacing it with a new part with the updated fields.
     * @param index Index of the part to be updated
     * @param selectedPart New part to replace the one currently at that index
     * */
    public static void updatePart (int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * Updates the product in the inventory at the given index by replacing it with a new product with the updated fields.
     * @param index Index of the product to be updated
     * @param newProduct New product to replace the one currently at that index
     * */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
        allProducts.get(index).saveAssociatedParts();
    }

    /**
     * Deletes the provided part from the inventory.
     * @param selectedPart Part to be deleted
     * @return True if successfully deleted, False if not
     * */
    public static boolean deletePart(Part selectedPart){
        Part removedPart = null;
        for(int i=0; i<allParts.size(); i++){
            if(allParts.get(i).getId() == selectedPart.getId()){removedPart = allParts.remove(i);}
        }
        return removedPart != null;
    }

    /**
     * Deletes the provided product from the inventory.
     * @param selectedProduct Product to be deleted
     * @return True if successfully deleted, False if not
     * */
    public static boolean deleteProduct(Product selectedProduct) throws Exception {
        Product removedProduct = null;
        for(int i=0; i<allProducts.size(); i++){
            if(allProducts.get(i).getId() == selectedProduct.getId()){
                if(allProducts.get(i).getAllAssociatedParts().size()>0){
                    throw new Exception("You may only delete products with no associated parts!");
                } else{
                    removedProduct = allProducts.remove(i);
                }
            }
        }
        return removedProduct != null;
    }

    /**
     * Returns all parts in inventory.
     * @return List of all parts in inventory
     * */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Returns all products in inventory.
     * @return List of all products in inventory
     * */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
