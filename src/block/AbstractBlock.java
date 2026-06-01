package block;
import gamefield.*;

/**
 * Абстрактный класс блока.
 * Блок может перемещаться по своей линии в незанятые ячейки.
 * Имеет цвет, ориентацию и знает о своих ячейках-владельцах.
 */
public abstract class AbstractBlock {
    protected Color color;              // Цвет блока
    protected Orientation orientation;  // Ориентация блока
    protected Cell[] ownerCells;        // Ячейки-владельцы, в которых расположен блок
    protected int length;               // Длина блока (количество занимаемых ячеек)

    /**
     * Конструктор блока.
     * @param color цвет блока
     * @param orientation ориентация блока
     * @param length длина блока
     */
    public AbstractBlock(Color color, Orientation orientation, int length) {
        this.color = color;
        this.orientation = orientation;
        this.length = length;
        this.ownerCells = new Cell[length];
    }

    /**
     * Получить ячейки-владельцы блока.
     * @return массив ячеек
     */
    public Cell[] getOwnerCells() {
        return ownerCells;
    }

    /**
     * Установить ячейку-владельца по индексу.
     * @param index индекс ячейки в блоке
     * @param cell ячейка
     */
    public void setOwnerCell(int index, Cell cell) {
        if (index >= 0 && index < ownerCells.length) {
            ownerCells[index] = cell;
        }
    }

    /**
     * Получить цвет блока.
     * @return цвет
     */
    public Color getColor() {
        return color;
    }

    /**
     * Получить ориентацию блока.
     * @return ориентация
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Получить длину блока.
     * @return длина
     */
    public int getLength() {
        return length;
    }

    /**
     * Проверить, может ли блок принадлежать данной ячейке.
     * @param cell ячейка
     * @param index индекс в блоке
     * @return true, если может
     */
    protected boolean canBelongTo(Cell cell, int index) {
        return cell != null && index >= 0 && index < length;
    }

    /**
     * Абстрактный метод перемещения блока.
     * @param direction направление движения
     * @param steps количество шагов
     * @return true, если перемещение успешно
     */
    public abstract boolean move(Direction direction, int steps);

    /**
     * Проверить, является ли блок красным.
     * @return true, если блок красный
     */
    public boolean isRedBlock() {
        return color == Color.RED;
    }
}