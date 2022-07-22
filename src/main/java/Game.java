import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Game {
    enum Status {
        IN_PROGRESS,
        WON
    }

    Status status = Status.IN_PROGRESS;
    User winner = null;

    void start(@NotNull User user1, @NotNull User user2);

    User getActiveUser(); // Whose turn is it?

    List<Move> listPossibleMoves();

    void makeMove(@NotNull Move move);

    default Status getStatus() {
        return status;
    }

    default User getWinner() {
        if (status == Status.IN_PROGRESS) {
            throw new RuntimeException("Programming Error. getWinner() called on IN_PROGRESS Game.");
        }
        return winner;
    }
}
