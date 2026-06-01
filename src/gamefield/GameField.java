package gamefield;

import java.util.*;

/**
 * Класс игрового поля.
 * Поле представляет собой прямоугольную область из ячеек.
 * Знает о своих размерах, ячейках и выходе.
 */
public class GameField {
    private int height;             // Высота поля
    private int width;              // Ширина поля
    private Cell[][] cells;         // Двумерный массив ячеек
    private Direction exitSide;      // Сторона выхода
    private int exitPosition;       // Позиция выхода на стороне

    /**
     * Конструктор игрового поля.
     * @param width ширина поля
     * @param height высота поля
     */
    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[height][width];
        createCells();
        setupNeighbors();
    }

    /**
     * Создать все ячейки поля.
     */
    private void createCells() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /**
     * Установить связи между соседними ячейками.
     */
    private void setupNeighbors() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell current = cells[row][col];

                if (row > 0) {
                    current.setNeighbor(Direction.NORTH, cells[row - 1][col]);
                }
                if (row < height - 1) {
                    current.setNeighbor(Direction.SOUTH, cells[row + 1][col]);
                }
                if (col > 0) {
                    current.setNeighbor(Direction.WEST, cells[row][col - 1]);
                }
                if (col < width - 1) {
                    current.setNeighbor(Direction.EAST, cells[row][col + 1]);
                }
            }
        }
    }

    /**
     * Установить выход.
     * @param side сторона выхода
     * @param position позиция выхода на стороне
     */
    public void setExit(Direction side, int position) {
        // Проверяем корректность позиции
        if (!isValidExitPosition(side, position)) {
            throw new IllegalArgumentException("Некорректная позиция выхода: " + position);
        }
        this.exitSide = side;
        this.exitPosition = position;
    }

    /**
     * Проверить корректность позиции выхода.
     * @param side сторона выхода
     * @param position позиция
     * @return true, если позиция корректна
     */
    private boolean isValidExitPosition(Direction side, int position) {
        if (side == Direction.NORTH || side == Direction.SOUTH) {
            return position >= 0 && position < width;
        } else {
            return position >= 0 && position < height;
        }
    }

    /**
     * Получить сторону выхода.
     * @return сторона выхода
     */
    public Direction getExitSide() {
        return exitSide;
    }

    /**
     * Получить позицию выхода.
     * @return позиция выхода
     */
    public int getExitPosition() {
        return exitPosition;
    }

    /**
     * Получить ячейку по координатам.
     * @param row номер строки
     * @param col номер столбца
     * @return ячейка или null
     */
    public Cell getCell(int row, int col) {
        if (isValidPosition(row, col)) {
            return cells[row][col];
        }
        return null;
    }

    /**
     * Проверить корректность позиции.
     * @param row номер строки
     * @param col номер столбца
     * @return true, если позиция корректна
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    /**
     * Получить итератор по всем ячейкам.
     * @return итератор
     */
    public Iterator<Cell> iterator() {
        return new GameFieldIterator();
    }

    /**
     * Установить размер поля.
     * @param width ширина поля
     * @param height высота поля
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        createCells();
        setupNeighbors();
    }

    /**
     * Деактивировать поле.
     */
    public void deactivate() {
        // Очистка всех ячеек
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                // Очистка ячеек при необходимости
            }
        }
    }

    /**
     * Получить высоту поля.
     * @return высота
     */
    public int getHeight() {
        return height;
    }

    /**
     * Получить ширину поля.
     * @return ширина
     */
    public int getWidth() {
        return width;
    }

    /**
     * Внутренний класс итератора по полю.
     */
    private class GameFieldIterator implements Iterator<Cell> {
        private int currentRow = 0;
        private int currentCol = 0;

        /**
         * Проверить, есть ли следующая ячейка.
         * @return true, если есть
         */
        @Override
        public boolean hasNext() {
            return currentRow < height;
        }

        /**
         * Получить следующую ячейку.
         * @return следующая ячейка
         */
        @Override
        public Cell next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Cell cell = cells[currentRow][currentCol];

            currentCol++;
            if (currentCol >= width) {
                currentCol = 0;
                currentRow++;
            }

            return cell;
        }
    }
}