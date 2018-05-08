package com.shm.dim.client.Models;

public class Order {

    private String OrderCode;
    private String ProductName;
    private String Quantity;
    private String Cost;
    private String Name;
    private String Address;
    private String Date;
    private String Time;
    private String Status;


    public Order(String orderCode, String productName, String quantity, String cost, String name,
                 String address, String date, String time, String status) {
        OrderCode = orderCode;
        ProductName = productName;
        Quantity = quantity;
        Cost = cost;
        Name = name;
        Address = address;
        Date = date;
        Time = time;
        Status = status;
    }


    public String getOrderCode() {
        return OrderCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getCost() {
        return Cost;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getStatus() {
        return Status;
    }
}