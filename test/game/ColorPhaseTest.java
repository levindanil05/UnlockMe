package test.game;

import game.ColorPhase;
import block.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса ColorPhase.
 * Проверяют корректность создания фаз и их поведения.
 */
public class ColorPhaseTest {

    @Test
    void testConstructor_WithColor_CreatesFixedPhase() {
        ColorPhase phase = new ColorPhase(3, Color.AQUA);

        assertEquals(3, phase.getDuration());
        assertEquals(Color.AQUA, phase.getAllowedColor());
        assertFalse(phase.isRandom());
    }

    @Test
    void testConstructor_WithoutColor_CreatesRandomPhase() {
        ColorPhase phase = new ColorPhase(2);

        assertEquals(2, phase.getDuration());
        assertNull(phase.getAllowedColor());
        assertTrue(phase.isRandom());
    }

    @Test
    void testConstructor_ZeroDuration_Allowed() {
        ColorPhase phase = new ColorPhase(0, Color.GRAY);

        assertEquals(0, phase.getDuration());
        assertEquals(Color.GRAY, phase.getAllowedColor());
        assertFalse(phase.isRandom());
    }

    @Test
    void testIsColorAllowed_FixedColor_MatchingColor_ReturnsTrue() {
        ColorPhase phase = new ColorPhase(3, Color.AQUA);

        assertTrue(phase.isColorAllowed(Color.AQUA, null));
    }

    @Test
    void testIsColorAllowed_FixedColor_DifferentColor_ReturnsFalse() {
        ColorPhase phase = new ColorPhase(3, Color.AQUA);

        assertFalse(phase.isColorAllowed(Color.GRAY, null));
        assertFalse(phase.isColorAllowed(Color.RED, null));
        assertFalse(phase.isColorAllowed(Color.BLUE, null));
    }

    @Test
    void testIsColorAllowed_FixedColor_NullInput_ReturnsFalse() {
        ColorPhase phase = new ColorPhase(3, Color.AQUA);

        assertFalse(phase.isColorAllowed(null, null));
    }

    @Test
    void testIsColorAllowed_RandomPhase_MatchingRandomColor_ReturnsTrue() {
        ColorPhase phase = new ColorPhase(1); // Случайный цвет
        Color randomColor = Color.GRAY;

        assertTrue(phase.isColorAllowed(Color.GRAY, randomColor));
    }

    @Test
    void testIsColorAllowed_RandomPhase_DifferentFromRandomColor_ReturnsFalse() {
        ColorPhase phase = new ColorPhase(1); // Случайный цвет
        Color randomColor = Color.GRAY;

        assertFalse(phase.isColorAllowed(Color.AQUA, randomColor));
        assertFalse(phase.isColorAllowed(Color.RED, randomColor));
    }

    @Test
    void testIsColorAllowed_RandomPhase_NullRandomColor_ReturnsFalse() {
        ColorPhase phase = new ColorPhase(1); // Случайный цвет

        assertFalse(phase.isColorAllowed(Color.GRAY, null));
    }

    @Test
    void testAllColors_AreHandledCorrectly() {
        Color[] colors = Color.values();

        for (Color color : colors) {
            ColorPhase phase = new ColorPhase(1, color);
            assertFalse(phase.isRandom());
            assertEquals(color, phase.getAllowedColor());
            assertTrue(phase.isColorAllowed(color, null));
        }
    }

    @Test
    void testRedColor_SpecialCase() {
        ColorPhase phase = new ColorPhase(1, Color.RED);

        assertEquals(Color.RED, phase.getAllowedColor());
        assertTrue(phase.isColorAllowed(Color.RED, null));
        assertFalse(phase.isColorAllowed(Color.GRAY, null));
    }
}