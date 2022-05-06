package com.example.mayasfood.recycleView.recycleViewModel;

public class RecycleView_Model {


    private String foodName,foodHeading, foodPrice;
    private int foodImg, stars;

    public RecycleView_Model(String foodName, String foodHeading, String foodPrice, int foodImg) {
        this.foodName = foodName;
        this.foodHeading = foodHeading;
        this.foodPrice = foodPrice;
        this.foodImg = foodImg;
    }

    public RecycleView_Model(String foodName, String foodPrice, int foodImg, int stars) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImg = foodImg;
        this.stars = stars;
    }

    public RecycleView_Model(String foodName) {
        this.foodName = foodName;
    }

    public RecycleView_Model(String foodName, int foodImg) {
        this.foodName = foodName;
        this.foodImg = foodImg;
    }

    public RecycleView_Model(String foodName, String foodHeading) {
        this.foodName = foodName;
        this.foodHeading = foodHeading;
    }

    public RecycleView_Model(int foodImg) {
        this.foodImg = foodImg;
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

    public int getFoodImg() {
        return foodImg;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
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
        this.foodImg = foodImg;
    }
}
