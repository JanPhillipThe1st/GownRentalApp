package com.yamatoapps.gownrentalapp;

public class Gown {
    public String name = "";
    public  String description = "";
    public  String size = "";
    public Double price = 0.00;
    public String image_url = "";

    public String id = "";


    public Gown(String name, String description, String size, Double price, String image_url, String id) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.price = price;
        this.image_url = image_url;
        this.id = id;
    }
}
