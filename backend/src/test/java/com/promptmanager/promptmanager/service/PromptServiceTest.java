import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PromptServiceTest {

    @Mock
    private PromptRepository promptRepository;
    
    @Mock
    private PromptAnalyzer promptAnalyzer;
    
    @InjectMocks
    private PromptService promptService;

    private Prompt prompt;

    @BeforeEach
    public void setUp() {
        prompt = new Prompt(); // Assume there is a no-arg constructor
        prompt.setId(1L);
        prompt.setModel("TestModel");
    }

    @Test
    public void testSave() {
        when(promptRepository.save(any(Prompt.class))).thenReturn(prompt);
        Prompt savedPrompt = promptService.save(prompt);
        verify(promptRepository, times(1)).save(prompt);
        assertEquals(prompt, savedPrompt);
    }

    @Test
    public void testGetAll() {
        when(promptRepository.findAll()).thenReturn(Arrays.asList(prompt));
        List<Prompt> allPrompts = promptService.getAll();
        verify(promptRepository, times(1)).findAll();
        assertEquals(1, allPrompts.size());
        assertEquals(prompt, allPrompts.get(0));
    }

    @Test
    public void testGetByModel() {
        when(promptRepository.findByModel("TestModel")).thenReturn(Arrays.asList(prompt));
        List<Prompt> promptsByModel = promptService.getByModel("TestModel");
        verify(promptRepository, times(1)).findByModel("TestModel");
        assertEquals(1, promptsByModel.size());
        assertEquals(prompt, promptsByModel.get(0));
    }

    @Test
    public void testDelete() {
        doNothing().when(promptRepository).deleteById(1L);
        promptService.delete(1L);
        verify(promptRepository, times(1)).deleteById(1L);
    }
}