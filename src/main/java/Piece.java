import java.util.List;

public interface Piece {
    enum Color {
        WHITE,
        BLACK
    }
    List<Move> listPossibleMoves();

    public Color getColor();
}
