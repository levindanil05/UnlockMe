package game;

import block.Color;

/**
 * Интерфейс ограничения на перемещение блоков по цветам.
 * Определяет, можно ли двигать блок определенного цвета на данном ходу.
 */
public interface MoveRestriction {

    /**
     * Проверить, можно ли двигать блок заданного цвета.
     * @param color цвет блока
     * @param moveNumber номер хода (начиная с 1)
     * @return true, если можно двигать
     */
    boolean canMove(Color color, int moveNumber);

    /**
     * Получить описание текущего ограничения.
     * @param moveNumber номер хода
     * @return текстовое описание для отображения игроку
     */
    String getDescription(int moveNumber);
}