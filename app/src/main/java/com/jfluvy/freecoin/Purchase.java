package com.jfluvy.freecoin;

public class Purchase {

    private int date;
    private Double quantity;
    private Double price;

    public Purchase(){}

    public Purchase(int date,Double quantity, Double price){
        this.date=date;
        this.quantity=quantity;
        this.price=price;
    }


    public int getDate() {
        return date;
    }
    public Double getQuantity() {
        return quantity;
    }
    public Double getPrice(){return price;}

    public void setDate(int date) {
        this.date = date;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}
