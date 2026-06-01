package game;

import block.Color;

/**
 * Ограничение: можно двигать блоки любого цвета.
 * Обычный режим игры без ограничений.
 */
public class AnyColorRestriction implements MoveRestriction {

    @Override
    public boolean canMove(Color color, int moveNumber) {
        return true; // Всегда можно двигать любой цвет
    }

    @Override
    public String getDescription(int moveNumber) {
        return "Можно двигать ЛЮБЫЕ блоки";
    }
}