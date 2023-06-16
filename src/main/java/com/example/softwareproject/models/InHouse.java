package com.example.softwareproject.models;


/**
 * Class that defines an in-house part.
 *
 * @author Kelsey Holley
 * */
public class InHouse extends Part {
    private int machineId;


    /**
     * Constructor that creates a new in-house part
     * @param id ID number of part
     * @param name Name of the part
     * @param price Price of the part
     * @param stock Number of parts in inventory
     * @param min Minimum number of parts allowed in inventory
     * @param max Maximum number of parts allowed in inventory
     * @param machineId ID number of the machine that created the part
     * */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id,name,price,stock,min,max);
        this.machineId = machineId;
    }

    /**
     * Gets the ID number of the machine that created the part.
     * @return ID number of the machine that created the part
     * */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Sets the ID number of the machine that created the part.
     * @param machineId ID number to be set
     * */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
