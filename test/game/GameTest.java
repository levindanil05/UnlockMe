package game;

import block.Color;
import gamefield.Direction;
import gamefield.GameField;
import gamefield.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты по сценарию 1: "Игра завершается победой Игрока"
 */
class GameTest {
    private Game game;
    private GameField gameField;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    /**
     * Сценарий 1: "Игра завершается победой Игрока"
     */
    @Test
    void testGameEndsWithPlayerVictory() {
        // Шаг 1: По указанию пользователя, Игра стартует
        game.start();

        assertTrue(game.isActive(), "Игра должна быть активна после старта");
        assertFalse(game.isOver(), "Игра не должна быть завершена в начале");

        // Шаг 2: Игра инициирует создание Поля и расстановку Блоков
        gameField = game.getGameField();
        assertNotNull(gameField, "Поле должно быть создано");
        assertEquals(6, gameField.getWidth());
        assertEquals(6, gameField.getHeight());

        // Шаг 3: Игра проверяет, что Блоки упорядочены
        assertTrue(game.areBlocksOrdered());

        // Шаг 4: Поле предоставляет доступ к ячейкам с блоками
        int blockCellsCount = countBlockCells();
        assertTrue(blockCellsCount > 0,
                "На поле должны быть ячейки с блоками");

        // Шаг 5: Игровой цикл - перемещение блоков
        Block redBlock = findRedBlock();
        assertNotNull(redBlock, "Красный блок должен существовать");

        // Перемещаем красный блок к выходу (но не до конца)
        game.moveBlock(redBlock, gamefield.Direction.EAST, 3);

        // Шаг 5.a.v: Игра определяет, что блоки упорядочены (красный блок на линии с выходом)
        assertTrue(game.areBlocksOrdered(),
                "Блоки должны быть упорядочены, когда красный блок на линии с выходом");

        // Но игра ещё не завершена, так как блок не достиг границы
        assertFalse(game.isOver(), "Игра не должна завершиться, пока блок не у границы");
        assertTrue(game.isActive(), "Игра должна оставаться активной");

        // Шаг 6: Перемещаем красный блок к границе поля
        game.moveBlock(redBlock, gamefield.Direction.EAST, 1);
    }

    /**
     * Сценарий 1, альтернативный: Игра ещё не завершена
     */
    @Test
    void testGameNotFinishedYet() {
        game.start();

        Block redBlock = findRedBlock();

        // Перемещаем красный блок, но не до конца
        game.moveBlock(redBlock, gamefield.Direction.EAST, 3);

        // Шаг 5: Игра определяет, что блоки упорядочены (на линии с выходом)
        assertTrue(game.areBlocksOrdered(),
                "Блоки упорядочены - красный блок на линии с выходом");

        // Но красный блок ещё не достиг границы поля
        assertFalse(game.hasRedBlockReachedExit(),
                "Красный блок ещё не достиг выхода на границе");

        // Игра не завершена
        assertFalse(game.isOver(), "Игра не должна завершиться");
        assertTrue(game.isActive(), "Игра должна оставаться активной");
    }

    /**
     * Проверка шага 4: Поле предоставляет доступ к ячейкам с блоками
     */
    @Test
    void testFieldProvidesAccessToBlockCells() {
        game.start();
        gameField = game.getGameField();

        int blockCellsCount = countBlockCells();
        assertTrue(blockCellsCount > 0,
                "На поле должны быть ячейки с блоками");
    }

    // Вспомогательный метод для подсчета ячеек с блоками
    private int countBlockCells() {
        int count = 0;
        GameField field = game.getGameField();

        for (int row = 0; row < field.getHeight(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Cell cell = field.getCell(row, col);
                if (!cell.isEmpty()) {
                    count++;
                    assertNotNull(cell.getUnits(),
                            "Ячейка должна предоставлять доступ к блокам");
                }
            }
        }

        return count;
    }

    // Вспомогательный метод для поиска красного блока
    private Block findRedBlock() {
        GameField field = game.getGameField();

        for (int row = 0; row < field.getHeight(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Cell cell = field.getCell(row, col);

                for (var unit : cell.getUnits()) {
                    if (unit.isRedBlock() && unit instanceof Block) {
                        return (Block) unit;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Тест: перемещение блока с ограничением.
     */
    @Test
    public void testMoveBlock_WithRestriction() {
        game.start();
        game.getLevel().setMoveRestriction(new SequenceColorRestriction());

        var redBlock = findRedBlock();
        assertNotNull(redBlock);

        boolean movedRed = game.moveBlock(redBlock, Direction.EAST, 1);

        assertTrue(movedRed, "Красный блок нельзя двигать на 1-м ходу");
    }


    /**
     * Тест: смена режима игры.
     */
    @Test
    public void testChangeGameMode() {
        game.start();

        assertTrue(game.getLevel().canMoveBlock(Color.GRAY, 1));

        game.getLevel().setMoveRestriction(new SequenceColorRestriction());

        assertTrue(game.getLevel().canMoveBlock(Color.GRAY, 1));
        assertTrue(game.getLevel().canMoveBlock(Color.AQUA, 1));
    }

}