package test.game;

import game.BanColorRestriction;
import gamefield.GameField;
import block.Block;
import block.Color;
import block.Orientation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для режима ограничения блока при приближении красного к выходу.
 */
public class BanColorRestrictionTest {

    private void placeRedBlock(GameField field, int row, int col) {
        Block redBlock = new Block(Color.RED, Orientation.HORIZONTAL, 2);
        field.getCell(row, col).putUnit(redBlock);
        redBlock.setOwnerCell(0, field.getCell(row, col));

        field.getCell(row, col + 1).putUnit(redBlock);
        redBlock.setOwnerCell(1, field.getCell(row, col + 1));
    }

    @Test
    void test_NonBlueColorsAlwaysAllowed() {
        GameField field = new GameField(6, 6);
        field.setExit(gamefield.Direction.EAST, 2);
        BanColorRestriction restriction = new BanColorRestriction(field, 2, Color.BLUE);

        assertTrue(restriction.canMove(Color.RED, 1));
        assertTrue(restriction.canMove(Color.GRAY, 1));
        assertTrue(restriction.canMove(Color.GREEN, 1));
    }

    @Test
    void test_BlueAllowedWhenRedIsFar() {
        GameField field = new GameField(6, 6);
        field.setExit(gamefield.Direction.EAST, 2);

        placeRedBlock(field, 2, 0);

        BanColorRestriction restriction = new BanColorRestriction(field, 2, Color.BLUE);
        assertTrue(restriction.canMove(Color.BLUE, 1));
    }

    @Test
    void test_BlueBannedWhenRedIsClose() {
        GameField field = new GameField(6, 6);
        field.setExit(gamefield.Direction.EAST, 2);

        placeRedBlock(field, 2, 3);

        BanColorRestriction restriction = new BanColorRestriction(field, 2, Color.BLUE);
        assertFalse(restriction.canMove(Color.BLUE, 1));
    }

    @Test
    void test_GreenBannedWhenRedIsClose() {
        GameField field = new GameField(6, 6);
        field.setExit(gamefield.Direction.EAST, 2);

        placeRedBlock(field, 2, 3);

        BanColorRestriction restriction = new BanColorRestriction(field, 2, Color.GREEN);
        assertFalse(restriction.canMove(Color.GREEN, 1));
    }

    @Test
    void test_GrayBannedWhenRedIsClose() {
        GameField field = new GameField(6, 6);
        field.setExit(gamefield.Direction.EAST, 2);

        placeRedBlock(field, 2, 3);

        BanColorRestriction restriction = new BanColorRestriction(field, 2, Color.GRAY);
        assertFalse(restriction.canMove(Color.GRAY, 1));
    }

}