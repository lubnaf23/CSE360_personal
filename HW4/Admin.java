import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
        this.roles.add("admin");
    }

    // Invite a user if it exists (lol)
    public void inviteUser() {
    	Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username of the user to invite: ");
        String username = scanner.nextLine();

        System.out.print("Enter role for the user (e.g., student, instructor, reviewer): ");
        String role = scanner.nextLine();

        //user existence
        User existingUser = Database.getUser(username);
        
        //use ACTIVATE ROLE
        if (existingUser == null) {
            System.out.println("User not found, creating new user.");
            User newUser = new User(username, "defaultpassword"); //HAHA
            newUser.addRole(role);
            Database.addUser(newUser);
            System.out.println("User " + username + " created with role: " + role);
        } else {
            existingUser.addRole(role);
            System.out.println("User " + username + " invited with role: " + role);
        }
    }

    //view all users
    public void viewAllUsers() {
        System.out.println("Users in the system:");
        for (User user : Database.getAllUsers()) {
            System.out.println(user.getUsername() + " - Roles: " + user.getRoles());
        }
    }

    //got rid of this prompt too much logic
    public void resetUserPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        System.out.println("Password for " + user.getUsername() + " has been reset.");
    }

    //this too could be implemented
    public void deleteUser(User user) {
        Database.removeUser(user);
        System.out.println("User " + user.getUsername() + " has been deleted.");
    }
}
