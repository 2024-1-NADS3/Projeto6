package com.example.frontend;

import java.io.Serializable;

public class Curso implements Serializable {
    private int id;
    private String title;
    private String type;
    private String category;
    private String img;
    private String description;
    private String address;
    private String zone;
    private int occupiedSlots;
    private int maxCapacity;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() { return description; }

    public String getAddress() {
        return address;
    }

    public String getZone() { return zone; }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setDescription(String description) { this.description = description; }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZone(String zone) { this.zone = zone; }

    public void setOccupiedSlots(int occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public String toString() {
        return
                "id=" + id + ", title='" + title + '\'' +
                        ", category='" + category + '\'' +
                        ", img='" + img + '\'' +
                        ", address='" + address + '\'' + ", zone='" + zone + '\'' +
                        ", occupiedSlots=" + occupiedSlots +
                        ", maxCapacity=" + maxCapacity
                ;
    }

    public Curso(int id, String title, String type, String category, String img, String description, String address, int occupiedSlots, int maxCapacity) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.category = category;
        this.img = img;
        this.description = description;
        this.address = address;
        this.occupiedSlots = occupiedSlots;
        this.maxCapacity = maxCapacity;
    }

    public Curso() {}
}