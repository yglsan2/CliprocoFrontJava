package utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe CacheManager.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
class CacheManagerTest {

    private static final String TEST_KEY = "testKey";
    private static final String TEST_VALUE = "testValue";

    @BeforeEach
    void setUp() {
        CacheManager.clear();
    }

    @Test
    void testPutAndGet() {
        CacheManager.put(TEST_KEY, TEST_VALUE);
        Object retrieved = CacheManager.get(TEST_KEY);
        assertEquals(TEST_VALUE, retrieved);
    }

    @Test
    void testRemove() {
        CacheManager.put(TEST_KEY, TEST_VALUE);
        CacheManager.remove(TEST_KEY);
        assertNull(CacheManager.get(TEST_KEY));
    }

    @Test
    void testClear() {
        CacheManager.put(TEST_KEY, TEST_VALUE);
        CacheManager.put("key2", "value2");
        CacheManager.clear();
        assertNull(CacheManager.get(TEST_KEY));
        assertNull(CacheManager.get("key2"));
    }

    @Test
    void testContainsKey() {
        assertFalse(CacheManager.containsKey(TEST_KEY));
        CacheManager.put(TEST_KEY, TEST_VALUE);
        assertTrue(CacheManager.containsKey(TEST_KEY));
    }

    @Test
    void testSize() {
        assertEquals(0, CacheManager.size());
        CacheManager.put(TEST_KEY, TEST_VALUE);
        assertEquals(1, CacheManager.size());
        CacheManager.put("key2", "value2");
        assertEquals(2, CacheManager.size());
    }
} 