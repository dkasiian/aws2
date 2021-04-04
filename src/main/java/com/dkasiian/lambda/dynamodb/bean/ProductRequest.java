package com.dkasiian.lambda.dynamodb.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProductRequest {
    private int id;
    private String name;
    private String pictureURL;
    private double price;

    public static void main(String[] args) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setId(1);
        productRequest.setName("Juice");
        productRequest.setPictureURL("https://www.example.com");
        productRequest.setPrice(30.0D);

        System.out.println(productRequest);
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
