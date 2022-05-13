package com.example.mayasfood.recycleView.recycleViewModel;

public class RecycleView_Model {


    private String foodName,foodHeading, foodPrice;
    private String foodImg, stars;
    private int foodImg1;

    public RecycleView_Model(String foodName, String foodHeading, String foodPrice, int foodImg) {
        this.foodName = foodName;
        this.foodHeading = foodHeading;
        this.foodPrice = foodPrice;
        this.foodImg1 = foodImg;
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
