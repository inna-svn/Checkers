import java.util.List;

public interface Piece {
    enum Color {
        WHITE,
        BLACK
    }
    Location location = null;
    List<Move> listPossibleMoves();
}
