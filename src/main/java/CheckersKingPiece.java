import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckersKingPiece extends CheckersPiece {
    public CheckersKingPiece(@NotNull Board board, @NotNull Color pieceColor) {
        super(board, pieceColor);
    }

    @Override
    public List<Move> listPossibleMoves() {
        // TODO: actually different from regular piece
        return super.listPossibleMoves();
    }
}
