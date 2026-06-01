package view;

import game.Game;
import game.MoveRestriction;
import gamefield.Cell;
import gamefield.Direction;
import gamefield.GameField;
import block.Block;
import block.AbstractBlock;
import block.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GameFieldView extends JPanel {
    private final Game game;
    private final GameField field;
    private final GameFrame frame;
    private final int gridSize;
    private final CellWidget[][] widgets;
    private Block selectedBlock = null;

    public GameFieldView(Game game, GameField field, int gridSize, GameFrame frame) {
        this.game = game;
        this.field = field;
        this.gridSize = gridSize;
        this.frame = frame;
        this.widgets = new CellWidget[gridSize][gridSize];
        initialize();
        setupInput();
    }

    public boolean isExitCell(int row, int col) {
        Direction exitSide = field.getExitSide();
        int exitPosition = field.getExitPosition();

        if (exitSide == null) return false;

        switch (exitSide) {
            case EAST:
                return col == gridSize - 1 && row == exitPosition;
            case WEST:
                return col == 0 && row == exitPosition;
            case SOUTH:
                return row == gridSize - 1 && col == exitPosition;
            case NORTH:
                return row == 0 && col == exitPosition;
            default:
                return false;
        }
    }

    private void initialize() {
        setPreferredSize(new Dimension(60 * gridSize, 60 * gridSize));
        setLayout(new GridLayout(gridSize, gridSize));
        setFocusable(true);

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                widgets[r][c] = new CellWidget(this, r, c);
                final int row = r;
                final int col = c;

                widgets[r][c].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleCellClick(row, col);
                    }
                });
                add(widgets[r][c]);
            }
        }
        updateField();
    }

    private void setupInput() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
    }

    public void updateField() {
        clearHighlights();

        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                widgets[r][c].update(field.getCell(r, c));
            }
        }

        if (selectedBlock != null) {
            highlightBlock(selectedBlock, true);
        }
    }

    private void handleCellClick(int row, int col) {
        if (game.isOver()) return;

        requestFocusInWindow();
        clearHighlights();

        Cell clicked = field.getCell(row, col);
        if (clicked != null && !clicked.isEmpty()) {
            List<AbstractBlock> units = clicked.getUnits();
            for (AbstractBlock u : units) {
                if (u instanceof Block block) {
                    selectedBlock = block;
                    highlightBlock(block, true);
                    System.out.println("Блок выбран: " + block.getColor());
                    return;
                }
            }
        }
        selectedBlock = null;
        System.out.println("Выбор снят");
    }

    private void highlightBlock(Block block, boolean highlight) {
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                Cell cell = field.getCell(r, c);
                if (cell != null && !cell.isEmpty()) {
                    List<AbstractBlock> units = cell.getUnits();
                    if (units.contains(block)) {
                        widgets[r][c].setHighlighted(highlight);
                    }
                }
            }
        }
    }

    private void clearHighlights() {
        for (int r = 0; r < gridSize; r++)
            for (int c = 0; c < gridSize; c++)
                widgets[r][c].setHighlighted(false);
    }

    private void handleKeyPress(int keyCode) {
        System.out.println("Нажата клавиша: " + keyCode);

        if (game.isOver() || selectedBlock == null) {
            System.out.println("Игнор: игра окончена или блок не выбран");
            return;
        }

        Direction dir = null;
        String dirName = "";

        switch (keyCode) {
            case KeyEvent.VK_UP -> { dir = Direction.NORTH; dirName = "ВВЕРХ"; }
            case KeyEvent.VK_DOWN -> { dir = Direction.SOUTH; dirName = "ВНИЗ"; }
            case KeyEvent.VK_LEFT -> { dir = Direction.WEST; dirName = "ВЛЕВО"; }
            case KeyEvent.VK_RIGHT -> { dir = Direction.EAST; dirName = "ВПРАВО"; }
            default -> {
                System.out.println("Неподдерживаемая клавиша");
                return;
            }
        }

        System.out.println("Попытка движения: " + dirName);

        MoveRestriction restriction = game.getLevel().getMoveRestriction();
        int nextMoveNumber = game.getMoveCount() + 1;
        Color blockColor = selectedBlock.getColor();

        if (restriction != null && !restriction.canMove(blockColor, nextMoveNumber)) {
            System.out.println("Нельзя двигать блок цвета " + blockColor +
                    " на ходу " + nextMoveNumber);
            System.out.println(restriction.getDescription(nextMoveNumber));
            return;
        }

        boolean moved = game.moveBlock(selectedBlock, dir, 1);
        System.out.println("Результат движения: " + (moved ? "УСПЕХ" : "ОТКАЗ"));

        if (moved) {
            updateField();
            frame.updateUI();

            if (game.isOver()) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            game.hasPlayerWon() ? "Победа!" : "Поражение",
                            "Игра окончена", JOptionPane.INFORMATION_MESSAGE);
                });
            }
        }
    }

    public void setGameNull() {
        clearHighlights();
        selectedBlock = null;
    }
}