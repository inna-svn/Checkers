import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
//        try {
//            User activeUser = new User("inna");
//            activeUser=User.signIn("Meirav", "98745");
//            System.out.println(activeUser.toString());
//        } catch (User.SignInError signInError) {
//            signInError.printStackTrace();
//        }
//        User newUser = new User("");
//        try {
//            newUser=User.signUp("new","123");
//        } catch (User.SignUpError signUpError) {
//            signUpError.printStackTrace();
//        }
        Game g = new CheckersGame();
        Board b = new Board();
        Database db = new Database();
    }

}
