import java.util.Scanner;

public class QuestionService {
    private static Scanner scanner = new Scanner(System.in);

    public static void askQuestion(User user) {
        if (!user.getRoles().contains("student")) {
            System.out.println("Only students can ask questions.");
            return;
        }

        System.out.print("Enter your question: ");
        scanner.nextLine();
        String content = scanner.nextLine();
        Question question = new Question(content);
        Database.addQuestion(question);
        System.out.println("Question submitted successfully!");
    }

    public static void viewQuestions() {
        for (Question q : Database.getQuestions()) {
            System.out.println(q.getId() + ". " + q.getContent() + (q.isResolved() ? " [Resolved]" : ""));
        }
    }

    public static void answerQuestion() {
        System.out.print("Enter question ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        for (Question q : Database.getQuestions()) {
            if (q.getId() == id) {
                System.out.print("Enter your answer: ");
                String answer = scanner.nextLine();
                q.addAnswer(answer);
                System.out.println("Answer added!");
                return;
            }
        }
        System.out.println("Question not found.");
    }
}

