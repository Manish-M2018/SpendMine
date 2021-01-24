package com.example.spendmine;

public class MyListData {
    private String name,city,coins;

    public MyListData(String name, String city, String coins) {
        this.name = name;
        this.city = city;
        this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCoins() {
        return coins;
    }
}
