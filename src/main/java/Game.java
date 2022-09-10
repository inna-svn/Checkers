import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface Game {

    String NAME = null;
    String getName();

    enum Status {
        IN_PROGRESS,
        FINISHED
    }

    // Not OK
    enum Outcome {
        WON,
        LOST,
        ABANDONED
    }

    Status status = Status.IN_PROGRESS;
    User winner = null;

    void start(@NotNull User user1, @NotNull User user2);

    User getActiveUser(); // Whose turn is it?
    User getBlackUser();
    User getWhiteUser();

    default void abandon(User user) {
        // TODO
    }

    Map<Piece, List<Move>> listPossibleMoves();

    void makeMove(@NotNull User user, @NotNull Move move);

    default Status getStatus() {
        return status;
    }

    default User getWinner() {
        Preconditions.checkState(status == Status.FINISHED, "getWinner() should only be called on IN_PROGRESS Game");
        return winner;
    }

    static List<Class<? extends Game>> allGamesClasses() {
        return new ArrayList<>(Arrays.asList(
                CheckersGame.class
                // Add new classes here
        ));
    }
    public Board getBoard();
    public void PrintBoard();

}
