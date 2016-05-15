package com.antuansoft.mongodb.domain;

import org.jongo.marshall.jackson.oid.MongoId;

import java.util.Date;
import java.util.List;

/**
 * Created by amit on 5/6/16.
 */
public class Order {

    @MongoId
    String id;
    Date date;
    String custInfo;

    List<Product> items;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustInfo() {
        return custInfo;
    }

    public void setCustInfo(String custInfo) {
        this.custInfo = custInfo;
    }

    public List<Product> getProducts() {
        return items;
    }

    public void setProducts(List<Product> products) {
        this.items = items;
    }
}

class Product {
    int quantity;
    int price;
    String desc;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
