import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    }

    void computeRate() {
        if (gamesNum == 0) {
            rate = 0;
        } else {
            rate = (float) winsNum / (float) gamesNum;
        }
        // Insert data to database
        String name = this.user.getUsername();
        try {
            try (PreparedStatement preparedUpdate = Database.getDatabase().getConnection().prepareStatement("UPDATE scores SET rate = ? WHERE userId = ? ")) {
                preparedUpdate.setInt(1, (int) rate);
                preparedUpdate.setInt(2, user.getId());
                preparedUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
