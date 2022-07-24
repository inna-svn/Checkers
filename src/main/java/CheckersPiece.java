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
        return null;
    }

    public Color getColor() {
        return color;
    }

}
