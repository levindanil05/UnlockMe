package gamefield;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {
    private GameField gameField;

    @BeforeEach
    void setUp() {
        gameField = new GameField(6, 6);
    }

    @Test
    void testGameFieldCreation() {
        assertEquals(6, gameField.getWidth());
        assertEquals(6, gameField.getHeight());
    }

    @Test
    void testGetCell() {
        Cell cell = gameField.getCell(2, 3);
        assertNotNull(cell);
        assertEquals(2, cell.getRow());
        assertEquals(3, cell.getCol());
    }

    @Test
    void testGetInvalidCell() {
        assertNull(gameField.getCell(-1, 0));
        assertNull(gameField.getCell(0, -1));
        assertNull(gameField.getCell(6, 0));
        assertNull(gameField.getCell(0, 6));
    }

    @Test
    void testSetExitEast() {
        gameField.setExit(Direction.EAST, 2);

        assertEquals(Direction.EAST, gameField.getExitSide());
        assertEquals(2, gameField.getExitPosition());
    }

    @Test
    void testSetExitNorth() {
        gameField.setExit(Direction.NORTH, 4);

        assertEquals(Direction.NORTH, gameField.getExitSide());
        assertEquals(4, gameField.getExitPosition());
    }

    @Test
    void testSetExitInvalidPosition() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameField.setExit(Direction.EAST, 10);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gameField.setExit(Direction.NORTH, -1);
        });
    }

    @Test
    void testCellNeighbors() {
        Cell centerCell = gameField.getCell(3, 3);

        assertNotNull(centerCell.getNeighbor(Direction.NORTH));
        assertNotNull(centerCell.getNeighbor(Direction.SOUTH));
        assertNotNull(centerCell.getNeighbor(Direction.EAST));
        assertNotNull(centerCell.getNeighbor(Direction.WEST));

        assertEquals(2, centerCell.getNeighbor(Direction.NORTH).getRow());
        assertEquals(4, centerCell.getNeighbor(Direction.SOUTH).getRow());
        assertEquals(4, centerCell.getNeighbor(Direction.EAST).getCol());
        assertEquals(2, centerCell.getNeighbor(Direction.WEST).getCol());
    }

    @Test
    void testEdgeCells() {
        Cell topLeftCell = gameField.getCell(0, 0);

        assertNull(topLeftCell.getNeighbor(Direction.NORTH));
        assertNull(topLeftCell.getNeighbor(Direction.WEST));
        assertNotNull(topLeftCell.getNeighbor(Direction.SOUTH));
        assertNotNull(topLeftCell.getNeighbor(Direction.EAST));
    }

    @Test
    void testDeactivate() {
        assertDoesNotThrow(() -> {
            gameField.deactivate();
        });
    }
}