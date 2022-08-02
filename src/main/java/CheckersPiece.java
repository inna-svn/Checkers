import java.util.List;

import org.jetbrains.annotations.NotNull;

public class CheckersPiece implements Piece {

    protected Board board;

    protected Color color;

    public CheckersPiece(@NotNull Board board, @NotNull Color pieceColor) {
        this.board = board;
        this.color = pieceColor;
    }

    @Override
    public List<Move> listPossibleMoves() {
        List<Move> moves = null;
        int colorMod = this.color == Color.WHITE ? 1 : -1;
        // Check if next cells are available
        try {
            if (this.board.getPiece(new Location(location.x() + colorMod, location.y() + 1)) == null) {
                moves.add(new Move(new Location(location.x(), location.y()), new Location(location.x() + colorMod, location.y() + 1), null));
            }
            else{

            }


        } catch {
        }
        try {
            if (this.board.getPiece(new Location(location.x() + colorMod, location.y() - 1)) == null) {
                moves.add(new Move(new Location(location.x(), location.y()), new Location(location.x() + colorMod, location.y() - 1), null));
            }
        } catch {
        }

        // Check if next cells are blocked and can be eaten


        return null;
    }

    public Color getColor() {
        return color;
    }

}
