import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckersGame implements Game {

    User activeUser, inactiveUser;
    Board b = new Board();

    @Override
    public void start(@NotNull User user1, @NotNull User user2) {
        activeUser = user1;
        inactiveUser = user2;
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }

    @Override
    public List<Move> listPossibleMoves() {
        return null;
    }

    @Override
    public void makeMove(@NotNull Move move) {

    }
}
