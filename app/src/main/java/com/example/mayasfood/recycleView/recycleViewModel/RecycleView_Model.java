package com.example.mayasfood.recycleView.recycleViewModel;

import androidx.lifecycle.ViewModel;

public class RecycleView_Model {

    private String foodName,foodHeading, foodPrice, productId;
    private String foodImg, stars;
    private int foodImg1, isFav;

    public RecycleView_Model(String foodName, String foodHeading, String foodPrice, int foodImg) {
        this.foodName = foodName;
        this.foodHeading = foodHeading;
        this.foodPrice = foodPrice;
        this.foodImg1 = foodImg;
    }

    public RecycleView_Model(String foodName, String foodPrice, String productId, String foodImg, String stars) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.productId = productId;
        this.foodImg = foodImg;
        this.stars = stars;
    }

    public RecycleView_Model(String foodName, String foodPrice, String productId, String foodImg, String stars, int isFav) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.productId = productId;
        this.foodImg = foodImg;
        this.stars = stars;
        this.isFav = isFav;
    }

    public RecycleView_Model(String foodName, String foodPrice, String foodImg, String stars) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImg = foodImg;
        this.stars = stars;
    }

    public RecycleView_Model(String foodName, String foodHeading, String foodPrice) {
        this.foodName = foodName;
        this.foodHeading = foodHeading;
        this.foodPrice = foodPrice;
    }

    public RecycleView_Model(String foodName) {
        this.foodName = foodName;
    }

    public RecycleView_Model(String foodName, int foodImg) {
        this.foodName = foodName;
        this.foodImg1 = foodImg;
    }

    public RecycleView_Model(String foodName, String foodImg) {
        this.foodName = foodName;
        this.foodImg = foodImg;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public RecycleView_Model(int foodImg) {
        this.foodImg1 = foodImg;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodHeading() {
        return foodHeading;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public String getStars() {
        return stars;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodHeading(String foodHeading) {
        this.foodHeading = foodHeading;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodImg(int foodImg) {
        this.foodImg1 = foodImg;
    }
}
