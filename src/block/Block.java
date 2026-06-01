package block;
import gamefield.*;

/**
 * Конкретный класс блока, реализующий логику перемещения.
 * Блок знает только о своих ячейках-владельцах, не знает о поле.
 * Может перемещаться в соседние незанятые ячейки по своей линии.
 */
public class Block extends AbstractBlock {

    /**
     * Конструктор блока.
     * @param color цвет блока
     * @param orientation ориентация блока
     * @param length длина блока
     */
    public Block(Color color, Orientation orientation, int length) {
        super(color, orientation, length);
    }

    /**
     * Переместить блок в заданном направлении.
     * @param direction направление движения
     * @param steps количество шагов
     * @return true, если перемещение успешно
     */
    @Override
    public boolean move(Direction direction, int steps) {
        // Проверяем, что направление соответствует ориентации блока
        if (!isValidDirection(direction)) {
            return false;
        }

        // Проверяем, можно ли переместиться на нужное количество шагов
        if (!canMove(direction, steps)) {
            return false;
        }

        // Освобождаем текущие ячейки
        clearOwnerCells();

        // Вычисляем новые позиции ячеек
        Cell[] newCells = calculateNewPositions(direction, steps);

        // Занимаем новые ячейки
        for (int i = 0; i < newCells.length; i++) {
            newCells[i].putUnit(this);
            setOwnerCell(i, newCells[i]);
        }

        return true;
    }

    /**
     * Проверить, является ли направление допустимым для данной ориентации.
     * @param direction направление
     * @return true, если направление допустимо
     */
    private boolean isValidDirection(Direction direction) {
        if (orientation == Orientation.HORIZONTAL) {
            return direction == Direction.WEST || direction == Direction.EAST;
        } else {
            return direction == Direction.NORTH || direction == Direction.SOUTH;
        }
    }

    /**
     * Проверить, может ли блок переместиться в заданном направлении.
     * @param direction направление
     * @param steps количество шагов
     * @return true, если может переместиться
     */
    private boolean canMove(Direction direction, int steps) {
        // Получаем крайнюю ячейку в направлении движения
        Cell edgeCell = getEdgeCell(direction);

        // Проверяем все ячейки на пути
        for (int i = 1; i <= steps; i++) {
            Cell nextCell = edgeCell.getNeighbor(direction);
            if (nextCell == null || !nextCell.isEmpty()) {
                return false;
            }
            edgeCell = nextCell;
        }

        return true;
    }

    /**
     * Получить крайнюю ячейку блока в заданном направлении.
     * @param direction направление
     * @return крайняя ячейка
     */
    private Cell getEdgeCell(Direction direction) {
        if (direction == Direction.EAST || direction == Direction.SOUTH) {
            return ownerCells[length - 1];
        } else {
            return ownerCells[0];
        }
    }

    /**
     * Вычислить новые позиции ячеек после перемещения.
     * @param direction направление
     * @param steps количество шагов
     * @return массив новых ячеек
     */
    private Cell[] calculateNewPositions(Direction direction, int steps) {
        Cell[] newCells = new Cell[length];

        for (int i = 0; i < length; i++) {
            Cell currentCell = ownerCells[i];
            Cell newCell = currentCell;

            for (int j = 0; j < steps; j++) {
                newCell = newCell.getNeighbor(direction);
            }

            newCells[i] = newCell;
        }

        return newCells;
    }

    /**
     * Освободить все текущие ячейки блока.
     */
    private void clearOwnerCells() {
        for (Cell cell : ownerCells) {
            if (cell != null) {
                cell.extractUnit(this);
            }
        }
    }

    /**
     * Проверить, достиг ли красный блок выхода.
     * @param exitSide сторона выхода
     * @param exitPosition позиция выхода
     * @return true, если блок достиг выхода
     */
    public boolean reachedExit(Direction exitSide, int exitPosition) {
        if (!isRedBlock()) {
            return false;
        }

        // Проверяем совместимость ориентации с выходом
        if (!isOrientationCompatible(exitSide)) {
            return false;
        }

        // Получаем крайнюю ячейку блока (которая должна выйти)
        Cell edgeCell = getEdgeCellForExit(exitSide);

        // Проверяем, находится ли крайняя ячейка на позиции выхода
        return isCellAtExitPosition(edgeCell, exitSide, exitPosition);
    }

    /**
     * Проверить, совместима ли ориентация блока со стороной выхода.
     * @param exitSide сторона выхода
     * @return true, если совместима
     */
    private boolean isOrientationCompatible(Direction exitSide) {
        // Для NORTH и SOUTH выхода нужен вертикальный блок
        if (exitSide == Direction.NORTH || exitSide == Direction.SOUTH) {
            return orientation == Orientation.VERTICAL;
        }
        // Для EAST и WEST выхода нужен горизонтальный блок
        else {
            return orientation == Orientation.HORIZONTAL;
        }
    }

    /**
     * Получить крайнюю ячейку для выхода.
     * @param exitSide сторона выхода
     * @return крайняя ячейка
     */
    private Cell getEdgeCellForExit(Direction exitSide) {
        if (exitSide == Direction.EAST || exitSide == Direction.SOUTH) {
            return ownerCells[length - 1];
        } else {
            return ownerCells[0];
        }
    }

    /**
     * Проверить, находится ли ячейка на позиции выхода.
     * @param cell ячейка
     * @param exitSide сторона выхода
     * @param exitPosition позиция выхода
     * @return true, если ячейка на позиции выхода
     */
    private boolean isCellAtExitPosition(Cell cell, Direction exitSide, int exitPosition) {
        int cellRow = cell.getRow();
        int cellCol = cell.getCol();

        switch (exitSide) {
            case EAST:
                return cellRow == exitPosition;
            case WEST:
                return cellRow == exitPosition;
            case SOUTH:
                return cellCol == exitPosition;
            case NORTH:
                return cellCol == exitPosition;
            default:
                return false;
        }
    }
}