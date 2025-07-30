package com.tourism.models;

public class Booking {
    private String bookingId;
    private String touristName;
    private String attractionName;
    private String date;
    private String difficulty;
    private double price;
    private String status;
    
    public Booking(String bookingId, String touristName, String attractionName, 
                   String date, String difficulty, double price, String status) {
        this.bookingId = bookingId;
        this.touristName = touristName;
        this.attractionName = attractionName;
        this.date = date;
        this.difficulty = difficulty;
        this.price = price;
        this.status = status;
    }
    
    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public String getTouristName() { return touristName; }
    public void setTouristName(String touristName) { this.touristName = touristName; }
    
    public String getAttractionName() { return attractionName; }
    public void setAttractionName(String attractionName) { this.attractionName = attractionName; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return bookingId + "," + touristName + "," + attractionName + "," + 
               date + "," + difficulty + "," + price + "," + status;
    }
    
    public static Booking fromString(String line) {
        String[] parts = line.split(",", 7);
        if (parts.length == 7) {
            return new Booking(parts[0], parts[1], parts[2], parts[3], 
                             parts[4], Double.parseDouble(parts[5]), parts[6]);
        }
        return null;
    }
}
