package game;

import gamefield.*;
import block.Color;
import block.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {
    private GameField gameField;
    private Level level;

    @BeforeEach
    void setUp() {
        gameField = new GameField(6, 6);
        level = new Level(gameField, 6, 6);
    }

    @Test
    void testCreateField() {
        level.createField();

        assertEquals(6, gameField.getWidth());
        assertEquals(6, gameField.getHeight());
    }

    @Test
    void testCreateBlocksWithValidConfiguration() {
        level.setExit(Direction.EAST, 2);

        Level.BlockConfiguration[] configs = {
                new Level.BlockConfiguration(Color.RED, Orientation.HORIZONTAL, 2, 2, 0),
                new Level.BlockConfiguration(Color.BLUE, Orientation.HORIZONTAL, 2, 0, 1),
                new Level.BlockConfiguration(Color.GREEN, Orientation.VERTICAL, 2, 1, 3)
        };

        level.setBlockConfigurations(configs);

        assertDoesNotThrow(() -> {
            level.createBlocks();
        });
    }

    @Test
    void testCreateBlocksWithInvalidRedBlockPosition() {
        level.setExit(Direction.EAST, 2);

        Level.BlockConfiguration[] configs = {
                new Level.BlockConfiguration(Color.RED, Orientation.HORIZONTAL, 2, 0, 0), // Позиция 0, а не 2
                new Level.BlockConfiguration(Color.BLUE, Orientation.HORIZONTAL, 2, 1, 1)
        };

        level.setBlockConfigurations(configs);

        assertThrows(IllegalStateException.class, () -> {
            level.createBlocks();
        });
    }

    @Test
    void testCreateBlocksWithInvalidOrientation() {
        level.setExit(Direction.EAST, 2);

        Level.BlockConfiguration[] configs = {
                new Level.BlockConfiguration(Color.RED, Orientation.VERTICAL, 2, 2, 0),
                new Level.BlockConfiguration(Color.BLUE, Orientation.HORIZONTAL, 2, 0, 1)
        };

        level.setBlockConfigurations(configs);

        assertThrows(IllegalStateException.class, () -> {
            level.createBlocks();
        });
    }

    @Test
    void testCreateBlocksWithNorthExit() {
        level.setExit(Direction.NORTH, 3);

        Level.BlockConfiguration[] configs = {
                new Level.BlockConfiguration(Color.RED, Orientation.VERTICAL, 2, 0, 3),
                new Level.BlockConfiguration(Color.BLUE, Orientation.HORIZONTAL, 2, 2, 1)
        };

        level.setBlockConfigurations(configs);

        assertDoesNotThrow(() -> {
            level.createBlocks();
        });
    }

    @Test
    void testBlockPlacement() {
        level.setExit(Direction.EAST, 1);

        Level.BlockConfiguration[] configs = {
                new Level.BlockConfiguration(Color.RED, Orientation.HORIZONTAL, 2, 1, 0),
                new Level.BlockConfiguration(Color.YELLOW, Orientation.VERTICAL, 3, 0, 4)
        };

        level.setBlockConfigurations(configs);
        level.createBlocks();

        Cell cell1_0 = gameField.getCell(1, 0);
        Cell cell1_1 = gameField.getCell(1, 1);

        assertFalse(cell1_0.isEmpty());
        assertFalse(cell1_1.isEmpty());
    }

    @Test
    void testGetGameField() {
        assertNotNull(level.getGameField());
        assertEquals(gameField, level.getGameField());
    }

    @Test
    void testBlockConfigurationCreation() {
        Level.BlockConfiguration config = new Level.BlockConfiguration(
                Color.ORANGE,
                Orientation.HORIZONTAL,
                3,
                2,
                1
        );

        assertEquals(Color.ORANGE, config.color);
        assertEquals(Orientation.HORIZONTAL, config.orientation);
        assertEquals(3, config.length);
        assertEquals(2, config.startRow);
        assertEquals(1, config.startCol);
    }

    /**
     * Тест установки и получения ограничения.
     */
    @Test
    public void testSetMoveRestriction() {
        MoveRestriction restriction = new AnyColorRestriction();

        level.setMoveRestriction(restriction);
        MoveRestriction retrieved = level.getMoveRestriction();

        assertNotNull(retrieved);
        assertEquals(restriction, retrieved);
    }

    /**
     * Тест: по умолчанию ограничение отсутствует (можно двигать все).
     */
    @Test
    public void testCanMoveBlock_Default_AllColorsAllowed() {
        boolean canMoveRed = level.canMoveBlock(Color.RED, 1);
        boolean canMoveBlue = level.canMoveBlock(Color.BLUE, 1);
        boolean canMoveGray = level.canMoveBlock(Color.GRAY, 1);

        assertTrue(canMoveRed);
        assertTrue(canMoveBlue);
        assertTrue(canMoveGray);
    }

    /**
     * Тест: с ограничением на голубые блоки.
     */
    @Test
    public void testCanMoveBlock_WithBlueRestriction() {
        level.setMoveRestriction(new SequenceColorRestriction());

        assertTrue(level.canMoveBlock(Color.AQUA, 1));
        assertTrue(level.canMoveBlock(Color.GRAY, 1));
        assertTrue(level.canMoveBlock(Color.RED, 2));
        assertTrue(level.canMoveBlock(Color.AQUA, 3));
    }

    /**
     * Тест получения описания ограничения.
     */
    @Test
    public void testGetRestrictionDescription() {
        level.setMoveRestriction(new SequenceColorRestriction());

        String desc1 = level.getRestrictionDescription(1);
        String desc5 = level.getRestrictionDescription(5);

        assertNotNull(desc1);
        assertFalse(desc1.contains("ГОЛУБЫЕ") || desc1.contains("Ход"));
        assertTrue(desc5.contains("ЛЮБЫЕ"));
    }

    /**
     * Тест: описание без ограничения.
     */
    @Test
    public void testGetRestrictionDescription_NoRestriction() {
        String desc = level.getRestrictionDescription(1);

        assertNotNull(desc);
    }
}