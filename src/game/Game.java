package game;
import block.Color;
import block.Orientation;
import game.Level;
import gamefield.GameField;

/**
 * Класс игры.
 * Организует общий игровой цикл.
 * Знает о поле и правилах игры.
 * Инициирует создание поля и расстановку всех блоков с помощью уровня.
 * Определяет окончание игры.
 */
public class Game {
    private GameField gameField;      // Игровое поле
    private Level level;              // Уровень
    private boolean gameActive;       // Флаг активности игры
    private boolean gameOver;         // Флаг окончания игры
    private boolean playerWon;        // Флаг победы игрока
    private int moveCount;            // Количество ходов

    /**
     * Конструктор игры.
     */
    public Game() {
        this.gameActive = false;
        this.gameOver = false;
        this.playerWon = false;
        this.moveCount = 0;
    }

    /**
     * Начать игру.
     * Инициирует создание поля и расстановку всех блоков с помощью уровня.
     */
    public void start() {
        // Создаем поле 6x6
        gameField = new GameField(6, 6);
        level = new Level(gameField, 6, 6);

        // Инициализируем поле и уровень
        level.createField();

        // Устанавливаем выход
        level.setExit(gamefield.Direction.EAST, 2);

        // Создаем блоки уровня
        createDefaultLevel();

        // Создаем блоки
        level.createBlocks();

        gameActive = true;
        gameOver = false;
        moveCount = 0;
    }

    /**
     * Создать стандартный уровень.
     */
    private void createDefaultLevel() {
        Level.BlockConfiguration[] configs = {
                new Level.BlockConfiguration(Color.RED, Orientation.HORIZONTAL, 2, 2, 0),
                new Level.BlockConfiguration(Color.GRAY, Orientation.HORIZONTAL, 2, 0, 1),
                new Level.BlockConfiguration(Color.GRAY, Orientation.HORIZONTAL, 2, 4, 2),
                new Level.BlockConfiguration(Color.AQUA, Orientation.VERTICAL, 2, 2, 5),
        };
        level.setBlockConfigurations(configs);
    }

    /**
     * Проверить, закончена ли игра.
     * @return true, если игра закончена
     */
    public boolean isOver() {
        return gameOver;
    }

    /**
     * Определить, что блоки упорядочены как требуется.
     * Проверяет, находится ли красный блок на одной линии с выходом.
     * @return true, если блоки упорядочены
     */
    public boolean areBlocksOrdered() {
        gamefield.Direction exitDirection = gameField.getExitSide();
        int exitPosition = gameField.getExitPosition();

        if (exitDirection == null) {
            return false;
        }

        // Проверяем все ячейки поля
        for (int row = 0; row < gameField.getHeight(); row++) {
            for (int col = 0; col < gameField.getWidth(); col++) {
                gamefield.Cell cell = gameField.getCell(row, col);
                for (var unit : cell.getUnits()) {
                    if (unit.isRedBlock() && unit instanceof Block) {
                        Block redBlock = (Block) unit;
                        gamefield.Cell[] ownerCells = redBlock.getOwnerCells();

                        if (ownerCells == null || ownerCells.length == 0) {
                            return false;
                        }

                        // Проверяем, находится ли красный блок на одной линии с выходом
                        switch (exitDirection) {
                            case EAST:
                            case WEST:
                                // Для EAST/WEST: блок должен быть в строке exitPosition
                                return ownerCells[0].getRow() == exitPosition;

                            case SOUTH:
                            case NORTH:
                                // Для SOUTH/NORTH: блок должен быть в столбце exitPosition
                                return ownerCells[0].getCol() == exitPosition;

                            default:
                                return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Проверить, достиг ли красный блок выхода на границе поля.
     * @return true, если красный блок достиг выхода
     */
    public boolean hasRedBlockReachedExit() {
        gamefield.Direction exitDirection = gameField.getExitSide();
        int exitPosition = gameField.getExitPosition();

        if (exitDirection == null) {
            return false;
        }

        for (int row = 0; row < gameField.getHeight(); row++) {
            for (int col = 0; col < gameField.getWidth(); col++) {
                gamefield.Cell cell = gameField.getCell(row, col);
                for (var unit : cell.getUnits()) {
                    if (unit.isRedBlock() && unit instanceof Block) {
                        Block redBlock = (Block) unit;
                        gamefield.Cell[] ownerCells = redBlock.getOwnerCells();

                        if (ownerCells == null || ownerCells.length == 0) {
                            return false;
                        }

                        // Проверяем, достиг ли красный блок границы поля
                        switch (exitDirection) {
                            case EAST:
                                gamefield.Cell rightEdge = ownerCells[ownerCells.length - 1];
                                return rightEdge.getRow() == exitPosition &&
                                        rightEdge.getCol() == gameField.getWidth() - 1;

                            case WEST:
                                gamefield.Cell leftEdge = ownerCells[0];
                                return leftEdge.getRow() == exitPosition &&
                                        leftEdge.getCol() == 0;

                            case SOUTH:
                                gamefield.Cell bottomEdge = ownerCells[ownerCells.length - 1];
                                return bottomEdge.getCol() == exitPosition &&
                                        bottomEdge.getRow() == gameField.getHeight() - 1;

                            case NORTH:
                                gamefield.Cell topEdge = ownerCells[0];
                                return topEdge.getCol() == exitPosition &&
                                        topEdge.getRow() == 0;

                            default:
                                return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Переместить блок.
     * @param block блок для перемещения
     * @param direction направление движения
     * @param steps количество шагов
     * @return true, если перемещение успешно
     */
    public boolean moveBlock(Block block, gamefield.Direction direction, int steps) {
        if (!gameActive || gameOver) {
            return false;
        }

        // Проверка ограничения перед ходом
        int nextMoveNumber = moveCount + 1;
        if (!level.canMoveBlock(block.getColor(), nextMoveNumber)) {
            System.out.println("Нельзя двигать блок цвета " + block.getColor() +
                    " на ходу " + nextMoveNumber);
            System.out.println(level.getRestrictionDescription(nextMoveNumber));
            return false;
        }

        boolean moved = block.move(direction, steps);
        if (moved) {
            moveCount++;
            checkWinCondition();
        }
        return moved;
    }

    /**
     * Проверить условие победы.
     * Определяет окончание игры.
     */
    private void checkWinCondition() {
        if (hasRedBlockReachedExit()) {
            gameOver = true;
            playerWon = true;
            gameActive = false;
        }
    }

    /**
     * Получить игровое поле.
     * @return игровое поле
     */
    public GameField getGameField() {
        return gameField;
    }

    /**
     * Получить уровень.
     * @return уровень
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Проверить, активна ли игра.
     * @return true, если игра активна
     */
    public boolean isActive() {
        return gameActive;
    }

    /**
     * Проверить, выиграл ли игрок.
     * @return true, если игрок выиграл
     */
    public boolean hasPlayerWon() {
        return playerWon;
    }

    /**
     * Получить количество ходов.
     * @return количество ходов
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Завершить игру досрочно.
     */
    public void endGame() {
        gameOver = true;
        gameActive = false;
    }
}