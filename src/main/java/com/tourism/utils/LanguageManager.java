package com.tourism.utils;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private static LanguageManager instance;
    private String currentLanguage = "EN";
    private Map<String, String> englishTexts;
    private Map<String, String> nepaliTexts;
    
    private LanguageManager() {
        initializeTexts();
    }
    
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    private void initializeTexts() {
        englishTexts = new HashMap<>();
        nepaliTexts = new HashMap<>();
        
        // Login and Registration
        englishTexts.put("login.title", "Nepal Tourism Management System");
        nepaliTexts.put("login.title", "नेपाल पर्यटन व्यवस्थापन प्रणाली");
        
        englishTexts.put("login.username", "Username:");
        nepaliTexts.put("login.username", "प्रयोगकर्ता नाम:");
        
        englishTexts.put("login.password", "Password:");
        nepaliTexts.put("login.password", "पासवर्ड:");
        
        englishTexts.put("login.button", "Login");
        nepaliTexts.put("login.button", "लगइन");
        
        englishTexts.put("register.button", "Register");
        nepaliTexts.put("register.button", "दर्ता गर्नुहोस्");
        
        englishTexts.put("register.title", "Register New User");
        nepaliTexts.put("register.title", "नयाँ प्रयोगकर्ता दर्ता");
        
        englishTexts.put("back.button", "Back");
        nepaliTexts.put("back.button", "फिर्ता");
        
        // Common
        englishTexts.put("welcome", "Welcome");
        nepaliTexts.put("welcome", "स्वागतम्");
        
        englishTexts.put("languages", "Languages");
        nepaliTexts.put("languages", "भाषाहरू");
        
        englishTexts.put("experience", "Experience");
        nepaliTexts.put("experience", "अनुभव");
        
        englishTexts.put("earnings", "Earnings");
        nepaliTexts.put("earnings", "आम्दानी");
        
        englishTexts.put("logout.button", "Logout");
        nepaliTexts.put("logout.button", "लगआउट");
        
        // Booking
        englishTexts.put("book.button", "Book Now");
        nepaliTexts.put("book.button", "अहिले बुक गर्नुहोस्");
        
        englishTexts.put("update.button", "Update");
        nepaliTexts.put("update.button", "अपडेट गर्नुहोस्");
        
        englishTexts.put("delete.button", "Delete");
        nepaliTexts.put("delete.button", "मेटाउनुहोस्");
        
        englishTexts.put("add.button", "Add");
        nepaliTexts.put("add.button", "थप्नुहोस्");
        
        englishTexts.put("cancel.button", "Cancel");
        nepaliTexts.put("cancel.button", "रद्द गर्नुहोस्");
        
        // Messages
        englishTexts.put("booking.success", "Booking created successfully!");
        nepaliTexts.put("booking.success", "बुकिङ सफलतापूर्वक सिर्जना गरियो!");
        
        englishTexts.put("booking.failed", "Failed to create booking!");
        nepaliTexts.put("booking.failed", "बुकिङ सिर्जना गर्न असफल!");
        
        englishTexts.put("booking.updated", "Booking updated successfully!");
        nepaliTexts.put("booking.updated", "बुकिङ सफलतापूर्वक अपडेट गरियो!");
        
        englishTexts.put("booking.deleted", "Booking deleted successfully!");
        nepaliTexts.put("booking.deleted", "बुकिङ सफलतापूर्वक मेटाइयो!");
        
        // Festival Discount
        englishTexts.put("festival.discount.title", "Festival Discount Applied!");
        nepaliTexts.put("festival.discount.title", "चाडपर्व छुट लागू गरियो!");
        
        englishTexts.put("festival.discount.header", "Dashain & Tihar Special");
        nepaliTexts.put("festival.discount.header", "दशैं र तिहार विशेष");
        
        englishTexts.put("festival.discount.message", "You have received a 20% festival discount on your booking!");
        nepaliTexts.put("festival.discount.message", "तपाईंले आफ्नो बुकिङमा २०% चाडपर्व छुट पाउनुभएको छ!");
        
        // Altitude Warning
        englishTexts.put("altitude.warning.title", "High Altitude Warning");
        nepaliTexts.put("altitude.warning.title", "उच्च उचाइ चेतावनी");
        
        englishTexts.put("altitude.warning.header", "Safety Alert");
        nepaliTexts.put("altitude.warning.header", "सुरक्षा चेतावनी");
        
        englishTexts.put("altitude.warning.message", "This trek involves high altitude. Please ensure you are physically prepared and consult a doctor if necessary.");
        nepaliTexts.put("altitude.warning.message", "यो ट्रेकमा उच्च उचाइ समावेश छ। कृपया तपाईं शारीरिक रूपमा तयार हुनुहुन्छ भनी सुनिश्चित गर्नुहोस् र आवश्यक भएमा डाक्टरसँग सल्लाह लिनुहोस्।");
        
        // Weather and Updates
        englishTexts.put("weather.alert", "Weather Alerts:");
        nepaliTexts.put("weather.alert", "मौसम चेतावनी:");
        
        englishTexts.put("heavy.snow.everest", "Heavy snow expected on Everest trek routes");
        nepaliTexts.put("heavy.snow.everest", "एभरेस्ट ट्रेक मार्गहरूमा भारी हिउँ पर्ने अपेक्षा");
        
        englishTexts.put("clear.weather.annapurna", "Clear weather conditions in Annapurna region");
        nepaliTexts.put("clear.weather.annapurna", "अन्नपूर्ण क्षेत्रमा सफा मौसम अवस्था");
        
        englishTexts.put("emergency.notices", "Emergency Notices:");
        nepaliTexts.put("emergency.notices", "आपतकालीन सूचनाहरू:");
        
        englishTexts.put("rescue.helicopter.available", "Rescue helicopter services available 24/7");
        nepaliTexts.put("rescue.helicopter.available", "उद्धार हेलिकप्टर सेवा २४/७ उपलब्ध");
        
        englishTexts.put("medical.checkup.required", "Medical checkup required for high altitude treks");
        nepaliTexts.put("medical.checkup.required", "उच्च उचाइ ट्रेकका लागि चिकित्सा जाँच आवश्यक");
        
        // Analytics
        englishTexts.put("total.revenue", "Total Revenue");
        nepaliTexts.put("total.revenue", "कुल आम्दानी");
        
        englishTexts.put("bookings", "Bookings");
        nepaliTexts.put("bookings", "बुकिङहरू");
        
        // Error Messages
        englishTexts.put("error.empty.fields", "Please fill in all required fields!");
        nepaliTexts.put("error.empty.fields", "कृपया सबै आवश्यक फिल्डहरू भर्नुहोस्!");
        
        englishTexts.put("error.invalid.credentials", "Invalid username or password!");
        nepaliTexts.put("error.invalid.credentials", "गलत प्रयोगकर्ता नाम वा पासवर्ड!");
        
        englishTexts.put("error.username.exists", "Username already exists!");
        nepaliTexts.put("error.username.exists", "प्रयोगकर्ता नाम पहिले नै अवस्थित छ!");
        
        englishTexts.put("error.past.date", "Cannot book for past dates!");
        nepaliTexts.put("error.past.date", "विगतका मितिहरूका लागि बुक गर्न सकिँदैन!");
        
        englishTexts.put("error.empty.booking.fields", "Please select attraction, date, and difficulty!");
        nepaliTexts.put("error.empty.booking.fields", "कृपया आकर्षण, मिति र कठिनाई चयन गर्नुहोस्!");
        
        englishTexts.put("error.guide.fields.required", "Languages and experience are required for guides!");
        nepaliTexts.put("error.guide.fields.required", "गाइडहरूका लागि भाषा र अनुभव आवश्यक छ!");
        
        englishTexts.put("error.invalid.price", "Please enter a valid price!");
        nepaliTexts.put("error.invalid.price", "कृपया मान्य मूल्य प्रविष्ट गर्नुहोस्!");
        
        englishTexts.put("error.title", "Error");
        nepaliTexts.put("error.title", "त्रुटि");
        
        englishTexts.put("success.title", "Success");
        nepaliTexts.put("success.title", "सफलता");
        
        englishTexts.put("success.registration", "Registration successful!");
        nepaliTexts.put("success.registration", "दर्ता सफल!");
        
        englishTexts.put("error.registration.failed", "Registration failed!");
        nepaliTexts.put("error.registration.failed", "दर्ता असफल!");
        
        // Selection Messages
        englishTexts.put("select.booking.update", "Please select a booking to update!");
        nepaliTexts.put("select.booking.update", "कृपया अपडेट गर्न बुकिङ चयन गर्नुहोस्!");
        
        englishTexts.put("select.booking.delete", "Please select a booking to delete!");
        nepaliTexts.put("select.booking.delete", "कृपया मेटाउन बुकिङ चयन गर्नुहोस्!");
        
        englishTexts.put("select.guide.update", "Please select a guide to update!");
        nepaliTexts.put("select.guide.update", "कृपया अपडेट गर्न गाइड चयन गर्नुहोस्!");
        
        englishTexts.put("select.guide.delete", "Please select a guide to delete!");
        nepaliTexts.put("select.guide.delete", "कृपया मेटाउन गाइड चयन गर्नुहोस्!");
        
        englishTexts.put("select.attraction.update", "Please select an attraction to update!");
        nepaliTexts.put("select.attraction.update", "कृपया अपडेट गर्न आकर्षण चयन गर्नुहोस्!");
        
        englishTexts.put("select.attraction.delete", "Please select an attraction to delete!");
        nepaliTexts.put("select.attraction.delete", "कृपया मेटाउन आकर्षण चयन गर्नुहोस्!");
        
        englishTexts.put("select.booking.cancel", "Please select a booking to cancel!");
        nepaliTexts.put("select.booking.cancel", "कृपया रद्द गर्न बुकिङ चयन गर्नुहोस्!");
        
        // Confirmation Messages
        englishTexts.put("confirm.delete", "Confirm Delete");
        nepaliTexts.put("confirm.delete", "मेटाउने पुष्टि गर्नुहोस्");
        
        englishTexts.put("delete.booking.confirm", "Are you sure you want to delete this booking?");
        nepaliTexts.put("delete.booking.confirm", "के तपाईं यो बुकिङ मेटाउन निश्चित हुनुहुन्छ?");
        
        englishTexts.put("delete.guide.confirm", "Are you sure you want to delete this guide?");
        nepaliTexts.put("delete.guide.confirm", "के तपाईं यो गाइड मेटाउन निश्चित हुनुहुन्छ?");
        
        englishTexts.put("delete.attraction.confirm", "Are you sure you want to delete this attraction?");
        nepaliTexts.put("delete.attraction.confirm", "के तपाईं यो आकर्षण मेटाउन निश्चित हुनुहुन्छ?");
        
        englishTexts.put("cancel.booking.confirm", "Are you sure you want to cancel this booking?");
        nepaliTexts.put("cancel.booking.confirm", "के तपाईं यो बुकिङ रद्द गर्न निश्चित हुनुहुन्छ?");
        
        englishTexts.put("action.cannot.undone", "This action cannot be undone.");
        nepaliTexts.put("action.cannot.undone", "यो कार्य पूर्ववत गर्न सकिँदैन।");
        
        // Success Messages for Admin
        englishTexts.put("guide.added.success", "Guide added successfully!");
        nepaliTexts.put("guide.added.success", "गाइड सफलतापूर्वक थपियो!");
        
        englishTexts.put("guide.updated.success", "Guide updated successfully!");
        nepaliTexts.put("guide.updated.success", "गाइड सफलतापूर्वक अपडेट गरियो!");
        
        englishTexts.put("guide.deleted.success", "Guide deleted successfully!");
        nepaliTexts.put("guide.deleted.success", "गाइड सफलतापूर्वक मेटाइयो!");
        
        englishTexts.put("attraction.added.success", "Attraction added successfully!");
        nepaliTexts.put("attraction.added.success", "आकर्षण सफलतापूर्वक थपियो!");
        
        englishTexts.put("attraction.updated.success", "Attraction updated successfully!");
        nepaliTexts.put("attraction.updated.success", "आकर्षण सफलतापूर्वक अपडेट गरियो!");
        
        englishTexts.put("attraction.deleted.success", "Attraction deleted successfully!");
        nepaliTexts.put("attraction.deleted.success", "आकर्षण सफलतापूर्वक मेटाइयो!");
        
        englishTexts.put("booking.cancelled.success", "Booking cancelled successfully!");
        nepaliTexts.put("booking.cancelled.success", "बुकिङ सफलतापूर्वक रद्द गरियो!");
        
        // Error Messages for Admin
        englishTexts.put("guide.add.failed", "Failed to add guide!");
        nepaliTexts.put("guide.add.failed", "गाइड थप्न असफल!");
        
        englishTexts.put("guide.update.failed", "Failed to update guide!");
        nepaliTexts.put("guide.update.failed", "गाइड अपडेट गर्न असफल!");
        
        englishTexts.put("guide.delete.failed", "Failed to delete guide!");
        nepaliTexts.put("guide.delete.failed", "गाइड मेटाउन असफल!");
        
        englishTexts.put("attraction.add.failed", "Failed to add attraction!");
        nepaliTexts.put("attraction.add.failed", "आकर्षण थप्न असफल!");
        
        englishTexts.put("attraction.update.failed", "Failed to update attraction!");
        nepaliTexts.put("attraction.update.failed", "आकर्षण अपडेट गर्न असफल!");
        
        englishTexts.put("attraction.delete.failed", "Failed to delete attraction!");
        nepaliTexts.put("attraction.delete.failed", "आकर्षण मेटाउन असफल!");
        
        englishTexts.put("booking.update.failed", "Failed to update booking!");
        nepaliTexts.put("booking.update.failed", "बुकिङ अपडेट गर्न असफल!");
        
        englishTexts.put("booking.delete.failed", "Failed to delete booking!");
        nepaliTexts.put("booking.delete.failed", "बुकिङ मेटाउन असफल!");
        
        englishTexts.put("booking.cancel.failed", "Failed to cancel booking!");
        nepaliTexts.put("booking.cancel.failed", "बुकिङ रद्द गर्न असफल!");
    }
    
    public String getText(String key) {
        if ("NP".equals(currentLanguage)) {
            return nepaliTexts.getOrDefault(key, englishTexts.getOrDefault(key, key));
        } else {
            return englishTexts.getOrDefault(key, key);
        }
    }
    
    public void toggleLanguage() {
        currentLanguage = "EN".equals(currentLanguage) ? "NP" : "EN";
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
