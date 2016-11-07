package com.example.erikiado.myapplication.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private String myData;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }
}