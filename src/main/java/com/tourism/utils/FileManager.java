package com.tourism.utils;

import com.tourism.models.User;
import com.tourism.models.Attraction;
import com.tourism.models.Booking;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.txt";
    private static final String ATTRACTIONS_FILE = DATA_DIR + "attractions.txt";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.txt";
    
    public static void initializeDataFiles() {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get(DATA_DIR));
            
            // Initialize users file with default admin
            if (!Files.exists(Paths.get(USERS_FILE))) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
                    writer.println("saniya,saniya123,Saniya Admin,admin@tourism.com,+977-1-1234567,admin,,");
                    writer.println("tourist1,password123,John Doe,john@email.com,+977-1-1111111,tourist,,");
                    writer.println("guide1,password123,Ram Bahadur,ram@email.com,+977-1-2222222,guide,English Nepali,5 years");
                }
            }
            
            // Initialize attractions file
            if (!Files.exists(Paths.get(ATTRACTIONS_FILE))) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(ATTRACTIONS_FILE))) {
                    writer.println("Everest Base Camp,High (5000-7000m),Extreme,2500.00");
                    writer.println("Annapurna Circuit,Medium (3000-5000m),Hard,1800.00");
                    writer.println("Langtang Valley,Medium (3000-5000m),Medium,1200.00");
                    writer.println("Manaslu Circuit,High (5000-7000m),Hard,2000.00");
                    writer.println("Gokyo Lakes,High (5000-7000m),Hard,1900.00");
                }
            }
            
            // Initialize bookings file
            if (!Files.exists(Paths.get(BOOKINGS_FILE))) {
                Files.createFile(Paths.get(BOOKINGS_FILE));
            }
            
        } catch (IOException e) {
            System.err.println("Error initializing data files: " + e.getMessage());
        }
    }
    
    public static User authenticateUser(String username, String password) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    User user = User.fromString(line);
                    if (user != null && user.getUsername().equals(username.trim()) && 
                        user.getPassword().equals(password.trim())) {
                        return user;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean userExists(String username) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    User user = User.fromString(line);
                    if (user != null && user.getUsername().equals(username)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean saveUser(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE, true))) {
            writer.println(user.toString());
            return true;
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }
    
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    User user = User.fromString(line);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
          {
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }
    
    public static boolean updateUser(User user) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));
            List<String> updatedLines = new ArrayList<>();
            
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    User existingUser = User.fromString(line);
                    if (existingUser != null && existingUser.getUsername().equals(user.getUsername())) {
                        updatedLines.add(user.toString());
                    } else {
                        updatedLines.add(line);
                    }
                }
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
                for (String line : updatedLines) {
                    writer.println(line);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean deleteUser(String username) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));
            List<String> updatedLines = new ArrayList<>();
            
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    User user = User.fromString(line);
                    if (user == null || !user.getUsername().equals(username)) {
                        updatedLines.add(line);
                    }
                }
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
                for (String line : updatedLines) {
                    writer.println(line);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Attraction> loadAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(ATTRACTIONS_FILE));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Attraction attraction = Attraction.fromString(line);
                    if (attraction != null) {
                        attractions.add(attraction);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading attractions: " + e.getMessage());
        }
        return attractions;
    }
    
    public static boolean saveAttraction(Attraction attraction) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTRACTIONS_FILE, true))) {
            writer.println(attraction.toString());
            return true;
        } catch (IOException e) {
            System.err.println("Error saving attraction: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean updateAttraction(Attraction attraction) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(ATTRACTIONS_FILE));
            List<String> updatedLines = new ArrayList<>();
            
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Attraction existingAttraction = Attraction.fromString(line);
                    if (existingAttraction != null && existingAttraction.getName().equals(attraction.getName())) {
                        updatedLines.add(attraction.toString());
                    } else {
                        updatedLines.add(line);
                    }
                }
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(ATTRACTIONS_FILE))) {
                for (String line : updatedLines) {
                    writer.println(line);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error updating attraction: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean deleteAttraction(String attractionName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(ATTRACTIONS_FILE));
            List<String> updatedLines = new ArrayList<>();
            
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Attraction attraction = Attraction.fromString(line);
                    if (attraction == null || !attraction.getName().equals(attractionName)) {
                        updatedLines.add(line);
                    }
                }
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(ATTRACTIONS_FILE))) {
                for (String line : updatedLines) {
                    writer.println(line);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting attraction: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(BOOKINGS_FILE));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Booking booking = Booking.fromString(line);
                    if (booking != null) {
                        bookings.add(booking);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }
    
    public static boolean saveBooking(Booking booking) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE, true))) {
            writer.println(booking.toString());
            return true;
        } catch (IOException e) {
            System.err.println("Error saving booking: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean updateBooking(Booking booking) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(BOOKINGS_FILE));
            List<String> updatedLines = new ArrayList<>();
            
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Booking existingBooking = Booking.fromString(line);
                    if (existingBooking != null && existingBooking.getBookingId().equals(booking.getBookingId())) {
                        updatedLines.add(booking.toString());
                    } else {
                        updatedLines.add(line);
                    }
                }
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
                for (String line : updatedLines) {
                    writer.println(line);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error updating booking: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean deleteBooking(String bookingId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(BOOKINGS_FILE));
            List<String> updatedLines = new ArrayList<>();
            
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Booking booking = Booking.fromString(line);
                    if (booking == null || !booking.getBookingId().equals(bookingId)) {
                        updatedLines.add(line);
                    }
                }
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
                for (String line : updatedLines) {
                    writer.println(line);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            return false;
        }
    }
}
