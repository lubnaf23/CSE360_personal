
import java.util.*;

public class Database {
    private static Map<String, User> users = new HashMap<>();
    private static List<Question> questions = new ArrayList<>();

    // Add user
    public static synchronized void addUser(User user) {
        users.put(user.getUsername(), user);
        System.out.println("User added: " + user.getUsername()); // Optional logging
    }

    // Get a user by username
    public static User getUser(String username) {
        return users.get(username);
    }

    // Add a question
    public static void addQuestion(Question question) {
        questions.add(question);
        System.out.println("Question added: " + question.getContent()); // Optional logging
    }

    // Get all questions
    public static List<Question> getQuestions() {
        return questions;
    }

    // Return all users
    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // Remove a user
    public static synchronized void removeUser(User user) {
        users.remove(user.getUsername());
        System.out.println("User removed: " + user.getUsername()); // Optional logging
    }
}

