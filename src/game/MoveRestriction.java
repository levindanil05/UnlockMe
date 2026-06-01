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

    default String getColorName(Color color) {
        if (color == null) return "неизвестный";
        switch (color) {
            case RED:
                return "КРАСНЫЕ";
            case BLUE:
                return "СИНИЕ";
            case GREEN:
                return "ЗЕЛЕНЫЕ";
            case YELLOW:
                return "ЖЕЛТЫЕ";
            case ORANGE:
                return "ОРАНЖЕВЫЕ";
            case PURPLE:
                return "ФИОЛЕТОВЫЕ";
            case AQUA:
                return "ГОЛУБЫЕ";
            case GRAY:
                return "СЕРЫЕ";
            default:
                return color.name();
        }
    }
}