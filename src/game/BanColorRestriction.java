package game;

import block.Color;
import gamefield.Cell;
import gamefield.Direction;
import gamefield.GameField;

/**
 * Ограничение на передвижение блока определённого цвета с момента,
 * когда красный(целевой) блок достигает оперделённого расстояния до финиша
 */
public class BanColorRestriction implements MoveRestriction  {

    private final GameField gameField;
    private final int banDistance;
    private final Color bannedColor;

    public BanColorRestriction(GameField _gameField, int _banDistance, Color _bannedColor){
        gameField = _gameField;
        banDistance = _banDistance;
        bannedColor = _bannedColor;
    }

    @Override
    public boolean canMove(Color color, int moveNumber){
        if (color != bannedColor) {
            return true;
        }

        if(isRedBlockCloseToExit()){
            return false;
        }

        return true;
    }

    private boolean isRedBlockCloseToExit(){
        int distance = getRedBlockDisanceToExit();
        return distance <= banDistance;
    }

    private int getRedBlockDisanceToExit(){
        if (gameField == null) {
            return Integer.MAX_VALUE;
        }

        Direction exitSide = gameField.getExitSide();

        if (exitSide == null){
            return Integer.MAX_VALUE;
        }

        Cell redBlockCell = findRedBlockCell();
        if (redBlockCell == null) {
            return Integer.MAX_VALUE;
        }

        return calculateDistanceToExit(redBlockCell, exitSide);
    }

    private Cell findRedBlockCell() {
        for (int row = 0; row < gameField.getHeight(); row++)
            for (int col = 0; col < gameField.getWidth(); col++) {
                Cell cell = gameField.getCell(row, col);
                if (cell != null) {
                    for (var unit : cell.getUnits()){
                        if (unit.isRedBlock()){
                            return cell;
                        }
                    }
                }
            }
            return null;
    }

    private int calculateDistanceToExit(Cell cell, Direction exitSide) {
        int cellRow = cell.getRow();
        int cellCol = cell.getCol();
        int width = gameField.getWidth();
        int height = gameField.getWidth();

        switch (exitSide) {
            case EAST :
                return (width -1) - cellCol;
            case WEST :
                return cellCol;
            case NORTH:
                return (height -1) - cellRow;
            case SOUTH:
                return cellRow;

            default:
                return Integer.MAX_VALUE;

        }
    }

    @Override
    public String getDescription(int moveNumber) {
        if(isRedBlockCloseToExit()) {
            return String.format(getColorName(bannedColor)+ " блоки запрещено двигать");
        }
        else {
            int distance = getRedBlockDisanceToExit();
            return String.format("Можно двигать любые блоки");
        }
    }


}
