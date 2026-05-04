import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PromptTest {
    private Prompt prompt;

    @BeforeEach
    public void setUp() {
        prompt = new Prompt();
    }

    @Test
    public void testIdGetterAndSetter() {
        prompt.setId(1L);
        Assertions.assertEquals(1L, prompt.getId());
    }

    @Test
    public void testAiModelGetterAndSetter() {
        prompt.setAiModel("model1");
        Assertions.assertEquals("model1", prompt.getAiModel());
    }

    @Test
    public void testTitleGetterAndSetter() {
        prompt.setTitle("A Valid Title");
        Assertions.assertEquals("A Valid Title", prompt.getTitle());
    }

    @Test
    public void testCategoryGetterAndSetter() {
        prompt.setCategory("Category1");
        Assertions.assertEquals("Category1", prompt.getCategory());
    }

    @Test
    public void testTextGetterAndSetter() {
        prompt.setText("This is a sample prompt text.");
        Assertions.assertEquals("This is a sample prompt text.", prompt.getText());
    }

    @Test
    public void testScoreGetterAndSetter() {
        prompt.setScore(10);
        Assertions.assertEquals(10, prompt.getScore());
    }

    @Test
    public void testIdSetterWithNull() {
        prompt.setId(null);
        Assertions.assertNull(prompt.getId());
    }

    @Test
    public void testAiModelSetterWithNull() {
        prompt.setAiModel(null);
        Assertions.assertNull(prompt.getAiModel());
    }

    @Test
    public void testTitleSetterWithEmptyString() {
        prompt.setTitle("");
        Assertions.assertEquals("", prompt.getTitle());
    }

    @Test
    public void testCategorySetterWithEmptyString() {
        prompt.setCategory("");
        Assertions.assertEquals("", prompt.getCategory());
    }

    @Test
    public void testTextSetterWithSpecialCharacters() {
        prompt.setText("!@#$%^&*()");
        Assertions.assertEquals("!@#$%^&*()", prompt.getText());
    }

    @Test
    public void testScoreSetterWithNegativeValue() {
        prompt.setScore(-1);
        Assertions.assertEquals(-1, prompt.getScore());
    }

    @Test
    public void testTitleWithBoundaryValue() {
        prompt.setTitle("A really really long title that exceeds the usual length limit expected for a title in the Prompt entity");
        Assertions.assertEquals("A really really long title that exceeds the usual length limit expected for a title in the Prompt entity", prompt.getTitle());
    }

    @Test
    public void testAiModelWithBoundaryValue() {
        prompt.setAiModel("A really really long ai model name that might exceed typical limits");
        Assertions.assertEquals("A really really long ai model name that might exceed typical limits", prompt.getAiModel());
    }

    @Test
    public void testTextWithBoundaryValue() {
        prompt.setText("A long text input that serves the purpose of boundary testing for the text field in a Prompt entity");
        Assertions.assertEquals("A long text input that serves the purpose of boundary testing for the text field in a Prompt entity", prompt.getText());
    }

    @Test
    public void testCategorySetterWithNull() {
        prompt.setCategory(null);
        Assertions.assertNull(prompt.getCategory());
    }

    @Test
    public void testScoreSetterWithZero() {
        prompt.setScore(0);
        Assertions.assertEquals(0, prompt.getScore());
    }

    @Test
    public void testAiModelSetterWithSpecialCharacters() {
        prompt.setAiModel("model@123!");
        Assertions.assertEquals("model@123!", prompt.getAiModel());
    }

    @Test
    public void testEmptyPrompt() {
        Assertions.assertNotNull(new Prompt());
    }

    @Test
    public void testPromptInitialization() {
        Prompt newPrompt = new Prompt(1L, "model1", "Sample Title", "Category1", "Prompt text", 5);
        Assertions.assertEquals(1L, newPrompt.getId());
        Assertions.assertEquals("model1", newPrompt.getAiModel());
        Assertions.assertEquals("Sample Title", newPrompt.getTitle());
        Assertions.assertEquals("Category1", newPrompt.getCategory());
        Assertions.assertEquals("Prompt text", newPrompt.getText());
        Assertions.assertEquals(5, newPrompt.getScore());
    }
}