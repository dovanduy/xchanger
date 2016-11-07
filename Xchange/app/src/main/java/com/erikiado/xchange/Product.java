package com.erikiado.xchange;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by erikiado on 11/7/16.
 */
@IgnoreExtraProperties
public class Product {

    public String name;
    public int hintPrice;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(String name, int hintPrice) {
        this.name = name;
        this.hintPrice = hintPrice;
    }

}