import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class Board {
    public static final int SIZE = 8;
    Piece[][] board;

    public Board() {
        this.board = new Piece[SIZE][SIZE];
    }

    public Piece getPiece(@NotNull Location location) {
        return this.board[location.x()][location.y()];
    }

    public void setPiece(@NotNull Location location, @NotNull Piece piece) {
        Preconditions.checkArgument(board[location.x()][location.y()] == null, "Programming Error. Placing piece into already occupied Location");
        this.board[location.x()][location.y()] = new CheckersPiece(this, piece.getColor(), location);
    }

    public void setKingPiece(@NotNull Location location, @NotNull Piece piece) {
        Preconditions.checkArgument(board[location.x()][location.y()] == null, "Programming Error. Placing piece into already occupied Location");
        this.board[location.x()][location.y()] = new CheckersKingPiece(this, piece.getColor(), location);
    }

    public void removePiece(@NotNull Location location) {
        Preconditions.checkArgument(board[location.x()][location.y()] != null, "Programming Error. Removing non-existing Piece");
        this.board[location.x()][location.y()] = null;
    }
}
