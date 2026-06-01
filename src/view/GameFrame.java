package view;

import game.Game;
import gamefield.GameField;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private final Game game;
    private GameFieldView fieldView;
    private final JLabel statusLabel;
    private final JLabel movesLabel;
    private final JLabel restrictionLabel;

    public GameFrame(Game game, GameField field, int gridSize) {
        this.game = game;
        setTitle("Rush Hour: Блоки и Выход");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(750, 650);
        setLocationRelativeTo(null);

        statusLabel = new JLabel("Статус: Игра активна", JLabel.CENTER);
        movesLabel = new JLabel("Ходов: 0", JLabel.LEFT);

        // Создаем метку для отображения ограничения
        restrictionLabel = new JLabel("", JLabel.CENTER);
        restrictionLabel.setFont(new Font("Arial", Font.BOLD, 13));
        restrictionLabel.setForeground(new Color(0, 100, 200));
        restrictionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        fieldView = new GameFieldView(game, field, gridSize, this);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(movesLabel, BorderLayout.WEST);
        infoPanel.add(restrictionLabel, BorderLayout.CENTER);
        topPanel.add(infoPanel, BorderLayout.SOUTH);

        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setBorder(BorderFactory.createTitledBorder("Управление"));
        helpPanel.add(new JLabel("Клик по блоку: выделить"));
        helpPanel.add(new JLabel("Стрелки: двигать вдоль линии"));
        helpPanel.add(new JLabel("Цель: вывести КРАСНЫЙ блок к выходу"));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(fieldView, BorderLayout.CENTER);
        mainPanel.add(helpPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        requestFocus();

        // Обновляем информацию об ограничении при старте
        updateUI();
    }

    public void updateUI() {
        statusLabel.setText(game.isOver() ?
                (game.hasPlayerWon() ? "Победа!" : "Проигрыш") :
                "Игра активна");

        movesLabel.setText("Ходов: " + game.getMoveCount());

        updateRestrictionInfo();
    }

    /**
     * Обновить информацию о текущем ограничении.
     */
    private void updateRestrictionInfo() {
        if (!game.isOver()) {
            String description = game.getLevel().getRestrictionDescription(game.getMoveCount() + 1);
            restrictionLabel.setText(description);
        } else {
            restrictionLabel.setText("");
        }
    }

    public void setGameNull() {
        if (fieldView != null) fieldView.setGameNull();
    }
}