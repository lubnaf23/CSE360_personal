package HW2;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for testing the Questions and Answers functionalities for this discussion board.
 * This class contains automated tests for adding, updating, deleting, and searching for questions and answers.
 * 
 * <p>Each test ensures that the application's question-answer system works as expected.</p>
 * 
 * @author Lubna Firdaus
 * @version 1.0
 */

public class HW3tests {
    
    private Questions questionManager;
    private Answers answerManager;
    private String testQuestionId = "test-question-id";

    /**
     * Sets up the test environment before each test runs.
     * Initializes the question and answer managers.
     */

    @Before
    public void setUp() {
        questionManager = new Questions();
        answerManager = new Answers();
    }

    /**
     * Tests whether a question can be successfully added.
     * Verifies that the number of questions increases after adding a new question.
     */
    @Test
    public void testAddQuestion() {
        Question newQuestion = new Question("What is your name?", "explain oop", "TestUser");
        questionManager.addQuestion(newQuestion);
        assertEquals("question added", 1, questionManager.getAllQuestions().size());
    }

    /**
     * Tests whether an answer can be successfully added to a question.
     * Verifies that the number of answers for a specific question increases.
     */
    @Test
    public void testAddAnswer() {
        Answer newAnswer = new Answer(testQuestionId, "test answer", "TestUser");
        answerManager.addAnswer(newAnswer);
        assertEquals("answer list contains one answer", 1, answerManager.getAnswersForQuestion(testQuestionId).size());
    }

    /**
     * Tests the search functionality for questions.
     * Ensures that a search for "Java" returns at least one result.
     */
    @Test
    public void testSearchQuestion() {
        questionManager.addQuestion(new Question("Java Question", "What is Java?", "TestUser"));
        boolean found = questionManager.searchQuestions("java").size() > 0;
        assertTrue("search finds at least one matching question", found);
    }

    /**
     * Tests updating an answer.
     * Ensures that after updating an answer's text, the updated text is stored correctly.
     */
    @Test
    public void testUpdateAnswer() {
        Answer answer = new Answer(testQuestionId, "old answer", "TestUser");
        answerManager.addAnswer(answer);
        answer.setAnswerText("updated answer");
        assertEquals("Answer should be updated", "updated answer", answerManager.getAnswerById(answer.getId()).getAnswerText());
    }

    /**
     * Tests the deletion of a question.
     * Ensures that after deleting a question, the list of questions is empty.
     */
    @Test
    public void testDeleteQuestion() {
        Question question = new Question("deleting question?", "TestUser");
        questionManager.addQuestion(question);
        questionManager.deleteQuestion(question.getId());
        assertEquals("question is deleted", 0, questionManager.getAllQuestions().size());
    }



}
