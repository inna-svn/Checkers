import java.util.List;

public interface Piece {
    enum Color {
        WHITE,
        BLACK
    }
    List<Move> listPossibleMoves();

    Color getColor();
    Location getLocation();
}
