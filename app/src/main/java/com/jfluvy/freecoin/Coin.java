package com.jfluvy.freecoin;

import java.io.Serializable;

public class Coin implements Serializable {
    private String non;
    private String name;
    private double quantity;
    private double price;
    private double value;
    private double aPrice;
    private double aValue;
    private String icon;
    private String coinMarketCap;

    public Coin(){}

    public Coin(String non,String name,double quantity,double price,double value,String icon){
        this.non=non;
        this.name=name;
        this.quantity=quantity;
        this.price=price;
        this.value=value;
        this.icon=icon;
        this.coinMarketCap="0";
    }

    public String getNon(){return non;}
    public String getName(){return name;}
    public double getQuantity(){return quantity;}
    public double getPrice(){return price;}
    public double getValue(){return value;}
    public double getaPrice(){return aPrice;}
    public double getaValue(){return aValue;}
    public String getIcon(){return icon;}
    public String getCoinMarketCap(){return coinMarketCap;}

    public void setNon(String non) {
        this.non = non;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public void setaPrice(double aPrice) {
        this.aPrice = aPrice;
    }
    public void setaValue(double aValue) {
        this.aValue = aValue;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public void setCoinMarketCap(String coinMarketCap) {
        this.coinMarketCap = coinMarketCap;
    }
}
