package view;

/**
 * Режимы игры.
 */
public enum GameMode {
    NORMAL("Обычный режим"),
    SEQUENCE("Режим последовательности");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}