package utilities;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import exceptions.MetricsException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe MetricsManager.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
class MetricsManagerTest {

    private static final String TEST_COUNTER_NAME = "test.counter";
    private static final String TEST_COUNTER_DESCRIPTION = "Test counter";
    private static final String TEST_TIMER_NAME = "test.timer";
    private static final String TEST_TIMER_DESCRIPTION = "Test timer";

    @Test
    void testCounterCreation() throws MetricsException {
        Counter counter = MetricsManager.counter(TEST_COUNTER_NAME, TEST_COUNTER_DESCRIPTION);
        assertNotNull(counter);
        assertEquals(TEST_COUNTER_NAME, counter.getId().getName());
        assertEquals(TEST_COUNTER_DESCRIPTION, counter.getId().getDescription());
    }

    @Test
    void testTimerCreation() throws MetricsException {
        Timer timer = MetricsManager.timer(TEST_TIMER_NAME, TEST_TIMER_DESCRIPTION);
        assertNotNull(timer);
        assertEquals(TEST_TIMER_NAME, timer.getId().getName());
        assertEquals(TEST_TIMER_DESCRIPTION, timer.getId().getDescription());
    }

    @Test
    void testMeasureExecutionTime() throws MetricsException {
        Timer timer = MetricsManager.timer(TEST_TIMER_NAME, TEST_TIMER_DESCRIPTION);
        String result = MetricsManager.measureExecutionTime(timer, () -> "test");
        assertEquals("test", result);
        assertTrue(timer.count() > 0);
    }

    @Test
    void testMeasureExecutionTimeWithException() throws MetricsException {
        Timer timer = MetricsManager.timer(TEST_TIMER_NAME, TEST_TIMER_DESCRIPTION);
        MetricsException exception = assertThrows(
            MetricsException.class,
            () -> MetricsManager.measureExecutionTime(timer, () -> {
                throw new RuntimeException("Test exception");
            })
        );
        assertTrue(exception.getMessage().contains("Erreur lors de la mesure du temps d'exécution"));
    }

    @Test
    void testRegistryAccess() {
        assertNotNull(MetricsManager.getRegistry());
    }
} 