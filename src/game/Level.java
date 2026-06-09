package game;
import block.Color;
import block.Orientation;
import gamefield.*;

/**
 * Класс уровня игры.
 * Отвечает за создание блоков и их расстановку на поле.
 * Проверяет корректность расположения красного блока относительно выхода.
 * Является фабрикой блоков и генератором начальной обстановки.
 */
public class Level {
    private GameField gameField;                      // Игровое поле
    private int fieldWidth;                           // Ширина поля
    private int fieldHeight;                          // Высота поля
    private BlockConfiguration[] blockConfigurations; // Конфигурации блоков
    private Direction exitSide;                        // Сторона выхода
    private int exitPosition;                         // Позиция выхода
    private MoveRestriction moveRestriction;          // Ограничение на перемещение

    /**
     * Конструктор уровня.
     * @param gameField игровое поле
     * @param width ширина поля
     * @param height высота поля
     */
    public Level(GameField gameField, int width, int height) {
        this.gameField = gameField;
        this.fieldWidth = width;
        this.fieldHeight = height;
        this.moveRestriction = new AnyColorRestriction(); // По умолчанию - без ограничений
    }


    /**
     * Создать поле.
     */
    public void createField() {
        gameField.setSize(fieldWidth, fieldHeight);
    }

    /**
     * Создать блоки.
     * @throws IllegalStateException если красный блок не соответствует выходу
     */
    public void createBlocks() {
        for (BlockConfiguration config : blockConfigurations) {
            createBlock(config);
        }
        // Проверяем корректность расположения красного блока относительно выхода
        if (!validateRedBlockPosition()) {
            throw new IllegalStateException(
                    "Некорректная установка блоков на поле: красный блок не находится напротив выхода!"
            );
        }
    }

    /**
     * Создать один блок.
     * @param config конфигурация блока
     */
    private void createBlock(BlockConfiguration config) {
        Block block = new Block(
                config.color,
                config.orientation,
                config.length
        );
        // Разместить блок на поле
        placeBlock(block, config.startRow, config.startCol);
    }

    /**
     * Разместить блок на поле.
     * @param block блок
     * @param startRow начальная строка
     * @param startCol начальный столбец
     */
    private void placeBlock(Block block, int startRow, int startCol) {
        Orientation orientation = block.getOrientation();
        int length = block.getLength();
        for (int i = 0; i < length; i++) {
            int row = startRow;
            int col = startCol;
            if (orientation == Orientation.HORIZONTAL) {
                col += i;
            } else {
                row += i;
            }

            Cell cell = gameField.getCell(row, col);
            if (cell != null) {
                cell.putUnit(block);
                block.setOwnerCell(i, cell);
            }
        }
    }

    /**
     * Проверить корректность расположения красного блока относительно выхода.
     * @return true, если красный блок находится напротив выхода
     */
    private boolean validateRedBlockPosition() {
        // Находим красный блок
        Block redBlock = findRedBlock();
        if (redBlock == null) {
            System.err.println("Ошибка: красный блок не найден на поле!");
            return false;
        }

        // Проверяем совместимость ориентации красного блока со стороной выхода
        if (!isOrientationCompatible(redBlock.getOrientation(), exitSide)) {
            System.err.println(
                    "Ошибка: ориентация красного блока (" + redBlock.getOrientation() +
                            ") не соответствует стороне выхода (" + exitSide + ")!"
            );
            return false;
        }

        // Проверяем, находится ли красный блок на правильной позиции относительно выхода
        if (!isRedBlockAtCorrectPosition(redBlock)) {
            System.err.println(
                    "Ошибка: красный блок не находится напротив выхода на позиции " + exitPosition + "!"
            );
            return false;
        }

        return true;
    }

    /**
     * Найти красный блок на поле.
     * @return красный блок или null
     */
    private Block findRedBlock() {
        for (Cell cell : getAllCells()) {
            for (Block block : cell.getUnits()) {
                if (block.isRedBlock() && block instanceof Block) {
                    return (Block) block;
                }
            }
        }
        return null;
    }

    /**
     * Проверить совместимость ориентации блока со стороной выхода.
     * @param orientation ориентация блока
     * @param exitSide сторона выхода
     * @return true, если совместима
     */
    private boolean isOrientationCompatible(Orientation orientation, Direction exitSide) {
        if (exitSide == Direction.NORTH || exitSide == Direction.SOUTH) {
            return orientation == Orientation.VERTICAL;
        }
        else {
            return orientation == Orientation.HORIZONTAL;
        }
    }

    /**
     * Проверить, находится ли красный блок на правильной позиции.
     * @param redBlock красный блок
     * @return true, если блок на правильной позиции
     */
    private boolean isRedBlockAtCorrectPosition(Block redBlock) {
        Cell[] ownerCells = redBlock.getOwnerCells();
        if (ownerCells == null || ownerCells.length == 0) {
            return false;
        }

        // Получаем первую ячейку блока для проверки позиции
        Cell firstCell = ownerCells[0];
        int blockRow = firstCell.getRow();
        int blockCol = firstCell.getCol();

        switch (exitSide) {
            case EAST:
            case WEST:
                return blockRow == exitPosition;
            case SOUTH:
            case NORTH:
                return blockCol == exitPosition;
            default:
                return false;
        }
    }

    /**
     * Получить все ячейки поля.
     * @return iterable ячеек
     */
    private Iterable<Cell> getAllCells() {
        return () -> gameField.iterator();
    }

    /**
     * Установить конфигурацию блоков.
     * @param configurations массив конфигураций
     */
    public void setBlockConfigurations(BlockConfiguration[] configurations) {
        this.blockConfigurations = configurations;
    }

    /**
     * Установить выход.
     * @param side сторона выхода
     * @param position позиция выхода
     */
    public void setExit(Direction side, int position) {
        this.exitSide = side;
        this.exitPosition = position;
        gameField.setExit(side, position);
    }

    /**
     * Получить игровое поле.
     * @return игровое поле
     */
    public GameField getGameField() {
        return gameField;
    }

    /**
     * Установить ограничение на перемещение блоков.
     * @param restriction ограничение
     */
    public void setMoveRestriction(MoveRestriction restriction) {
        this.moveRestriction = restriction;
    }

    /**
     * Получить текущее ограничение на перемещение.
     * @return ограничение
     */
    public MoveRestriction getMoveRestriction() {
        return moveRestriction;
    }

    /**
     * Проверить, можно ли двигать блок заданного цвета на текущем ходу.
     * @param color цвет блока
     * @param moveNumber номер хода
     * @return true, если можно двигать
     */
    public boolean canMoveBlock(Color color, int moveNumber) {
        if (moveRestriction == null) {
            return true; // По умолчанию - без ограничений
        }
        return moveRestriction.canMove(color, moveNumber);
    }

    /**
     * Получить описание текущего ограничения.
     * @param moveNumber номер хода
     * @return текстовое описание
     */
    public String getRestrictionDescription(int moveNumber) {
        if (moveRestriction == null) {
            return "Можно двигать ЛЮБЫЕ блоки";
        }
        return moveRestriction.getDescription(moveNumber);
    }

    /**
     * Внутренний класс для конфигурации блока.
     */
    public static class BlockConfiguration {
        public Color color;           // Цвет блока
        public Orientation orientation; // Ориентация блока
        public int length;            // Длина блока
        public int startRow;          // Начальная строка
        public int startCol;          // Начальный столбец

        /**
         * Конструктор конфигурации блока.
         * @param color цвет
         * @param orientation ориентация
         * @param length длина
         * @param startRow начальная строка
         * @param startCol начальный столбец
         */
        public BlockConfiguration(Color color, Orientation orientation, int length,
                                  int startRow, int startCol) {
            this.color = color;
            this.orientation = orientation;
            this.length = length;
            this.startRow = startRow;
            this.startCol = startCol;
        }
    }

    /**
     * Настроить ограничение для режима "Последовательность".
     */
    public void setupSequenceModeRestriction() {
        ColorPhase phase1 = new ColorPhase(3, Color.AQUA);  // 3 хода только AQUA
        ColorPhase phase2 = new ColorPhase(1);               // 1 ход случайный цвет

        setMoveRestriction(new SequenceColorRestriction(phase1, phase2));
    }
}