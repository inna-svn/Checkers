import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        User activeUser = new User(3, "inna");
        User inactiveUser = new User(4, "ilya");
        Game g = new CheckersGame();
        g.start(activeUser, inactiveUser, Game.StartType.REGULAR);

        Map<Piece, List<Move>> currMapp;

        currMapp = g.listPossibleMoves();
        List<Move> moves = currMapp.get(g.getBoard().getPiece(new Location(0, 1)));
        moves.forEach(System.out::println);

    }

}
