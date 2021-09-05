package com.example.hackathon;


public class ProductDetails{
    public String product_image;
    public String product_name;
    public int product_price;
    public String shopping_site;

    public ProductDetails(String product_image, String product_name, int product_price, String shopping_site) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_price = product_price;
        this.shopping_site = shopping_site;
    }
}
