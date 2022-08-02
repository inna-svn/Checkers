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

        // Default game pieces
        board.setPiece(new Location(0, 0), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(0, 2), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(0, 4), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(0, 6), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(1, 1), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(1, 3), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(1, 5), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(1, 7), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(2, 0), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(2, 2), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(2, 4), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(2, 6), new CheckersPiece(board, Piece.Color.WHITE));
        board.setPiece(new Location(5, 1), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(5, 3), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(5, 5), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(5, 7), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(6, 0), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(6, 2), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(6, 4), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(6, 6), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(7, 1), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(7, 3), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(7, 5), new CheckersPiece(board, Piece.Color.BLACK));
        board.setPiece(new Location(7, 7), new CheckersPiece(board, Piece.Color.BLACK));
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }

    @Override
    public Map<Piece, List<Move>> listPossibleMoves() {
        Preconditions.checkState(status == Status.IN_PROGRESS);
        //TODO MAP LOGIC Map<Piece, List<Move>> currMap = new Map<Piece, List<Move>>;

        // Go over all cells to check all available pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece currPiece = this.board.getPiece(new Location(row, col));

                // Check if current location is a game piece
                if (currPiece != null){
                    //currMap[currPiece] = currPiece.getlegalmoves()


                }
            }
        }


        return null; //return currMap
    }

    @Override
    public void makeMove(@NotNull User user, @NotNull Move move) {
        // TODO

        User temp = this.activeUser;
        this.activeUser = this.inactiveUser;
        inactiveUser = activeUser;
    }
}
