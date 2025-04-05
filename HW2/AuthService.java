// import java.util.Scanner;

// public class AuthService {
//     private static Scanner scanner = new Scanner(System.in);

//     public static User login() {
//         System.out.print("Enter username: ");
//         String username = scanner.next();
//         System.out.print("Enter password: ");
//         String password = scanner.next();

//         User user = Database.getUser(username);
//         if (user != null && user.checkPassword(password)) {
//             System.out.println("Login successful!");
//             return user;
//         } else {
//             System.out.println("Invalid credentials.");
//             return null;
//         }
//     }
// }
import java.util.Scanner;

public class AuthService {
    private static Scanner scanner = new Scanner(System.in);

    public static User login() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        User user = Database.getUser(username);
        if (user != null && user.checkPassword(password)) {
            System.out.println("Login successful!");
            return user;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }
}
