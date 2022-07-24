import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CheckersGame implements Game {

    User activeUser, inactiveUser;
    Board board = new Board();

    @Override
    public void start(@NotNull User user1, @NotNull User user2) {
        activeUser = user1;
        inactiveUser = user2;
        // TODO: Put pieces on the board like this:
        board.setPiece(new Location(0, 0), new CheckersPiece(board, Piece.Color.WHITE));
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }

    @Override
    public Map<Piece, List<Move>> listPossibleMoves() {
        Preconditions.checkState(status == Status.IN_PROGRESS);
        return null;
    }

    @Override
    public void makeMove(@NotNull User user, @NotNull Move move) {
        // TODO
    }
}
