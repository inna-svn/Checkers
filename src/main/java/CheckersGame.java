import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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
        Piece.Color currColor = Piece.Color.WHITE;

        // Initialize board with game pieces
        for (int row = 0; row < 8; row++) {
            // For rows 0,1,2 color is white, skip row 3-4, then use color 5
            if (row == 3) {
                row = 5;
                currColor = Piece.Color.BLACK;
            }
            // Go over cols and place soldiers
            for (int col = ((row % 2) == 0) ? 0 : 1; col < 8; col = col + 2) {
                // Place a soldier at current location with the right color
                Location currLocation = new Location(row,col);
                board.setPiece(currLocation, new CheckersPiece(board, currColor, currLocation));
            }
        }
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }

    @Override
    public Map<Piece, List<Move>> listPossibleMoves() {
        Preconditions.checkState(status == Status.IN_PROGRESS);
        Map<Piece, List<Move>> currMap = new HashMap();

        // Go over all cells to check all available pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece currPiece = this.board.getPiece(new Location(row, col));

                // Check if current location is a game piece
                if (currPiece != null){
                    currMap.put(currPiece, currPiece.listPossibleMoves());
                }
            }
        }

        return currMap;
    }

    @Override
    public void makeMove(@NotNull User user, @NotNull Move move) {

        this.board.setPiece(move.end(), this.board.getPiece(move.start()));
        this.board.setPiece(move.start(), null);
        if (move.intermediates() != null) { this.board.setPiece(move.intermediates(), null); }

        Piece currPiece = this.board.getPiece(move.end());

        if ((currPiece.getColor() == Piece.Color.BLACK && move.end().getY() == 0) ||
            (currPiece.getColor() == Piece.Color.WHITE && move.end().getY() == 7))
        {
            this.board.setPiece(move.end(), new CheckersKingPiece(this.board, currPiece.getColor(), move.end()));
        }


        User temp = this.activeUser;
        this.activeUser = this.inactiveUser;
        inactiveUser = temp;
    }
}
