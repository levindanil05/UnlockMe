package game;

import block.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Ограничение с настраиваемой последовательностью фаз.
 * Каждая фаза определяет, сколько ходов и какой цвет разрешен.
 * После завершения всех фаз разрешаются любые цвета.
 */
public class SequenceColorRestriction implements MoveRestriction {

    private final List<ColorPhase> phases;  // Последовательность фаз
    private Color randomColor;               // Выбранный случайный цвет
    private final Random random;

    /**
     * Создать ограничение с заданной последовательностью фаз.
     * @param phases массив фаз
     */
    public SequenceColorRestriction(ColorPhase... phases) {
        this.phases = Arrays.asList(phases);
        this.random = new Random();
        this.randomColor = null;
    }

    @Override
    public boolean canMove(Color color, int moveNumber) {
        // Определяем, в какой фазе мы находимся
        PhaseResult phaseResult = getCurrentPhase(moveNumber);

        if (phaseResult == null) {
            // Все фазы завершены - разрешаем любой цвет
            return true;
        }

        ColorPhase phase = phaseResult.phase;

        if (phase.isRandom()) {
            // Для случайной фазы выбираем цвет при первом входе
            if (randomColor == null) {
                randomColor = getRandomColor();
            }
            return color == randomColor;
        } else {
            // Для обычной фазы проверяем конкретный цвет
            return color == phase.getAllowedColor();
        }
    }

    @Override
    public String getDescription(int moveNumber) {
        PhaseResult phaseResult = getCurrentPhase(moveNumber);

        if (phaseResult == null) {
            return " Можно двигать ЛЮБЫЕ блоки";
        }

        ColorPhase phase = phaseResult.phase;
        int movesInPhase = phaseResult.moveNumberInPhase;

        if (phase.isRandom()) {
            if (randomColor == null) {
                randomColor = getRandomColor();
            }
            return String.format("Ход %d/%d: можно двигать только %s блоки",
                    movesInPhase, phase.getDuration(), phase.getColorName(randomColor));
        } else {
            return String.format("Ход %d/%d: можно двигать только %s блоки",
                    movesInPhase, phase.getDuration(), phase.getColorName(phase.getAllowedColor()));
        }
    }

    /**
     * Определить текущую фазу для заданного хода.
     * @return результат с фазой и номером хода внутри фазы, или null если все фазы завершены
     */
    private PhaseResult getCurrentPhase(int moveNumber) {
        int currentMove = 0;

        for (ColorPhase phase : phases) {
            int phaseStart = currentMove + 1;
            int phaseEnd = currentMove + phase.getDuration();

            if (moveNumber >= phaseStart && moveNumber <= phaseEnd) {
                int moveInPhase = moveNumber - currentMove;
                return new PhaseResult(phase, moveInPhase);
            }

            currentMove += phase.getDuration();
        }

        // Все фазы завершены
        return null;
    }

    /**
     * Получить случайный цвет (кроме красного).
     */
    private Color getRandomColor() {
        Color[] colors = {
                Color.AQUA, Color.RED, Color.GRAY
        };
        return colors[random.nextInt(colors.length)];
    }

    /**
     * Вспомогательный класс для хранения результата поиска фазы.
     */
    private static class PhaseResult {
        final ColorPhase phase;
        final int moveNumberInPhase;

        PhaseResult(ColorPhase phase, int moveNumberInPhase) {
            this.phase = phase;
            this.moveNumberInPhase = moveNumberInPhase;
        }
    }
}