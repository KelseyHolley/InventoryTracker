package com.example.softwareproject.models;


/**
 * Class that defines an outsourced part.
 *
 * @author Kelsey Holley
 * */
public class Outsourced extends Part {
    private String companyName;

    /**
     * Constructor that creates a new outsourced part
     * @param id ID number of part
     * @param name Name of the part
     * @param price Price of the part
     * @param stock Number of parts in inventory
     * @param min Minimum number of parts allowed in inventory
     * @param max Maximum number of parts allowed in inventory
     * @param companyName Name of company that supplied the part
     * */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id,name,price,stock,min,max);
        this.companyName = companyName;
    }

    /**
     * Gets the name of the company that supplied the part.
     * @return Name of the company that supplied the part.
     * */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the name of the company that supplied the part.
     * @param companyName New company name
     * */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
