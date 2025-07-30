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
            writer.write("Username: " + user.getUsername().trim() + "\n");
            writer.write("Password: " + user.getPassword().trim() + "\n");  // Trim password
            writer.write("Full Name: " + user.getFullName().trim() + "\n");
            writer.write("Email: " + user.getEmail().trim() + "\n");
            writer.write("Phone: " + user.getPhone().trim() + "\n");
            writer.write("Role: " + user.getRole().trim() + "\n");
            
            if ("Guide".equals(user.getRole())) {
                writer.write("Languages: " + user.getLanguages().trim() + "\n");
                writer.write("Experience: " + user.getExperience().trim() + "\n");
            }
            
            writer.write("------------------------\n");
            
            // Debug output
            System.out.println("Saved user to " + fileName + ": " + user.getUsername().trim());
            return true;
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static User authenticateUser(String username, String password, String fileName) {
        // Create file if it doesn't exist
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new file: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        System.out.println("Authenticating user: '" + username + "' in file: " + fileName);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: ")) {
                    String fileUsername = line.substring(10).trim();
                    String passwordLine = reader.readLine();
                    
                    if (passwordLine == null || !passwordLine.startsWith("Password: ")) {
                        continue;
                    }
                    
                    String filePassword = passwordLine.substring(10).trim();
                    
                    System.out.println("Found user: '" + fileUsername + "' with password: '" + filePassword + "'");
                    System.out.println("Comparing with input: '" + username + "' / '" + password + "'");
                    
                    if (fileUsername.equals(username)) {
                        // Simple string comparison with trimmed values
                        if (password.equals(filePassword)) {
                            System.out.println("Password matched for user: " + username);
                            
                            // Read the rest of user data
                            String fullNameLine = reader.readLine();
                            String emailLine = reader.readLine();
                            String phoneLine = reader.readLine();
                            String roleLine = reader.readLine();
                            
                            if (fullNameLine == null || emailLine == null || phoneLine == null || roleLine == null) {
                                System.out.println("Incomplete user data for: " + username);
                                continue;
                            }
                            
                            String fullName = fullNameLine.startsWith("Full Name: ") ? fullNameLine.substring(11).trim() : "";
                            String email = emailLine.startsWith("Email: ") ? emailLine.substring(7).trim() : "";
                            String phone = phoneLine.startsWith("Phone: ") ? phoneLine.substring(7).trim() : "";
                            String role = roleLine.startsWith("Role: ") ? roleLine.substring(6).trim() : "";
                            
                            User user = new User(fileUsername, filePassword, fullName, email, phone, role);
                            
                            if ("Guide".equals(role)) {
                                String languagesLine = reader.readLine();
                                String experienceLine = reader.readLine();
                                
                                if (languagesLine != null && experienceLine != null) {
                                    String languages = languagesLine.startsWith("Languages: ") ? languagesLine.substring(11).trim() : "";
                                    String experience = experienceLine.startsWith("Experience: ") ? experienceLine.substring(12).trim() : "";
                                    user.setLanguages(languages);
                                    user.setExperience(experience);
                                }
                            }
                            
                            System.out.println("Successfully authenticated user: " + username);
                            return user;
                        } else {
                            System.out.println("Password mismatch for user: " + username);
                            System.out.println("Expected: '" + filePassword + "' (length: " + filePassword.length() + ")");
                            System.out.println("Got: '" + password + "' (length: " + password.length() + ")");
                            
                            // Debug: Print character codes
                            System.out.print("Expected chars: ");
                            for (char c : filePassword.toCharArray()) {
                                System.out.print((int)c + " ");
                            }
                            System.out.println();
                            
                            System.out.print("Got chars: ");
                            for (char c : password.toCharArray()) {
                                System.out.print((int)c + " ");
                            }
                            System.out.println();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Authentication failed for user: " + username);
        return null;
    }
    
    public static boolean userExists(String username) {
        boolean existsInTourists = getUserByUsername(username, "tourists.txt") != null;
        boolean existsInGuides = getUserByUsername(username, "guides.txt") != null;
        
        System.out.println("Checking if user exists: " + username + 
                          " (Tourists: " + existsInTourists + ", Guides: " + existsInGuides + ")");
        
        return existsInTourists || existsInGuides;
    }
    
    public static User getUserByUsername(String username, String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: ")) {
                    String fileUsername = line.substring(10).trim();
                    
                    if (fileUsername.equals(username)) {
                        String passwordLine = reader.readLine();
                        String fullNameLine = reader.readLine();
                        String emailLine = reader.readLine();
                        String phoneLine = reader.readLine();
                        String roleLine = reader.readLine();
                        
                        if (passwordLine == null || fullNameLine == null || emailLine == null || 
                            phoneLine == null || roleLine == null) {
                            continue;
                        }
                        
                        String password = passwordLine.startsWith("Password: ") ? passwordLine.substring(10).trim() : "";
                        String fullName = fullNameLine.startsWith("Full Name: ") ? fullNameLine.substring(11).trim() : "";
                        String email = emailLine.startsWith("Email: ") ? emailLine.substring(7).trim() : "";
                        String phone = phoneLine.startsWith("Phone: ") ? phoneLine.substring(7).trim() : "";
                        String role = roleLine.startsWith("Role: ") ? roleLine.substring(6).trim() : "";
                        
                        User user = new User(fileUsername, password, fullName, email, phone, role);
                        
                        if ("Guide".equals(role)) {
                            String languagesLine = reader.readLine();
                            String experienceLine = reader.readLine();
                            
                            if (languagesLine != null && experienceLine != null) {
                                String languages = languagesLine.startsWith("Languages: ") ? languagesLine.substring(11).trim() : "";
                                String experience = experienceLine.startsWith("Experience: ") ? experienceLine.substring(12).trim() : "";
                                user.setLanguages(languages);
                                user.setExperience(experience);
                            }
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
    
    // Initialize with sample data if files don't exist
    public static void initializeSampleData() {
        // Create sample tourist
        File touristFile = new File("tourists.txt");
        if (!touristFile.exists() || touristFile.length() == 0) {
            User sampleTourist = new User("tourist1", "password123", "John Doe", "john@example.com", "+977-1234567890", "Tourist");
            saveUser(sampleTourist, "tourists.txt");
            System.out.println("Created sample tourist: tourist1/password123");
        }
        
        // Create sample guide
        File guideFile = new File("guides.txt");
        if (!guideFile.exists() || guideFile.length() == 0) {
            User sampleGuide = new User("guide1", "password123", "Ram Sharma", "ram@example.com", "+977-9876543210", "Guide");
            sampleGuide.setLanguages("English, Nepali");
            sampleGuide.setExperience("5 years");
            saveUser(sampleGuide, "guides.txt");
            System.out.println("Created sample guide: guide1/password123");
        }
        
        // Create sample attractions
        File attractionFile = new File("attractions.txt");
        if (!attractionFile.exists() || attractionFile.length() == 0) {
            saveAttraction(new Attraction("Everest Base Camp", "High", "Hard", 1500.0));
            saveAttraction(new Attraction("Annapurna Circuit", "High", "Medium", 1200.0));
            saveAttraction(new Attraction("Chitwan National Park", "Low", "Easy", 800.0));
            saveAttraction(new Attraction("Pokhara Lake", "Low", "Easy", 600.0));
            System.out.println("Created sample attractions");
        }
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
        
        File file = new File("attractions.txt");
        if (!file.exists()) {
            return attractions;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader("attractions.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    String name = line.substring(6).trim();
                    String altitudeLine = reader.readLine();
                    String difficultyLine = reader.readLine();
                    String priceLine = reader.readLine();
                    
                    if (altitudeLine != null && difficultyLine != null && priceLine != null) {
                        String altitude = altitudeLine.startsWith("Altitude: ") ? altitudeLine.substring(10).trim() : "";
                        String difficulty = difficultyLine.startsWith("Difficulty: ") ? difficultyLine.substring(12).trim() : "";
                        String priceStr = priceLine.startsWith("Base Price: ") ? priceLine.substring(12).trim() : "0";
                        
                        try {
                            double basePrice = Double.parseDouble(priceStr);
                            attractions.add(new Attraction(name, altitude, difficulty, basePrice));
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing price for attraction: " + name);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        
        File file = new File("bookings.txt");
        if (!file.exists()) {
            return bookings;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Booking ID: ")) {
                    String bookingId = line.substring(12).trim();
                    String touristLine = reader.readLine();
                    
                    if (touristLine != null && touristLine.startsWith("Tourist: ")) {
                        String tourist = touristLine.substring(9).trim();
                        
                        if (tourist.equals(username)) {
                            String attractionLine = reader.readLine();
                            String dateLine = reader.readLine();
                            String difficultyLine = reader.readLine();
                            String priceLine = reader.readLine();
                            String statusLine = reader.readLine();
                            
                            if (attractionLine != null && dateLine != null && difficultyLine != null && 
                                priceLine != null && statusLine != null) {
                                
                                String attraction = attractionLine.startsWith("Attraction: ") ? attractionLine.substring(12).trim() : "";
                                String date = dateLine.startsWith("Date: ") ? dateLine.substring(6).trim() : "";
                                String difficulty = difficultyLine.startsWith("Difficulty: ") ? difficultyLine.substring(12).trim() : "";
                                String priceStr = priceLine.startsWith("Price: ") ? priceLine.substring(7).trim() : "0";
                                String status = statusLine.startsWith("Status: ") ? statusLine.substring(8).trim() : "";
                                
                                try {
                                    double price = Double.parseDouble(priceStr);
                                    bookings.add(new Booking(bookingId, tourist, attraction, date, difficulty, price, status));
                                } catch (NumberFormatException e) {
                                    System.out.println("Error parsing price for booking: " + bookingId);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    public static List<Booking> loadAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        
        File file = new File("bookings.txt");
        if (!file.exists()) {
            return bookings;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Booking ID: ")) {
                    String bookingId = line.substring(12).trim();
                    String touristLine = reader.readLine();
                    String attractionLine = reader.readLine();
                    String dateLine = reader.readLine();
                    String difficultyLine = reader.readLine();
                    String priceLine = reader.readLine();
                    String statusLine = reader.readLine();
                    
                    if (touristLine != null && attractionLine != null && dateLine != null && 
                        difficultyLine != null && priceLine != null && statusLine != null) {
                        
                        String tourist = touristLine.startsWith("Tourist: ") ? touristLine.substring(9).trim() : "";
                        String attraction = attractionLine.startsWith("Attraction: ") ? attractionLine.substring(12).trim() : "";
                        String date = dateLine.startsWith("Date: ") ? dateLine.substring(6).trim() : "";
                        String difficulty = difficultyLine.startsWith("Difficulty: ") ? difficultyLine.substring(12).trim() : "";
                        String priceStr = priceLine.startsWith("Price: ") ? priceLine.substring(7).trim() : "0";
                        String status = statusLine.startsWith("Status: ") ? statusLine.substring(8).trim() : "";
                        
                        try {
                            double price = Double.parseDouble(priceStr);
                            bookings.add(new Booking(bookingId, tourist, attraction, date, difficulty, price, status));
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing price for booking: " + bookingId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        
        File file = new File("guides.txt");
        if (!file.exists()) {
            return guides;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader("guides.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Username: ")) {
                    String username = line.substring(10).trim();
                    String passwordLine = reader.readLine();
                    String fullNameLine = reader.readLine();
                    String emailLine = reader.readLine();
                    String phoneLine = reader.readLine();
                    String roleLine = reader.readLine();
                    String languagesLine = reader.readLine();
                    String experienceLine = reader.readLine();
                    
                    if (passwordLine != null && fullNameLine != null && emailLine != null && 
                        phoneLine != null && roleLine != null && languagesLine != null && experienceLine != null) {
                        
                        String password = passwordLine.startsWith("Password: ") ? passwordLine.substring(10).trim() : "";
                        String fullName = fullNameLine.startsWith("Full Name: ") ? fullNameLine.substring(11).trim() : "";
                        String email = emailLine.startsWith("Email: ") ? emailLine.substring(7).trim() : "";
                        String phone = phoneLine.startsWith("Phone: ") ? phoneLine.substring(7).trim() : "";
                        String role = roleLine.startsWith("Role: ") ? roleLine.substring(6).trim() : "";
                        String languages = languagesLine.startsWith("Languages: ") ? languagesLine.substring(11).trim() : "";
                        String experience = experienceLine.startsWith("Experience: ") ? experienceLine.substring(12).trim() : "";
                        
                        User guide = new User(username, password, fullName, email, phone, role);
                        guide.setLanguages(languages);
                        guide.setExperience(experience);
                        guides.add(guide);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
