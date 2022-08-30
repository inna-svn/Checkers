import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        User newUser = new User("meirav");
//        UserGameScore gss = new UserGameScore(newUser,CheckersGame.class,3,4,4/3);
//        gss.computeRate();
//        try {
//            User activeUser = new User("inna");
//            activeUser=User.signIn("Meirav", "98745");
//            System.out.println(activeUser.toString());
//        } catch (User.SignInError signInError) {
//            signInError.printStackTrace();
//        }
        //   User newUser = new User("");
//        try {
//            newUser=User.signUp("new","123");
//        } catch (User.SignUpError signUpError) {
//            signUpError.printStackTrace();
//        }
        User activeUser = new User(3, "inna");
        User inactiveUser = new User(4, "ilya");
        Game g = new CheckersGame();
        g.start(activeUser, inactiveUser);

        Map<Piece, List<Move>> currMapp = new HashMap<>();

        //      g.makeMove(activeUser,new Move(new Location(2,4),new Location(3,3),null));
        //     g.makeMove(activeUser,new Move(new Location(3,3),new Location(4,2),null));
        //       g.makeMove(activeUser,new Move(new Location(3,3),new Location(4,4),null));
//
        //     g.makeMove(activeUser,new Move(new Location(2,0),new Location(3,1),null));
        //    g.makeMove(activeUser,new Move(new Location(1,1),new Location(2,0),null));

        currMapp = g.listPossibleMoves();
        List<Move> moves = currMapp.get(g.getBoard().getPiece(new Location(0, 1)));
        moves.forEach(move -> System.out.println(move));
        //String addUser = "INSERT INTO users VALUES(4,'Tamar','23587',0)";
        //db.execution(addUser);


    }

}
