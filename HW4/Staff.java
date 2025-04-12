package HW3;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import HW3.Review;
import HW3.Reviews;

/**
 * Represents a staff member who has administrative privileges to manage the platform,
 * including reviewers, system settings, and monitoring activity.
 */
public class Staff {
    private String id;
    private String username;
    private String email;
    private Date createdAt;
    private Map<String, Object> systemSettings; // For system settings
    private List<ActivityLog> activityLogs; // For logging activities
    private List<String> trustedReviewers; // List of trusted reviewer usernames
    private Reviews reviewsSystem; // Reference to the reviews system

    /**
     * Creates a new staff member with the given username and email
     * @param username Username of the staff member
     * @param email Email address of the staff member
     * @param reviewsSystem Reference to the Reviews system
     */
    public Staff(String username, String email, Reviews reviewsSystem) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.createdAt = new Date();
        this.systemSettings = new HashMap<>();
        this.activityLogs = new ArrayList<>();
        this.trustedReviewers = new ArrayList<>();
        this.reviewsSystem = reviewsSystem;
        
        // Initialize default system settings
        initializeDefaultSettings();
    }

    /**
     * Initializes the default system settings
     */
    private void initializeDefaultSettings() {
        systemSettings.put("reviewWeightage", 1.0);
        systemSettings.put("trustedReviewerWeightage", 1.5);
        systemSettings.put("reviewVisibilityBeforeApproval", false);
        systemSettings.put("minReviewsForTrustedStatus", 10);
    }

    /**
     * Gets a list of all reviewers in the system
     * @return A list of all unique reviewer usernames
     */
    public List<String> getAllReviewers() {
        List<String> allReviewers = reviewsSystem.getReviews().stream()
                .map(Review::getReviewer)
                .distinct()
                .collect(Collectors.toList());
        
        logActivity("Viewed all reviewers");
        return allReviewers;
    }
    
    /**
     * Deletes a reviewer account from the system by removing all their reviews
     * @param reviewerUsername Username of the reviewer to be deleted
     * @return true if any reviews were deleted, false otherwise
     */
    public boolean deleteReviewer(String reviewerUsername) {
        // Get all reviews by this reviewer
        List<Review> reviewerReviews = reviewsSystem.getReviewsByReviewer(reviewerUsername);
        
        if (reviewerReviews.isEmpty()) {
            return false;
        }
        
        // Remove all reviews by this reviewer
        for (Review review : reviewerReviews) {
            reviewsSystem.removeReview(review);
        }
        
        // Remove from trusted reviewers list if present
        trustedReviewers.remove(reviewerUsername);
        
        logActivity("Deleted reviewer: " + reviewerUsername);
        return true;
    }
    
    /**
     * Updates a system setting with the given key and value
     * @param key Name of the setting to update
     * @param value New value for the setting
     * @throws IllegalArgumentException if the key or value is null, or if the value is invalid for the setting
     */
    public void updateSystemSetting(String key, Object value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Setting key and value cannot be null");
        }
        
        // Validate settings
        if (key.equals("reviewWeightage") || key.equals("trustedReviewerWeightage")) {
            if (!(value instanceof Double) || (Double) value <= 0) {
                throw new IllegalArgumentException("Weightage must be a positive number");
            }
        } else if (key.equals("reviewVisibilityBeforeApproval")) {
            if (!(value instanceof Boolean)) {
                throw new IllegalArgumentException("Visibility setting must be a boolean");
            }
        } else if (key.equals("minReviewsForTrustedStatus")) {
            if (!(value instanceof Integer) || (Integer) value < 0) {
                throw new IllegalArgumentException("Minimum reviews must be a positive integer");
            }
        }
        
        systemSettings.put(key, value);
        logActivity("Updated system setting: " + key + " to " + value);
    }
    
    /**
     * Gets the value of a system setting
     * @param key Name of the setting to retrieve
     * @return The value of the setting, or null if the setting doesn't exist
     */
    public Object getSystemSetting(String key) {
        return systemSettings.get(key);
    }
    
    /**
     * Gets all system settings
     * @return A map of all system settings
     */
    public Map<String, Object> getAllSystemSettings() {
        return new HashMap<>(systemSettings);
    }
    
    /**
     * Gets all activity logs in the system
     * @return A list of all activity logs
     */
    public List<ActivityLog> getActivityLogs() {
        return new ArrayList<>(activityLogs);
    }
    
    /**
     * Gets activity logs within the specified date range
     * @param startDate Start date for the range
     * @param endDate End date for the range
     * @return A list of activity logs within the date range
     */
    public List<ActivityLog> getActivityLogsByDateRange(Date startDate, Date endDate) {
        return activityLogs.stream()
                .filter(log -> !log.getTimestamp().before(startDate) && !log.getTimestamp().after(endDate))
                .collect(Collectors.toList());
    }
    
    /**
     * Demotes a trusted reviewer to a regular reviewer
     * @param reviewerUsername Username of the reviewer to demote
     * @return true if the reviewer was successfully demoted, false if the reviewer wasn't found or wasn't trusted
     */
    public boolean demoteTrustedReviewer(String reviewerUsername) {
        boolean wasRemoved = trustedReviewers.remove(reviewerUsername);
        
        if (wasRemoved) {
            logActivity("Demoted trusted reviewer: " + reviewerUsername);
        }
        
        return wasRemoved;
    }
    
    /**
     * Adds a reviewer to the trusted reviewers list
     * @param reviewerUsername Username of the reviewer to promote
     * @return true if the reviewer was added, false if they were already trusted
     */
    public boolean promoteTrustedReviewer(String reviewerUsername) {
        // Check if the reviewer exists
        List<Review> reviewerReviews = reviewsSystem.getReviewsByReviewer(reviewerUsername);
        
        if (reviewerReviews.isEmpty()) {
            return false; // Reviewer doesn't exist
        }
        
        // Check if reviewer meets minimum review count requirement
        int minReviews = (Integer) getSystemSetting("minReviewsForTrustedStatus");
        if (reviewerReviews.size() < minReviews) {
            logActivity("Failed to promote reviewer (insufficient reviews): " + reviewerUsername);
            return false;
        }
        
        // Add to trusted reviewers if not already there
        if (!trustedReviewers.contains(reviewerUsername)) {
            trustedReviewers.add(reviewerUsername);
            logActivity("Promoted reviewer to trusted status: " + reviewerUsername);
            return true;
        }
        
        return false; // Already trusted
    }
    
    /**
     * Checks if a reviewer is trusted
     * @param reviewerUsername Username of the reviewer to check
     * @return true if the reviewer is trusted, false otherwise
     */
    public boolean isReviewerTrusted(String reviewerUsername) {
        return trustedReviewers.contains(reviewerUsername);
    }
    
    /**
     * Gets all trusted reviewers
     * @return A list of trusted reviewer usernames
     */
    public List<String> getTrustedReviewers() {
        return new ArrayList<>(trustedReviewers);
    }
    
    /**
     * Logs an activity performed by this staff member
     * @param action Description of the action performed
     */
    private void logActivity(String action) {
        ActivityLog log = new ActivityLog(this.username, action, new Date());
        activityLogs.add(log);
    }
    
    // Getters
    /**
     * Gets the ID of the staff member
     * @return The staff member's ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Gets the username of the staff member
     * @return The staff member's username
     */
    public String getUsername() { 
        return username; 
    }
    
    /**
     * Gets the email address of the staff member
     * @return The staff member's email address
     */
    public String getEmail() { 
        return email; 
    }
    
    /**
     * Gets the creation date of the staff member account
     * @return The date the account was created
     */
    public Date getCreatedAt() { 
        return createdAt; 
    }
    
    /**
     * Represents a system activity log entry
     */
    public static class ActivityLog {
        private String id;
        private String staffUsername;
        private String action;
        private Date timestamp;
        
        /**
         * Creates a new activity log entry
         * @param staffUsername Username of the staff member who performed the action
         * @param action Description of the action
         * @param timestamp Time when the action was performed
         */
        public ActivityLog(String staffUsername, String action, Date timestamp) {
            this.id = UUID.randomUUID().toString();
            this.staffUsername = staffUsername;
            this.action = action;
            this.timestamp = timestamp;
        }
        
        /**
         * Gets the ID of the log entry
         * @return The log entry ID
         */
        public String getId() { 
            return id; 
        }
        
        /**
         * Gets the username of the staff member who performed the action
         * @return The staff member's username
         */
        public String getStaffUsername() { 
            return staffUsername; 
        }
        
        /**
         * Gets the description of the action
         * @return The action description
         */
        public String getAction() { 
            return action; 
        }
        
        /**
         * Gets the timestamp when the action was performed
         * @return The action timestamp
         */
        public Date getTimestamp() { 
            return timestamp; 
        }
        
        @Override
        public String toString() {
            return "[" + timestamp + "] " + staffUsername + ": " + action;
        }
    }
}