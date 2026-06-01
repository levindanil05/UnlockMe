package view;

import javax.swing.*;
import java.awt.*;

/**
 * Диалог выбора режима игры.
 */
public class GameModeSelector {

    /**
     * Показать диалог выбора режима игры.
     * @return выбранный режим игры
     */
    public static GameMode showModeSelectionDialog() {
        String[] options = {
                "Обычный режим (любые блоки)",
                "Режим последовательности (с ограничениями)",
                "Режим с ограничениями"
        };

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Выберите режим игры:", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JTextArea descriptionArea = new JTextArea(
                " Обычный режим:\n" +
                        "   - Можно двигать блоки любого цвета\n" +
                        "   - Классическая игра Rush Hour\n\n" +
                        "   - Режим последовательности:\n" +
                        "   - Ходы 1-3: только ГОЛУБЫЕ блоки\n" +
                        "   - Ход 4: случайный цвет\n" +
                        "   - Ходы 5+: любые блоки\n" +
                        "   - Красный блок только когда разрешен"
        );
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descriptionArea.setBackground(new Color(240, 240, 240));
        panel.add(descriptionArea, BorderLayout.CENTER);

        int result = JOptionPane.showOptionDialog(
                null,
                panel,
                "Rush Hour - Выбор режима",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (result == 0) {
            return GameMode.NORMAL;
        } else if (result == 1) {
            return GameMode.SEQUENCE;
        } else {
            return GameMode.BAN;
        }
    }
}