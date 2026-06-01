package block;

import gamefield.Cell;
import gamefield.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractBlockTest {
    private TestBlock testBlock;
    private Cell[] cells;

    @BeforeEach
    void setUp() {
        testBlock = new TestBlock(Color.PURPLE, Orientation.HORIZONTAL, 2);

        cells = new Cell[2];
        for (int i = 0; i < 2; i++) {
            cells[i] = new Cell(3, i);
        }

        cells[0].setNeighbor(Direction.EAST, cells[1]);
        cells[1].setNeighbor(Direction.WEST, cells[0]);

        testBlock.setOwnerCell(0, cells[0]);
        testBlock.setOwnerCell(1, cells[1]);
    }

    @Test
    void testAbstractBlockProperties() {
        assertEquals(Color.PURPLE, testBlock.getColor());
        assertEquals(Orientation.HORIZONTAL, testBlock.getOrientation());
        assertEquals(2, testBlock.getLength());
        assertFalse(testBlock.isRedBlock());
    }

    @Test
    void testGetOwnerCells() {
        Cell[] ownerCells = testBlock.getOwnerCells();

        assertEquals(2, ownerCells.length);
        assertEquals(cells[0], ownerCells[0]);
        assertEquals(cells[1], ownerCells[1]);
    }

    @Test
    void testSetOwnerCell() {
        Cell newCell = new Cell(3, 2);
        testBlock.setOwnerCell(1, newCell);

        assertEquals(newCell, testBlock.getOwnerCells()[1]);
    }

    @Test
    void testSetOwnerCellInvalidIndex() {
        Cell newCell = new Cell(3, 2);

        assertDoesNotThrow(() -> {
            testBlock.setOwnerCell(5, newCell);
        });

        assertEquals(cells[1], testBlock.getOwnerCells()[1]);
    }

    @Test
    void testCanBelongTo() {
        Cell cell = new Cell(0, 0);

        assertTrue(testBlock.canBelongToProtected(cell, 0));
        assertTrue(testBlock.canBelongToProtected(cell, 1));
        assertFalse(testBlock.canBelongToProtected(null, 0));
        assertFalse(testBlock.canBelongToProtected(cell, 5)); // Индекс за пределами
    }

    @Test
    void testIsRedBlock() {
        TestBlock redBlock = new TestBlock(Color.RED, Orientation.VERTICAL, 2);
        TestBlock blueBlock = new TestBlock(Color.BLUE, Orientation.HORIZONTAL, 2);

        assertTrue(redBlock.isRedBlock());
        assertFalse(blueBlock.isRedBlock());
        assertFalse(testBlock.isRedBlock());
    }

    @Test
    void testMoveDelegation() {
        Cell[] extendedCells = new Cell[4];
        for (int i = 0; i < 4; i++) {
            extendedCells[i] = new Cell(3, i);
        }

        for (int i = 0; i < 3; i++) {
            extendedCells[i].setNeighbor(Direction.EAST, extendedCells[i + 1]);
            extendedCells[i + 1].setNeighbor(Direction.WEST, extendedCells[i]);
        }

        TestBlock movableBlock = new TestBlock(Color.GREEN, Orientation.HORIZONTAL, 2);
        extendedCells[0].putUnit(movableBlock);
        extendedCells[1].putUnit(movableBlock);
        movableBlock.setOwnerCell(0, extendedCells[0]);
        movableBlock.setOwnerCell(1, extendedCells[1]);

        assertTrue(movableBlock.move(Direction.EAST, 1));
    }

    // Тестовый класс для тестирования абстрактного класса
    private static class TestBlock extends AbstractBlock {
        public TestBlock(Color color, Orientation orientation, int length) {
            super(color, orientation, length);
        }

        @Override
        public boolean move(Direction direction, int steps) {
            if (!isValidDirection(direction)) {
                return false;
            }

            Cell edgeCell = getEdgeCell(direction);
            for (int i = 0; i < steps; i++) {
                Cell nextCell = edgeCell.getNeighbor(direction);
                if (nextCell == null || !nextCell.isEmpty()) {
                    return false;
                }
                edgeCell = nextCell;
            }

            return true;
        }

        private boolean isValidDirection(Direction direction) {
            if (orientation == Orientation.HORIZONTAL) {
                return direction == Direction.WEST || direction == Direction.EAST;
            } else {
                return direction == Direction.NORTH || direction == Direction.SOUTH;
            }
        }

        private Cell getEdgeCell(Direction direction) {
            switch (direction) {
                case EAST:
                case SOUTH:
                    return ownerCells[length - 1];
                case WEST:
                case NORTH:
                default:
                    return ownerCells[0];
            }
        }

        // Публичный метод для тестирования protected метода
        public boolean canBelongToProtected(Cell cell, int index) {
            return canBelongTo(cell, index);
        }
    }
}