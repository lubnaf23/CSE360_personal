package HW3;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import java.util.Map;

import HW2.Review;
import HW2.Reviews;

/**
 * Unit tests for the Staff class
 */
public class StaffTest {
    
    private Staff staff;
    private Reviews reviews;
    
    @Before
    public void setUp() {
        reviews = new Reviews();
        staff = new Staff("admin", "admin@example.com", reviews);
        
        // Add some test reviews to work with
        reviews.addReview(new Review("reviewer1", "Good question", "q123"));
        reviews.addReview(new Review("reviewer1", "Interesting", "q456"));
        reviews.addReview(new Review("reviewer2", "Needs improvement", "q123", 3));
        reviews.addReview(new Review("reviewer3", "Very thorough", "q789", 5));
    }
    
    @Test
    public void testStaffCreation() {
        assertNotNull("Staff ID should not be null", staff.getId());
        assertEquals("admin", staff.getUsername());
        assertEquals("admin@example.com", staff.getEmail());
        assertNotNull("Creation date should not be null", staff.getCreatedAt());
    }
    
    @Test
    public void testGetAllReviewers() {
        List<String> reviewers = staff.getAllReviewers();
        assertNotNull("Reviewers list should not be null", reviewers);
        assertEquals("Should have 3 reviewers", 3, reviewers.size());
        assertTrue("Should contain reviewer1", reviewers.contains("reviewer1"));
        assertTrue("Should contain reviewer2", reviewers.contains("reviewer2"));
        assertTrue("Should contain reviewer3", reviewers.contains("reviewer3"));
    }
    
    @Test
    public void testDeleteReviewer() {
        // Test deleting an existing reviewer
        boolean deleted = staff.deleteReviewer("reviewer1");
        assertTrue("Should return true when deleting existing reviewer", deleted);
        
        // Verify reviewer1's reviews were removed
        List<Review> remainingReviews = reviews.getReviews();
        assertEquals("Should have 2 reviews left", 2, remainingReviews.size());
        for (Review review : remainingReviews) {
            assertNotEquals("Should not contain reviewer1's reviews", "reviewer1", review.getReviewer());
        }
        
        // Test deleting a non-existent reviewer
        deleted = staff.deleteReviewer("nonexistent");
        assertFalse("Should return false when deleting non-existent reviewer", deleted);
    }
    
    @Test
    public void testSystemSettings() {
        // Test getting default settings
        Map<String, Object> settings = staff.getAllSystemSettings();
        assertNotNull("Settings should not be null", settings);
        assertEquals("Default review weightage should be 1.0", 1.0, settings.get("reviewWeightage"));
        assertEquals("Default trusted reviewer weightage should be 1.5", 1.5, settings.get("trustedReviewerWeightage"));
        assertFalse("Default review visibility before approval should be false", 
                    (Boolean) settings.get("reviewVisibilityBeforeApproval"));
        assertEquals("Default minimum reviews for trusted status should be 10", 
                    10, settings.get("minReviewsForTrustedStatus"));
        
        // Test updating a setting
        staff.updateSystemSetting("reviewWeightage", 2.0);
        assertEquals("Updated review weightage should be 2.0", 
                    2.0, staff.getSystemSetting("reviewWeightage"));
        
        // Test updating review visibility
        staff.updateSystemSetting("reviewVisibilityBeforeApproval", true);
        assertTrue("Updated review visibility should be true", 
                  (Boolean) staff.getSystemSetting("reviewVisibilityBeforeApproval"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSettingValue() {
        staff.updateSystemSetting("reviewWeightage", -1.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSettingType() {
        staff.updateSystemSetting("reviewVisibilityBeforeApproval", "true"); // String instead of Boolean
    }
    
    @Test
    public void testActivityLogs() {
        // Initial setup has already generated some logs
        List<Staff.ActivityLog> logs = staff.getActivityLogs();
        assertFalse("Should have some activity logs", logs.isEmpty());
        
        // Perform some actions to generate more logs
        staff.getAllReviewers();
        staff.updateSystemSetting("minReviewsForTrustedStatus", 5);
        staff.deleteReviewer("reviewer2");
        
        // Check logs again
        List<Staff.ActivityLog> updatedLogs = staff.getActivityLogs();
        assertTrue("Should have more logs now", updatedLogs.size() > logs.size());
        
        // Verify log content of last action
        Staff.ActivityLog lastLog = updatedLogs.get(updatedLogs.size() - 1);
        assertEquals("admin", lastLog.getStaffUsername());
        assertTrue("Last log should be about deleting reviewer2", 
                  lastLog.getAction().contains("reviewer2"));
    }
    
    @Test
    public void testActivityLogsByDateRange() {
        // Create a date range
        Date startDate = new Date(System.currentTimeMillis() - 1000); // 1 second ago
        
        // Perform some actions
        staff.getAllReviewers();
        staff.updateSystemSetting("minReviewsForTrustedStatus", 5);
        
        Date endDate = new Date(); // Now
        
        List<Staff.ActivityLog> rangedLogs = staff.getActivityLogsByDateRange(startDate, endDate);
        assertFalse("Should have logs within the date range", rangedLogs.isEmpty());
        
        // Test with a future date range (should be empty)
        Date futureStart = new Date(System.currentTimeMillis() + 1000000);
        Date futureEnd = new Date(System.currentTimeMillis() + 2000000);
        List<Staff.ActivityLog> futureLogs = staff.getActivityLogsByDateRange(futureStart, futureEnd);
        assertTrue("Should have no logs in future date range", futureLogs.isEmpty());
    }
    
    @Test
    public void testTrustedReviewers() {
        // Add some reviewers to trusted list
        staff.updateSystemSetting("minReviewsForTrustedStatus", 1); // Lower threshold for testing
        
        boolean promoted = staff.promoteTrustedReviewer("reviewer1");
        assertTrue("Should successfully promote reviewer1", promoted);
        
        List<String> trustedReviewers = staff.getTrustedReviewers();
        assertEquals("Should have 1 trusted reviewer", 1, trustedReviewers.size());
        assertTrue("Should contain reviewer1", trustedReviewers.contains("reviewer1"));
        
        // Try to promote again (should fail as already trusted)
        promoted = staff.promoteTrustedReviewer("reviewer1");
        assertFalse("Should fail when promoting already trusted reviewer", promoted);
        
        // Test demoting a trusted reviewer
        boolean demoted = staff.demoteTrustedReviewer("reviewer1");
        assertTrue("Should successfully demote reviewer1", demoted);
        
        trustedReviewers = staff.getTrustedReviewers();
        assertTrue("Should have no trusted reviewers after demotion", trustedReviewers.isEmpty());
        
        // Test demoting a non-trusted reviewer
        demoted = staff.demoteTrustedReviewer("reviewer2");
        assertFalse("Should fail when demoting non-trusted reviewer", demoted);
    }
    
    @Test
    public void testMinimumReviewsRequirement() {
        // Set minimum reviews to 3
        staff.updateSystemSetting("minReviewsForTrustedStatus", 3);
        
        // reviewer1 has 2 reviews, should not be promotable
        boolean promoted = staff.promoteTrustedReviewer("reviewer1");
        assertFalse("Should not promote reviewer1 with insufficient reviews", promoted);
        
        // Add more reviews to reach threshold
        reviews.addReview(new Review("reviewer1", "Another review", "q999", 4));
        
        // Now promotion should succeed
        promoted = staff.promoteTrustedReviewer("reviewer1");
        assertTrue("Should promote reviewer1 after meeting review threshold", promoted);
    }
    
    @Test
    public void testNonExistentReviewer() {
        boolean promoted = staff.promoteTrustedReviewer("nonexistent");
        assertFalse("Should not promote non-existent reviewer", promoted);
        
        boolean demoted = staff.demoteTrustedReviewer("nonexistent");
        assertFalse("Should not demote non-existent reviewer", demoted);
    }
    
    @Test
    public void testIsReviewerTrusted() {
        // Lower threshold for testing
        staff.updateSystemSetting("minReviewsForTrustedStatus", 1);
        
        // Initially no trusted reviewers
        assertFalse("reviewer1 should not be trusted initially", staff.isReviewerTrusted("reviewer1"));
        
        // Promote reviewer1
        staff.promoteTrustedReviewer("reviewer1");
        
        // Check trusted status
        assertTrue("reviewer1 should be trusted after promotion", staff.isReviewerTrusted("reviewer1"));
        assertFalse("reviewer2 should not be trusted", staff.isReviewerTrusted("reviewer2"));
    }
}