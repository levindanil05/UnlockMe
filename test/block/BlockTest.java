package block;

import gamefield.Cell;
import gamefield.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {
    private Block horizontalBlock;
    private Block verticalBlock;
    private Cell[] cells;

    @BeforeEach
    void setUp() {
        horizontalBlock = new Block(Color.RED, Orientation.HORIZONTAL, 2);

        verticalBlock = new Block(Color.BLUE, Orientation.VERTICAL, 3);

        cells = new Cell[3];
        for (int i = 0; i < 3; i++) {
            cells[i] = new Cell(2, i);
        }

        for (int i = 0; i < 2; i++) {
            cells[i].setNeighbor(Direction.EAST, cells[i + 1]);
            cells[i + 1].setNeighbor(Direction.WEST, cells[i]);
        }

        cells[0].putUnit(horizontalBlock);
        cells[1].putUnit(horizontalBlock);
        horizontalBlock.setOwnerCell(0, cells[0]);
        horizontalBlock.setOwnerCell(1, cells[1]);
    }

    @Test
    void testBlockCreation() {
        assertEquals(Color.RED, horizontalBlock.getColor());
        assertEquals(Orientation.HORIZONTAL, horizontalBlock.getOrientation());
        assertEquals(2, horizontalBlock.getLength());
        assertTrue(horizontalBlock.isRedBlock());

        assertFalse(verticalBlock.isRedBlock());
    }

    @Test
    void testGetOwnerCells() {
        assertEquals(2, horizontalBlock.getOwnerCells().length);
        assertEquals(cells[0], horizontalBlock.getOwnerCells()[0]);
        assertEquals(cells[1], horizontalBlock.getOwnerCells()[1]);
    }

    @Test
    void testMoveHorizontalBlockEast() {
        Cell targetCell = new Cell(2, 2);
        cells[1].setNeighbor(Direction.EAST, targetCell);
        targetCell.setNeighbor(Direction.WEST, cells[1]);

        assertTrue(horizontalBlock.move(Direction.EAST, 1));

        assertEquals(cells[1], horizontalBlock.getOwnerCells()[0]);
        assertEquals(targetCell, horizontalBlock.getOwnerCells()[1]);
        assertFalse(cells[0].getUnits().contains(horizontalBlock));
    }

    @Test
    void testMoveHorizontalBlockWest() {
        Cell leftCell = new Cell(2, -1);
        cells[0].setNeighbor(Direction.WEST, leftCell);
        leftCell.setNeighbor(Direction.EAST, cells[0]);

        assertTrue(horizontalBlock.move(Direction.WEST, 1));

        assertEquals(leftCell, horizontalBlock.getOwnerCells()[0]);
        assertEquals(cells[0], horizontalBlock.getOwnerCells()[1]);
    }

    @Test
    void testMoveBlockedByAnotherBlock() {
        Block blockingBlock = new Block(Color.GREEN, Orientation.VERTICAL, 2);
        cells[2].putUnit(blockingBlock);

        assertFalse(horizontalBlock.move(Direction.EAST, 1));

        assertEquals(cells[0], horizontalBlock.getOwnerCells()[0]);
        assertEquals(cells[1], horizontalBlock.getOwnerCells()[1]);
    }

    @Test
    void testMoveInInvalidDirection() {
        assertFalse(horizontalBlock.move(Direction.NORTH, 1));
        assertFalse(horizontalBlock.move(Direction.SOUTH, 1));
    }

    @Test
    void testMoveVerticalBlock() {
        Cell[] verticalCells = new Cell[4];
        for (int i = 0; i < 4; i++) {
            verticalCells[i] = new Cell(i, 0);
        }

        for (int i = 0; i < 3; i++) {
            verticalCells[i].setNeighbor(Direction.SOUTH, verticalCells[i + 1]);
            verticalCells[i + 1].setNeighbor(Direction.NORTH, verticalCells[i]);
            verticalCells[i].putUnit(verticalBlock);
        }

        verticalBlock.setOwnerCell(0, verticalCells[0]);
        verticalBlock.setOwnerCell(1, verticalCells[1]);
        verticalBlock.setOwnerCell(2, verticalCells[2]);

        assertTrue(verticalBlock.move(Direction.SOUTH, 1));

        assertEquals(verticalCells[1], verticalBlock.getOwnerCells()[0]);
        assertEquals(verticalCells[2], verticalBlock.getOwnerCells()[1]);
        assertEquals(verticalCells[3], verticalBlock.getOwnerCells()[2]);
    }

}