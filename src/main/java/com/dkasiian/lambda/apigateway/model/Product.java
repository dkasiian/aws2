package com.dkasiian.lambda.apigateway.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Product {
    private int id;
    private String name;
    private String pictureURL;
    private double price;

    public Product(final String json) {
        final Gson gson = new Gson();
        final Product request = gson.fromJson(json, Product.class);

        this.id = request.getId();
        this.name = request.getName();
        this.pictureURL = request.getPictureURL();
        this.price = request.getPrice();
    }

    public String toString() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
