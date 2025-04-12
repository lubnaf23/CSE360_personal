package HW3;

import java.util.HashSet;
import java.util.Set;

public class User {
    protected String username;
    protected String password;
    protected Set<String> roles;
    protected String activeRole;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
        this.activeRole = "";  // Initially no active role
    }

    public String getUsername() { return username; }
    public boolean checkPassword(String password) { return this.password.equals(password); }
    public Set<String> getRoles() { return roles; }
    public void addRole(String role) { roles.add(role); }
    public void setActiveRole(String role) { this.activeRole = role; }
    public String getActiveRole() { return activeRole; }

    // Set a new password
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
