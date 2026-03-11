package com.example.labs_11_3;

import java.io.Serializable;

public class Room implements Serializable {
    private String roomId;
    private String roomName;
    private double price;
    private boolean isOccupied; // true: Đã thuê, false: Còn trống
    private String tenantName;
    private String phoneNumber;

    public Room() {
    }

    public Room(String roomId, String roomName, double price, boolean isOccupied, String tenantName, String phoneNumber) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.isOccupied = isOccupied;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return isOccupied ? "Occupied" : "Available";
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", price=" + price +
                ", status=" + getStatus() +
                ", tenantName='" + tenantName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
