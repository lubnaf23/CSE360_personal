package HW3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import HW2.Review;

/**
 * Represents a reviewer who can review questions, answers, and other content on the platform
 */
public class Reviewer {
    private String id;
    private String username;
    private String email;
    private boolean isTrusted;
    private Date createdAt;
    private Date lastActive;
    private int totalReviews;
    private List<Review> reviews;
    
    /**
     * Creates a new reviewer with the given username and email
     * @param username Username of the reviewer
     * @param email Email address of the reviewer
     */
    public Reviewer(String username, String email) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.isTrusted = false; // New reviewers start as untrusted
        this.createdAt = new Date();
        this.lastActive = new Date();
        this.totalReviews = 0;
        this.reviews = new ArrayList<>();
    }

    /**
     * Adds a review to the reviewer's history
     * @param review The review to add
     * @throws IllegalArgumentException if the review is null
     */
    public void addReview(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }
        reviews.add(review);
        totalReviews++;
        lastActive = new Date();
    }

    /**
     * Gets the reviewer's review history
     * @return A list of all reviews by the reviewer
     */
    public List<Review> getReviews() {
        return new ArrayList<>(reviews);
    }

    /**
     * Checks if the reviewer is active (has submitted a review in the last 30 days)
     * @return true if the reviewer is active, false otherwise
     */
    public boolean isActive() {
        Date now = new Date();
        // Check if reviewer has been active in the last 30 days (approximately)
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;
        return (now.getTime() - lastActive.getTime()) < thirtyDaysInMillis;
    }

    /**
     * Gets the reviewer's ID
     * @return The reviewer's ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Gets the reviewer's username
     * @return The reviewer's username
     */
    public String getUsername() { 
        return username; 
    }
    
    /**
     * Gets the reviewer's email address
     * @return The reviewer's email address
     */
    public String getEmail() { 
        return email; 
    }
    
    /**
     * Checks if the reviewer has trusted status
     * @return true if the reviewer is trusted, false otherwise
     */
    public boolean isTrusted() { 
        return isTrusted; 
    }
    
    /**
     * Gets the creation date of the reviewer account
     * @return The date the account was created
     */
    public Date getCreatedAt() { 
        return createdAt; 
    }
    
    /**
     * Gets the date when the reviewer was last active
     * @return The date of the reviewer's last activity
     */
    public Date getLastActive() { 
        return lastActive; 
    }
    
    /**
     * Gets the total number of reviews submitted by the reviewer
     * @return The reviewer's total review count
     */
    public int getTotalReviews() { 
        return totalReviews; 
    }

    /**
     * Sets the reviewer's username
     * @param username The new username
     * @throws IllegalArgumentException if the username is null or empty
     */
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.username = username.trim();
    }

    /**
     * Sets the reviewer's email address
     * @param email The new email address
     * @throws IllegalArgumentException if the email is null or empty
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        this.email = email.trim();
    }

    /**
     * Sets the reviewer's trusted status
     * @param trusted The new trusted status
     */
    public void setTrusted(boolean trusted) {
        this.isTrusted = trusted;
    }
    
    /**
     * Updates the last active timestamp to the current time
     */
    public void updateLastActive() {
        this.lastActive = new Date();
    }

    @Override
    public String toString() {
        String status = isTrusted ? "Trusted" : "Regular";
        return username + " (" + status + " reviewer) - Reviews: " + totalReviews;
    }
}