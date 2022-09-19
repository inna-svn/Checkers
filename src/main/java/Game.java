import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class Game {

    String NAME = null;
    protected Board board = new Board();
    protected User winner, loser;

    public String getName() {
        return NAME;
    }

    enum StartType {
        REGULAR,
        TEST1,
        TEST2
    }

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

    abstract void start(@NotNull User user1, @NotNull User user2, StartType startType);
    abstract void presetEndGame(@NotNull User user1, @NotNull User user2);
    abstract void presetKing(@NotNull User user1, @NotNull User user2);

    abstract User getActiveUser(); // Whose turn is it?
    abstract User getBlackUser();
    abstract User getWhiteUser();

    void abandon(User user) {
        status = Status.FINISHED;
    }

    abstract Map<Piece, List<Move>> listPossibleMoves();

    abstract void makeMove(@NotNull User user, @NotNull Move move);

    public Status getStatus() {
        return status;
    }

    abstract void processPossibleGameEnd();

    public User getWinner() {
        return winner;
    }

    public User getLoser() {
        return loser;
    }

    static List<Class<? extends Game>> allGamesClasses() {
        return new ArrayList<>(Arrays.asList(
                CheckersGame.class
                // Add new classes here
        ));
    }

    public Board getBoard() {
        return this.board;
    }

    public User userThatPlays(Piece.Color color) {
        if(color == Piece.Color.WHITE) {
            return getWhiteUser();
        } else {
            return getBlackUser();
        }
    }

    public boolean pieceBelongsToUser(@NotNull Piece piece, @NotNull User user) {
        return userThatPlays(piece.getColor()).equals(user);
    }

    public boolean userCanMovePiece(@NotNull User user, @Nullable Piece piece) {
        if(piece == null) {
            return false;
        }
        if(!getActiveUser().equals(user)) {
            return false;
        }
        return pieceBelongsToUser(piece, user);
    }

}
