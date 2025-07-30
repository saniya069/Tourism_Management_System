package com.tourism.utils;

import com.tourism.models.Attraction;
import com.tourism.models.Booking;
import com.tourism.models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    
    public static boolean saveUser(User user, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("Username: " + user.getUsername() + "\n");
            writer.write("Password: " + user.getPassword() + "\n");
            writer.write("Full Name: " + user.getFullName() + "\n");
            writer.write("Email: " + user.getEmail() + "\n");
            writer.write("Phone: " + user.getPhone() + "\n");
            writer.write("Role: " + user.getRole() + "\n");
            
            if ("Guide".equals(user.getRole())) {
                writer.write("Languages: " + user.getLanguages() + "\n");
                writer.write("Experience: " + user.getExperience() + "\n");
            }
            
            writer.write("------------------------\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static User authenticateUser(String username, String password, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            User user = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: ")) {
                    String fileUsername = line.substring(10);
                    String filePassword = reader.readLine().substring(10);
                    
                    if (fileUsername.equals(username) && PasswordUtils.verifyPassword(password, filePassword)) {
                        String fullName = reader.readLine().substring(11);
                        String email = reader.readLine().substring(7);
                        String phone = reader.readLine().substring(7);
                        String role = reader.readLine().substring(6);
                        
                        user = new User(fileUsername, filePassword, fullName, email, phone, role);
                        
                        if ("Guide".equals(role)) {
                            String languages = reader.readLine().substring(11);
                            String experience = reader.readLine().substring(12);
                            user.setLanguages(languages);
                            user.setExperience(experience);
                        }
                        
                        return user;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean userExists(String username) {
        return getUserByUsername(username, "tourists.txt") != null || 
               getUserByUsername(username, "guides.txt") != null;
    }
    
    public static User getUserByUsername(String username, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: ")) {
                    String fileUsername = line.substring(10);
                    
                    if (fileUsername.equals(username)) {
                        String password = reader.readLine().substring(10);
                        String fullName = reader.readLine().substring(11);
                        String email = reader.readLine().substring(7);
                        String phone = reader.readLine().substring(7);
                        String role = reader.readLine().substring(6);
                        
                        User user = new User(fileUsername, password, fullName, email, phone, role);
                        
                        if ("Guide".equals(role)) {
                            String languages = reader.readLine().substring(11);
                            String experience = reader.readLine().substring(12);
                            user.setLanguages(languages);
                            user.setExperience(experience);
                        }
                        
                        return user;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean saveAttraction(Attraction attraction) {
        try (FileWriter writer = new FileWriter("attractions.txt", true)) {
            writer.write("Name: " + attraction.getName() + "\n");
            writer.write("Altitude: " + attraction.getAltitude() + "\n");
            writer.write("Difficulty: " + attraction.getDifficulty() + "\n");
            writer.write("Base Price: " + attraction.getBasePrice() + "\n");
            writer.write("------------------------\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Attraction> loadAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("attractions.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    String name = line.substring(6);
                    String altitude = reader.readLine().substring(10);
                    String difficulty = reader.readLine().substring(12);
                    double basePrice = Double.parseDouble(reader.readLine().substring(12));
                    
                    attractions.add(new Attraction(name, altitude, difficulty, basePrice));
                }
            }
        } catch (IOException e) {
            // File might not exist yet, return empty list
        }
        
        return attractions;
    }
    
    public static Attraction getAttractionByName(String name) {
        List<Attraction> attractions = loadAttractions();
        return attractions.stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    public static boolean saveBooking(Booking booking) {
        try (FileWriter writer = new FileWriter("bookings.txt", true)) {
            writer.write("Booking ID: " + booking.getBookingId() + "\n");
            writer.write("Tourist: " + booking.getTouristName() + "\n");
            writer.write("Attraction: " + booking.getAttractionName() + "\n");
            writer.write("Date: " + booking.getDate() + "\n");
            writer.write("Difficulty: " + booking.getDifficulty() + "\n");
            writer.write("Price: " + booking.getPrice() + "\n");
            writer.write("Status: " + booking.getStatus() + "\n");
            writer.write("------------------------\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Booking> loadUserBookings(String username) {
        List<Booking> bookings = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Booking ID: ")) {
                    String bookingId = line.substring(12);
                    String tourist = reader.readLine().substring(9);
                    
                    if (tourist.equals(username)) {
                        String attraction = reader.readLine().substring(12);
                        String date = reader.readLine().substring(6);
                        String difficulty = reader.readLine().substring(12);
                        double price = Double.parseDouble(reader.readLine().substring(7));
                        String status = reader.readLine().substring(8);
                        
                        bookings.add(new Booking(bookingId, tourist, attraction, date, difficulty, price, status));
                    }
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        }
        
        return bookings;
    }
    
    public static List<Booking> loadAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Booking ID: ")) {
                    String bookingId = line.substring(12);
                    String tourist = reader.readLine().substring(9);
                    String attraction = reader.readLine().substring(12);
                    String date = reader.readLine().substring(6);
                    String difficulty = reader.readLine().substring(12);
                    double price = Double.parseDouble(reader.readLine().substring(7));
                    String status = reader.readLine().substring(8);
                    
                    bookings.add(new Booking(bookingId, tourist, attraction, date, difficulty, price, status));
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        }
        
        return bookings;
    }
    
    public static List<Booking> loadGuideBookings(String guideName) {
        // For simplicity, randomly assign some bookings to guides
        // In a real system, you'd have guide assignments in the booking data
        List<Booking> allBookings = loadAllBookings();
        List<Booking> guideBookings = new ArrayList<>();
        
        // Assign every 3rd booking to this guide (mock assignment)
        for (int i = 0; i < allBookings.size(); i += 3) {
            guideBookings.add(allBookings.get(i));
        }
        
        return guideBookings;
    }
    
    public static List<User> loadGuides() {
        List<User> guides = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("guides.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: ")) {
                    String username = line.substring(10);
                    String password = reader.readLine().substring(10);
                    String fullName = reader.readLine().substring(11);
                    String email = reader.readLine().substring(7);
                    String phone = reader.readLine().substring(7);
                    String role = reader.readLine().substring(6);
                    String languages = reader.readLine().substring(11);
                    String experience = reader.readLine().substring(12);
                    
                    User guide = new User(username, password, fullName, email, phone, role);
                    guide.setLanguages(languages);
                    guide.setExperience(experience);
                    guides.add(guide);
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        }
        
        return guides;
    }
    
    public static boolean updateBooking(Booking booking) {
        List<Booking> bookings = loadAllBookings();
        
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId().equals(booking.getBookingId())) {
                bookings.set(i, booking);
                return saveAllBookings(bookings);
            }
        }
        
        return false;
    }
    
    public static boolean deleteBooking(String bookingId) {
        List<Booking> bookings = loadAllBookings();
        bookings.removeIf(b -> b.getBookingId().equals(bookingId));
        return saveAllBookings(bookings);
    }
    
    public static boolean updateGuide(User guide) {
        List<User> guides = loadGuides();
        
        for (int i = 0; i < guides.size(); i++) {
            if (guides.get(i).getUsername().equals(guide.getUsername())) {
                guides.set(i, guide);
                return saveAllGuides(guides);
            }
        }
        
        return false;
    }
    
    public static boolean deleteGuide(String username) {
        List<User> guides = loadGuides();
        guides.removeIf(g -> g.getUsername().equals(username));
        return saveAllGuides(guides);
    }
    
    public static boolean updateAttraction(Attraction attraction) {
        List<Attraction> attractions = loadAttractions();
        
        for (int i = 0; i < attractions.size(); i++) {
            if (attractions.get(i).getName().equals(attraction.getName())) {
                attractions.set(i, attraction);
                return saveAllAttractions(attractions);
            }
        }
        
        return false;
    }
    
    public static boolean deleteAttraction(String name) {
        List<Attraction> attractions = loadAttractions();
        attractions.removeIf(a -> a.getName().equals(name));
        return saveAllAttractions(attractions);
    }
    
    private static boolean saveAllBookings(List<Booking> bookings) {
        try (FileWriter writer = new FileWriter("bookings.txt")) {
            for (Booking booking : bookings) {
                writer.write("Booking ID: " + booking.getBookingId() + "\n");
                writer.write("Tourist: " + booking.getTouristName() + "\n");
                writer.write("Attraction: " + booking.getAttractionName() + "\n");
                writer.write("Date: " + booking.getDate() + "\n");
                writer.write("Difficulty: " + booking.getDifficulty() + "\n");
                writer.write("Price: " + booking.getPrice() + "\n");
                writer.write("Status: " + booking.getStatus() + "\n");
                writer.write("------------------------\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean saveAllGuides(List<User> guides) {
        try (FileWriter writer = new FileWriter("guides.txt")) {
            for (User guide : guides) {
                writer.write("Username: " + guide.getUsername() + "\n");
                writer.write("Password: " + guide.getPassword() + "\n");
                writer.write("Full Name: " + guide.getFullName() + "\n");
                writer.write("Email: " + guide.getEmail() + "\n");
                writer.write("Phone: " + guide.getPhone() + "\n");
                writer.write("Role: " + guide.getRole() + "\n");
                writer.write("Languages: " + guide.getLanguages() + "\n");
                writer.write("Experience: " + guide.getExperience() + "\n");
                writer.write("------------------------\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean saveAllAttractions(List<Attraction> attractions) {
        try (FileWriter writer = new FileWriter("attractions.txt")) {
            for (Attraction attraction : attractions) {
                writer.write("Name: " + attraction.getName() + "\n");
                writer.write("Altitude: " + attraction.getAltitude() + "\n");
                writer.write("Difficulty: " + attraction.getDifficulty() + "\n");
                writer.write("Base Price: " + attraction.getBasePrice() + "\n");
                writer.write("------------------------\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
