import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        this.board[location.x()][location.y()] =new CheckersPiece(this, piece.getColor() ,location);
    }
    public void setKingPiece(@NotNull Location location, @NotNull Piece piece) {
        Preconditions.checkArgument(board[location.x()][location.y()] == null, "Programming Error. Placing piece into already occupied Location");
        this.board[location.x()][location.y()] =new CheckersKingPiece(this, piece.getColor() ,location);
    }
    public void removePiece(@NotNull Location location) {
        Preconditions.checkArgument(board[location.x()][location.y()] != null, "Programming Error. Removing non-existing Piece");
        this.board[location.x()][location.y()] = null;

//        System.out.println("piece "+location+"was removed");

    }

    public void makeMove(@NotNull Move move) {

    }

    public List<List<Piece>> getAllPiecesInRowsAndColumns() {
        List<List<Piece>> rows = new ArrayList<>(SIZE);
        List<Piece> rowPieces;

        for (int row = 0; row < SIZE; row++) {
            rowPieces = new ArrayList<>(SIZE);
            for (int col = 0; col < SIZE; col++) {
                rowPieces.add(getPiece(new Location(row, col))); // XXX: inverse row & col
            }
            rows.add(rowPieces);
        }
        return rows;
    }
}
