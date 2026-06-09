package gamefield;

import game.Block;

import java.util.*;

/**
 * Класс ячейки игрового поля.
 * Ячейка представляет собой квадратную область поля.
 * Знает о граничащих с ней соседних ячейках и блоках, которые в ней расположены.
 */
public class Cell {
    private List<Block> units;          // Блоки, расположенные в ячейке
    private Map<Direction, Cell> neighbors;     // Соседние ячейки
    private int row;                            // Номер строки
    private int col;                            // Номер столбца

    /**
     * Конструктор ячейки.
     * @param row номер строки
     * @param col номер столбца
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.units = new ArrayList<>();
        this.neighbors = new EnumMap<>(Direction.class);
    }

    /**
     * Разместить блок в ячейке.
     * @param unit блок для размещения
     * @return true, если блок успешно размещен
     */
    public boolean putUnit(Block unit) {
        if (unit == null) {
            return false;
        }

        if (!units.contains(unit)) {
            units.add(unit);
            return true;
        }

        return false;
    }

    /**
     * Извлечь блок из ячейки.
     * @param unit блок для извлечения
     * @return true, если блок успешно извлечен
     */
    public boolean extractUnit(Block unit) {
        return units.remove(unit);
    }

    /**
     * Проверить, пуста ли ячейка.
     * @return true, если ячейка пуста
     */
    public boolean isEmpty() {
        return units.isEmpty();
    }

    /**
     * Получить всех соседей ячейки.
     * @return карта направлений и соседних ячеек
     */
    public Map<Direction, Cell> getNeighbors() {
        return new EnumMap<>(neighbors);
    }

    /**
     * Получить соседа по направлению.
     * @param direction направление
     * @return соседняя ячейка или null
     */
    public Cell getNeighbor(Direction direction) {
        return neighbors.get(direction);
    }

    /**
     * Установить соседа по направлению.
     * @param direction направление
     * @param neighbor соседняя ячейка
     */
    public void setNeighbor(Direction direction, Cell neighbor) {
        neighbors.put(direction, neighbor);
    }

    /**
     * Проверить, является ли другая ячейка соседом.
     * @param other другая ячейка
     * @return true, если ячейка является соседом
     */
    public boolean isNeighbor(Cell other) {
        return neighbors.containsValue(other);
    }

    /**
     * Получить номер строки.
     * @return номер строки
     */
    public int getRow() {
        return row;
    }

    /**
     * Получить номер столбца.
     * @return номер столбца
     */
    public int getCol() {
        return col;
    }

    /**
     * Получить все блоки в ячейке.
     * @return список блоков
     */
    public List<Block> getUnits() {
        return new ArrayList<>(units);
    }

    @Override
    public String toString() {
        return String.format("Cell[%d,%d]", row, col);
    }
}