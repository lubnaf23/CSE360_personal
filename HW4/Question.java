import java.util.ArrayList;
import java.util.List;

public class Question {
    private static int counter = 1;
    private int id;
    private String content;
    private List<String> answers;
    private boolean resolved;

    public Question(String content) {
        this.id = counter++;
        this.content = content;
        this.answers = new ArrayList<>();
        this.resolved = false;
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public List<String> getAnswers() { return answers; }
    public boolean isResolved() { return resolved; }

    public void addAnswer(String answer) { answers.add(answer); }
    public void markResolved() { this.resolved = true; }
}

