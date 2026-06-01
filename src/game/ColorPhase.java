package game;

import block.Color;

/**
 * Представляет одну фазу в последовательности ограничений.
 * Определяет, сколько ходов длится фаза и какой цвет разрешен.
 */
public class ColorPhase {
    private final int duration;        // Сколько ходов длится фаза
    private final Color allowedColor;  // Разрешенный цвет
    private final boolean isRandom;    // true = случайный цвет

    /**
     * Создать фазу с конкретным цветом.
     * @param duration количество ходов
     * @param color разрешенный цвет
     */
    public ColorPhase(int duration, Color color) {
        this.duration = duration;
        this.allowedColor = color;
        this.isRandom = false;
    }

    /**
     * Создать фазу со случайным цветом.
     * @param duration количество ходов
     */
    public ColorPhase(int duration) {
        this.duration = duration;
        this.allowedColor = null;
        this.isRandom = true;
    }

    public int getDuration() {
        return duration;
    }

    public Color getAllowedColor() {
        return allowedColor;
    }

    public boolean isRandom() {
        return isRandom;
    }

    /**
     * Проверить, разрешен ли данный цвет в этой фазе.
     */
    public boolean isColorAllowed(Color color, Color randomColor) {
        if (isRandom) {
            return color == randomColor;
        }
        return color == allowedColor;
    }

    @Override
    public String toString() {
        if (isRandom) {
            return duration + " ход(ов): случайный цвет";
        }
        return duration + " ход(ов): " + getColorName(allowedColor);
    }

    public String getColorName(Color color) {
        if (color == null) return "неизвестный";
        switch (color) {
            case RED: return "КРАСНЫЙ";
            case BLUE: return "СИНИЙ";
            case GREEN: return "ЗЕЛЕНЫЙ";
            case YELLOW: return "ЖЕЛТЫЙ";
            case ORANGE: return "ОРАНЖЕВЫЙ";
            case PURPLE: return "ФИОЛЕТОВЫЙ";
            case AQUA: return "ГОЛУБОЙ";
            case GRAY: return "СЕРЫЙ";
            default: return color.name();
        }
    }
}