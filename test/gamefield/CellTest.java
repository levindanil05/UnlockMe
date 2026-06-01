package gamefield;

import block.Block;
import block.Color;
import block.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private Cell cell;
    private Cell neighborNorth;
    private Cell neighborEast;

    @BeforeEach
    void setUp() {
        cell = new Cell(2, 3);
        neighborNorth = new Cell(1, 3);
        neighborEast = new Cell(2, 4);

        // Устанавливаем связи между ячейками
        cell.setNeighbor(Direction.NORTH, neighborNorth);
        cell.setNeighbor(Direction.EAST, neighborEast);
        neighborNorth.setNeighbor(Direction.SOUTH, cell);
        neighborEast.setNeighbor(Direction.WEST, cell);
    }

    @Test
    void testCellCreation() {
        assertEquals(2, cell.getRow());
        assertEquals(3, cell.getCol());
        assertTrue(cell.isEmpty());
    }

    @Test
    void testPutUnit() {
        Block block = new Block(Color.RED, Orientation.HORIZONTAL, 2);

        assertTrue(cell.putUnit(block));
        assertFalse(cell.isEmpty());
        assertEquals(1, cell.getUnits().size());
    }

    @Test
    void testPutUnitNull() {
        assertFalse(cell.putUnit(null));
        assertTrue(cell.isEmpty());
    }

    @Test
    void testPutUnitTwice() {
        Block block = new Block(Color.BLUE, Orientation.VERTICAL, 2);

        assertTrue(cell.putUnit(block));
        assertFalse(cell.putUnit(block)); // Второй раз должен вернуть false
        assertEquals(1, cell.getUnits().size());
    }

    @Test
    void testExtractUnit() {
        Block block = new Block(Color.GREEN, Orientation.HORIZONTAL, 2);
        cell.putUnit(block);

        assertTrue(cell.extractUnit(block));
        assertTrue(cell.isEmpty());
    }

    @Test
    void testExtractNonExistentUnit() {
        Block block = new Block(Color.YELLOW, Orientation.VERTICAL, 2);

        assertFalse(cell.extractUnit(block));
        assertTrue(cell.isEmpty());
    }

    @Test
    void testGetNeighbor() {
        assertEquals(neighborNorth, cell.getNeighbor(Direction.NORTH));
        assertEquals(neighborEast, cell.getNeighbor(Direction.EAST));
        assertNull(cell.getNeighbor(Direction.SOUTH));
        assertNull(cell.getNeighbor(Direction.WEST));
    }

    @Test
    void testIsNeighbor() {
        assertTrue(cell.isNeighbor(neighborNorth));
        assertTrue(cell.isNeighbor(neighborEast));
        assertFalse(cell.isNeighbor(new Cell(5, 5)));
    }

    @Test
    void testGetUnitsByClass() {
        Block block1 = new Block(Color.RED, Orientation.HORIZONTAL, 2);
        Block block2 = new Block(Color.BLUE, Orientation.VERTICAL, 2);

        cell.putUnit(block1);
        cell.putUnit(block2);

        assertEquals(2, cell.getUnits(Block.class).length);
    }

    @Test
    void testToString() {
        assertEquals("Cell[2,3]", cell.toString());
    }
}