package com.brendarono.bookmanager.data.model;

import java.io.Serializable;

public class Book implements Serializable {

    public Book() {
    }

    private String timestamp;

    private String name;

    private String author;

    private String price;

    private String year;

    private String code;

   // private Integer count;

    public Book(String timestamp, String name, String author, String price, String year, String code) {
        this.timestamp = timestamp;
        this.name = name;
        this.author = author;
        this.price = price;
        this.year = year;
        this.code = code;
        //this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
     **/

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
