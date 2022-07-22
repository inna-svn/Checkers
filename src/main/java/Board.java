import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class Board {
    public static final int SIZE = 8;
    Piece[][] board;

    public Board() {
        this.board = new Piece[SIZE][SIZE];
    }

    public Piece getPiece(Location location) {
        return board[location.y()][location.x()];
    }

    public void setPiece(@NotNull Location location, @NotNull Piece piece) {
        Preconditions.checkArgument(board[location.y()][location.x()] == null, "Programming Error. Placing piece into already occupied Location");
        board[location.y()][location.x()] = piece;
    }

    public void removePiece(@NotNull Location location) {
        Preconditions.checkArgument(board[location.y()][location.x()] != null, "Programming Error. Removing non-existing Piece");
        board[location.y()][location.x()] = null;

    }

    public void makeMove(@NotNull Move move) {

    }
}
