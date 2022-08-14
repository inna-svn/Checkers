import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        User activeUser = new User("inna");
        User inactiveUser = new User("ilya");
        Game g = new CheckersGame();
        g.start(activeUser,inactiveUser);

        Map<Piece, List<Move>> currMapp = new HashMap<>();

        g.makeMove(activeUser,new Move(new Location(2,4),new Location(3,3),null));
   //     g.makeMove(activeUser,new Move(new Location(3,3),new Location(4,2),null));
        g.makeMove(activeUser,new Move(new Location(3,3),new Location(4,4),null));

   //     g.makeMove(activeUser,new Move(new Location(2,0),new Location(3,1),null));
    //    g.makeMove(activeUser,new Move(new Location(1,1),new Location(2,0),null));

        currMapp = g.listPossibleMoves();
        List<Move> moves = currMapp.get(g.getBoard().getPiece(new Location(5,5)));
        moves.forEach(move -> System.out.println(move));
        //String addUser = "INSERT INTO users VALUES(4,'Tamar','23587',0)";
        //db.execution(addUser);


    }
}
