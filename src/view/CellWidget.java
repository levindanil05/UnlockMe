package view;

import gamefield.Cell;
import game.Block;
import block.Color;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CellWidget extends JPanel {
    private final GameFieldView gameFieldView;
    private final int row;
    private final int col;
    private final JLabel label;
    private Cell cell;

    public CellWidget(GameFieldView gameFieldView, int row, int col) {
        this.gameFieldView = gameFieldView;
        this.row = row;
        this.col = col;
        setPreferredSize(new Dimension(60, 60));
        setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 2));
        setLayout(new BorderLayout());

        label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setOpaque(true);
        label.setBackground(java.awt.Color.WHITE);
        add(label, BorderLayout.CENTER);
    }

    public void update(Cell cell) {
        this.cell = cell;

        // Проверяем, является ли ячейка выходом
        boolean isExit = gameFieldView.isExitCell(row, col);

        if (cell == null || cell.isEmpty()) {
            // Пустая ячейка
            label.setText(isExit ? "ВЫХОД" : "");
            label.setBackground(isExit ? new java.awt.Color(100, 255, 100) : java.awt.Color.WHITE);
            setBorder(BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 1));
        } else {
            List<Block> units = cell.getUnits();
            Block unit = units.isEmpty() ? null : units.get(0);

            if (unit != null) {
                java.awt.Color bgColor = getColorForBlock(unit);
                label.setBackground(bgColor);

                if (isExit) {
                    label.setText("ВЫХОД");
                }

                setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 1));
            }
        }
    }

    private java.awt.Color getColorForBlock(Block unit) {
        switch (unit.getColor()) {
            case RED:
                return new java.awt.Color(220, 40, 40);
            case BLUE:
                return new java.awt.Color(30, 100, 200);
            case GREEN:
                return new java.awt.Color(30, 150, 30);
            case YELLOW:
                return new java.awt.Color(230, 200, 0);
            case ORANGE:
                return new java.awt.Color(240, 140, 0);
            case PURPLE:
                return new java.awt.Color(130, 0, 150);
            case AQUA:
                return new java.awt.Color(30, 200, 250);
            case GRAY:
            default:
                return new java.awt.Color(150, 150, 150);
        }
    }

    public void setHighlighted(boolean highlighted) {
        java.awt.Color borderColor = highlighted ? java.awt.Color.CYAN : java.awt.Color.BLACK;
        int thickness = highlighted ? 3 : 1;
        setBorder(BorderFactory.createLineBorder(borderColor, thickness));
        repaint();
    }

}