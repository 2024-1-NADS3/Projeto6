package com.example.frontend;

import java.io.Serializable;

public class Curso implements Serializable {
    private int courseid;
    private String title;
    private String type;
    private String category;
    private String img;
    private String description;
    private String address;
    private String zone;
    private int occupiedSlots;
    private int maxCapacity;

    private String initialDate;

    private String endDate;

    public int getCourseid() {
        return courseid;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getZone() {
        return zone;
    }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setOccupiedSlots(int occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return
                "id=" + courseid + ", title='" + title + '\'' +
                        ", category='" + category + '\'' +
                        ", img='" + img + '\'' +
                        ", address='" + address + '\'' + ", zone='" + zone + '\'' +
                        ", occupiedSlots=" + occupiedSlots +
                        ", maxCapacity=" + maxCapacity
                ;
    }


    public Curso(int courseid, String title, String type, String category, String img, String description, String address, String zone, int occupiedSlots, int maxCapacity, String initialDate, String endDate) {
        this.courseid = courseid;
        this.title = title;
        this.type = type;
        this.category = category;
        this.img = img;
        this.description = description;
        this.address = address;
        this.zone = zone;
        this.occupiedSlots = occupiedSlots;
        this.maxCapacity = maxCapacity;
        this.initialDate = initialDate;
        this.endDate = endDate;
    }


    public Curso() {}
}