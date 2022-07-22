import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface Game {
    enum Status {
        IN_PROGRESS,
        WON
    }

    Status status = Status.IN_PROGRESS;
    User winner = null;

    void start(@NotNull User user1, @NotNull User user2);

    User getActiveUser(); // Whose turn is it?

    Map<Piece, List<Move>> listPossibleMoves();

    void makeMove(@NotNull Move move);

    default Status getStatus() {
        return status;
    }

    default User getWinner() {
        Preconditions.checkState(status == Status.IN_PROGRESS, "getWinner() should only be called on IN_PROGRESS Game");
        return winner;
    }

}
