import block.Color;
import game.BanColorRestriction;
import game.Game;
import game.AnyColorRestriction;
import view.GameFrame;
import view.GameMode;
import view.GameModeSelector;

public class Main {
    public static void main(String[] args) {
        // Показываем диалог выбора режима
        GameMode mode = GameModeSelector.showModeSelectionDialog();

        // Создаем игру
        Game game = new Game();
        game.start();

        // Устанавливаем ограничение в зависимости от режима
        if (mode == GameMode.SEQUENCE) {
            game.getLevel().setupSequenceModeRestriction();
            System.out.println(" Выбран режим ПОСЛЕДОВАТЕЛЬНОСТИ");
        } else if (mode == GameMode.BAN) {
            game.getLevel().setMoveRestriction(new BanColorRestriction(game.getGameField(), 2, Color.AQUA));
            System.out.println(" Выбран режим ОГРАНИЧЕНИЯ");
        }
            else {
                game.getLevel().setMoveRestriction(new AnyColorRestriction());
                System.out.println(" Выбран ОБЫЧНЫЙ режим");
        }

        // Создаем окно игры
        new GameFrame(game, game.getGameField(), 6);
    }
}