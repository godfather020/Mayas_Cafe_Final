package com.example.mayasfood.recycleView.recycleViewModel;

import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleView_Model {

    private String foodName,foodHeading, foodPrice, productId;
    private String foodImg, stars;
    private int foodImg1, isFav;
    private String runOrder_num , runOrder_total, runOrder_pickup, runOrder_create, runOrder_Quantity, runOrder_status,
            runOrder_date, orderRating, orderComment, order_id, foodSize;
    String runOrder_img, offerAmt;

    public RecycleView_Model(String orderId ,String runOrder_num, String runOrder_total, String runOrder_pickup, String runOrder_create, String runOrder_Quantity, String runOrder_status, String runOrder_date, String runOrder_img) {
        this.runOrder_num = runOrder_num;
        this.runOrder_total = runOrder_total;
        this.runOrder_pickup = runOrder_pickup;
        this.runOrder_create = runOrder_create;
        this.runOrder_Quantity = runOrder_Quantity;
        this.runOrder_status = runOrder_status;
        this.runOrder_date = runOrder_date;
        this.runOrder_img = runOrder_img;
        this.order_id = orderId;
    }

    public RecycleView_Model(String runOrder_num, String runOrder_total, String runOrder_pickup, String runOrder_create, String runOrder_Quantity, String runOrder_status, String runOrder_date, String runOrder_img, String orderRating, String orderComment) {
        this.runOrder_num = runOrder_num;
        this.runOrder_total = runOrder_total;
        this.runOrder_pickup = runOrder_pickup;
        this.runOrder_create = runOrder_create;
        this.runOrder_Quantity = runOrder_Quantity;
        this.runOrder_status = runOrder_status;
        this.runOrder_date = runOrder_date;
        this.runOrder_img = runOrder_img;
        this.orderRating = orderRating;
        this.orderComment = orderComment;
    }

    public RecycleView_Model(String foodName, String foodHeading, String foodPrice, int foodImg) {
        this.foodName = foodName;
        this.foodHeading = foodHeading;
        this.foodPrice = foodPrice;
        this.foodImg1 = foodImg;
    }

    public RecycleView_Model(String foodSize, String foodName, String foodHeading, String foodPrice, int foodImg) {
        this.foodName = foodName;
        this.foodHeading = foodHeading;
        this.foodPrice = foodPrice;
        this.foodImg1 = foodImg;
        this.foodSize = foodSize;
    }

    public RecycleView_Model(String offerAmt, String foodName, String foodPrice, String productId, String foodImg, String stars) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.productId = productId;
        this.foodImg = foodImg;
        this.stars = stars;
        this.offerAmt = offerAmt;
    }

    public RecycleView_Model(String foodSize, String offerAmt, String foodName, String foodPrice, String productId, String foodImg, String stars) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.productId = productId;
        this.foodImg = foodImg;
        this.stars = stars;
        this.offerAmt = offerAmt;
        this.foodSize = foodSize;
    }

    public RecycleView_Model(String foodName, String foodPrice, String productId, String foodImg, String stars) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.productId = productId;
        this.foodImg = foodImg;
        this.stars = stars;
    }

    public RecycleView_Model(String foodSize, String offerAmt, String foodName, String foodPrice, String productId, String foodImg, String stars, int isFav) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.productId = productId;
        this.foodImg = foodImg;
        this.stars = stars;
        this.isFav = isFav;
        this.offerAmt = offerAmt;
        this.foodSize = foodSize;
    }

    public RecycleView_Model(String offerAmt, String foodName, String foodPrice, String productId, String foodImg, String stars, int isFav) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.productId = productId;
        this.foodImg = foodImg;
        this.stars = stars;
        this.isFav = isFav;
        this.offerAmt = offerAmt;
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

    public String getRunOrder_num() {
        return runOrder_num;
    }

    public void setRunOrder_num(String runOrder_num) {
        this.runOrder_num = runOrder_num;
    }

    public String getRunOrder_total() {
        return runOrder_total;
    }

    public void setRunOrder_total(String runOrder_total) {
        this.runOrder_total = runOrder_total;
    }

    public String getRunOrder_pickup() {
        return runOrder_pickup;
    }

    public void setRunOrder_pickup(String runOrder_pickup) {
        this.runOrder_pickup = runOrder_pickup;
    }

    public String getFoodSize() {
        return foodSize;
    }

    public void setFoodSize(String foodSize) {
        this.foodSize = foodSize;
    }

    public String getRunOrder_create() {
        return runOrder_create;
    }

    public void setRunOrder_create(String runOrder_create) {
        this.runOrder_create = runOrder_create;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRunOrder_Quantity() {
        return runOrder_Quantity;
    }

    public void setRunOrder_Quantity(String runOrder_Quantity) {
        this.runOrder_Quantity = runOrder_Quantity;
    }

    public String getRunOrder_status() {
        return runOrder_status;
    }

    public void setRunOrder_status(String runOrder_status) {
        this.runOrder_status = runOrder_status;
    }

    public String getRunOrder_date() {
        return runOrder_date;
    }

    public void setRunOrder_date(String runOrder_date) {
        this.runOrder_date = runOrder_date;
    }

    public String getRunOrder_img() {
        return runOrder_img;
    }

    public void setRunOrder_img(String runOrder_img) {
        this.runOrder_img = runOrder_img;
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

    public int getFoodImg1() {
        return foodImg1;
    }

    public void setFoodImg1(int foodImg1) {
        this.foodImg1 = foodImg1;
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

    public String getOrderRating() {
        return orderRating;
    }

    public void setOrderRating(String orderRating) {
        this.orderRating = orderRating;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public String getOfferAmt() {
        return offerAmt;
    }

    public void setOfferAmt(String offerAmt) {
        this.offerAmt = offerAmt;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }
}
