import org.jetbrains.annotations.NotNull;

public final class UserGameScore {
    private final User user;
    private final Class<? extends Game> gameClass;
    private int gamesNum;
    private int winsNum;
    private float rate;

    public UserGameScore(@NotNull User user, @NotNull Class<? extends Game> gameClass, int gamesNum, int winsNum, float rate) {
        this.user = user;
        this.gameClass = gameClass;
        this.gamesNum = gamesNum;
        this.winsNum = winsNum;
        this.rate = rate;

    }

    public User getUser() {
        return user;
    }

    public int getGamesNum() {
        return gamesNum;
    }

    public int getWinsNum() {
        return winsNum;
    }

    public float getRate() {
        return rate;
    }

    public Class<? extends Game> getGameClass() {
        return gameClass;
    }

    void updateFromGameOutcome(@NotNull Game.Outcome outcome) {
        if (outcome == Game.Outcome.ABANDONED) {
            return;
        }
        gamesNum += 1;
        if (outcome == Game.Outcome.WON) {
            winsNum += 1;
        }
        computeRate();

        Database.getDatabase().saveRate(user,rate);
    }

    void computeRate() {
        if (gamesNum == 0) {
            rate = 0;
        } else {
            rate = (float) winsNum / (float) gamesNum;
        }
    }
}
