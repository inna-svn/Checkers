import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {

    @BeforeAll
    @org.junit.jupiter.api.Test
    void cleanDatabase() throws SQLException {
        String cleanScores = "DELETE FROM scores";
        String cleanUsers = "DELETE FROM users";
        Database.getDatabase().getConnection().createStatement().execute(cleanScores);
        Database.getDatabase().getConnection().createStatement().execute(cleanUsers);
    }

    @org.junit.jupiter.api.Test
    void userSignUp() throws User.SignUpError {
        User newUser1 = User.signUp("newSignUp", "11111");
        Assertions.assertEquals(newUser1.getUsername(), "newSignUp");
    }

    @org.junit.jupiter.api.Test
    void userSignIn() throws User.SignInError, User.SignUpError {
        User.signUp("new1", "22222");
        User userNew1 = User.signIn("new1", "22222");
        Assertions.assertEquals(userNew1.getUsername(), "new1");
    }

    @Test
    void userScoreTestGame() throws User.SignUpError {
        User userTest1, userTest2;
        userTest1 = User.signUp("inna", "12345");
        userTest2 = User.signUp("ilya", "12345");
        Assertions.assertEquals(userTest1.scoreForGame(CheckersGame.class).getWinsNum(), 0);
        Assertions.assertEquals(userTest2.scoreForGame(CheckersGame.class).getWinsNum(), 0);
    }

    @org.junit.jupiter.api.Test
    void userScoreTest() throws User.SignUpError {
        User userTest1;
        userTest1 = User.signUp("test1", "test1");
        User.signUp("test2", "test2");
        Assertions.assertEquals(userTest1.scoreForGame(CheckersGame.class).getWinsNum(), 0);
    }

}
