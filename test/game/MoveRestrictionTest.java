package test.game;

import game.AnyColorRestriction;
import game.MoveRestriction;
import game.SequenceColorRestriction;
import block.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для системы ограничений на перемещение блоков.
 */
public class MoveRestrictionTest {

    @Test
    public void testAnyColorRestriction_AllowsAllColors() {
        MoveRestriction restriction = new AnyColorRestriction();

        assertTrue(restriction.canMove(Color.RED, 1));
        assertTrue(restriction.canMove(Color.BLUE, 1));
        assertTrue(restriction.canMove(Color.GRAY, 5));
        assertTrue(restriction.canMove(Color.GREEN, 10));
    }

    @Test
    public void testAnyColorRestriction_Description() {
        MoveRestriction restriction = new AnyColorRestriction();

        String description = restriction.getDescription(1);

        assertTrue(description.contains("ЛЮБЫЕ"));
    }


    @Test
    public void testSequenceColorRestriction_FourthMove_RandomColor() {
        MoveRestriction restriction = new SequenceColorRestriction();

        boolean redAllowed = restriction.canMove(Color.RED, 4);
        boolean blueAllowed = restriction.canMove(Color.BLUE, 4);
        boolean grayAllowed = restriction.canMove(Color.GRAY, 4);
        boolean greenAllowed = restriction.canMove(Color.GREEN, 4);
        boolean yellowAllowed = restriction.canMove(Color.YELLOW, 4);
        boolean orangeAllowed = restriction.canMove(Color.ORANGE, 4);
        boolean purpleAllowed = restriction.canMove(Color.PURPLE, 4);

        assertTrue(redAllowed || blueAllowed || grayAllowed ||
                greenAllowed || yellowAllowed || orangeAllowed || purpleAllowed);
    }

    @Test
    public void testSequenceColorRestriction_FifthMoveAndLater_AllColorsAllowed() {
        MoveRestriction restriction = new SequenceColorRestriction();

        assertTrue(restriction.canMove(Color.RED, 5));
        assertTrue(restriction.canMove(Color.BLUE, 5));
        assertTrue(restriction.canMove(Color.GRAY, 5));
        assertTrue(restriction.canMove(Color.GREEN, 6));
        assertTrue(restriction.canMove(Color.YELLOW, 10));
    }


    @Test
    public void testSequenceColorRestriction_Description_LaterMoves() {
        MoveRestriction restriction = new SequenceColorRestriction();

        String desc5 = restriction.getDescription(5);
        String desc10 = restriction.getDescription(10);

        assertTrue(desc5.contains("ЛЮБЫЕ"));
        assertTrue(desc10.contains("ЛЮБЫЕ"));
    }
}