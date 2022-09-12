import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {

    @BeforeAll
    @org.junit.jupiter.api.Test
    void cleanDatabase() {
        String cleanScores = "DELETE FROM scores";
        String cleanUsers = "DELETE FROM users";
        try {
            Database.getDatabase().getConnection().createStatement().execute(cleanScores);
            Database.getDatabase().getConnection().createStatement().execute(cleanUsers);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void userSignUp() {
        User newUser1 = null;
        try {
            newUser1 = User.signUp("newSignUp", "11111");
        } catch (User.SignUpError signUpError) {
            signUpError.printStackTrace();
        }
        Assertions.assertEquals(newUser1.getUsername(), "newSignUp");
    }

    @org.junit.jupiter.api.Test
    void userSignIn() {
        User userNew1 = null;
        try {
            userNew1 = User.signUp("new1", "22222");
            userNew1 = User.signIn("new1", "22222");
        } catch (User.SignUpError | User.SignInError e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(userNew1.getUsername(), "new1");
    }

    @Test
    void userScoreTestGame() {
        User userTest1, userTest2;
        try {
            userTest1 = User.signUp("inna", "12345");
            userTest2 = User.signUp("ilya", "12345");
            Assertions.assertEquals(userTest1.scoreForGame(CheckersGame.class).getWinsNum(), 0);
            Assertions.assertEquals(userTest2.scoreForGame(CheckersGame.class).getWinsNum(), 0);
        } catch (User.SignUpError | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void userScoreTest() {
        User userTest1, userTest2;
        try {
            userTest1 = User.signUp("test1", "test1");
            userTest2 = User.signUp("test2", "test2");
            Assertions.assertEquals(userTest1.scoreForGame(CheckersGame.class).getWinsNum(), 0);
        } catch (User.SignUpError | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
