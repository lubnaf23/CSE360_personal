import java.util.Scanner;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Q/A System");

        // Creating the first admin user
        if (Database.getUser("admin") == null) {
            System.out.println("First-time setup: Creating admin user.");
            Database.addUser(new Admin("admin", "admin123"));
        }
        
        //student just to initialize
        if (Database.getUser("student") == null) {
            System.out.println("First-time setup: Creating student user.");
            //Database.addUser(new User("student", "student123"));
            //user.addRole("student");  // Add the role to the student
            //Database.addUser("student");
            Student studentUser = new Student("student", "student123");  // Create a student, not just a generic user
            Database.addUser(studentUser);
        }

        if (Database.getUser("reviewer") == null) {
            System.out.println("First-time setup: Creating reviewer user.");
            Database.addUser(new Admin("reviewer", "reviewer123"));
        }

        // Main Loop for Login/Exit
        while (true) {
            System.out.println("\n1. Log in\n2. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                User user = AuthService.login();
                if (user != null) {
                    if (user.getRoles().size() > 1) {
                        // single/multiple
                        selectRole(user);
                    } else {
                        // single
                        user.setActiveRole(user.getRoles().iterator().next());
                        userMenu(user);
                        
                    }
                }
            } else if (choice == 2) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

    private static void selectRole(User user) {
        System.out.println("\nYou have multiple roles:");
        int i = 1;
        for (String role : user.getRoles()) {
            System.out.println(i + ". " + role);
            i++;
        }
        System.out.print("Select a role: ");
        int roleChoice = scanner.nextInt();
        if (roleChoice > 0 && roleChoice <= user.getRoles().size()) {
            String selectedRole = (String) user.getRoles().toArray()[roleChoice - 1]; // Using toArray() for index-based selection
            user.setActiveRole(selectedRole);
            userMenu(user);
        } else {
            System.out.println("Invalid role selection.");
        }
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\nLogged in as: " + user.getActiveRole());
            switch (user.getActiveRole().toLowerCase()) {
                case "admin":
                    adminMenu((Admin) user);
                    break;
                case "student":
                    studentMenu((Student) user);
                    break;
                case "reviewer":
                    reviewerMenu(user);
                    break;
                default:
                    System.out.println("No menu available for this role.");
            }
            break; // Exit after role menu
        }
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Invite User");
            System.out.println("2. View All Users");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    admin.inviteUser();  //add user
                    break;
                case 2:
                    admin.viewAllUsers();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // private static void studentMenu(Student student) {
    //     while (true) {
    //         System.out.println("\nStudent Menu:");
    //         System.out.println("1. Ask Question");
    //         System.out.println("2. View All Questions");
    //         System.out.println("3. Logout");
    //         System.out.print("Choose an option: ");
    //         int choice = scanner.nextInt();

    //         switch (choice) {
    //             case 1:
    //                 QuestionService.askQuestion(student);
    //                 break;
    //             case 2:
    //                 QuestionService.viewQuestions();
    //                 break;
    //             case 3:
    //                 return;
    //             default:
    //                 System.out.println("Invalid option.");
    //         }
    //     }
    // }

    private static void reviewerMenu(User reviewer) { //no reviwer yet
        System.out.println("\nReviewer Menu:");
        System.out.println("1. Answer a Question");
        System.out.println("2. View Answers");
        System.out.println("3. Logout");
        System.out.print("Choose an option: ");
    }
}
