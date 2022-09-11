import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {

    @BeforeAll
    @org.junit.jupiter.api.Test
    void cleanDatabase(){
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
        User existingUser = null;
        try {
            existingUser = User.signUp("new1", "123");
            //User.signIn("new1", "123");
        } catch (User.SignUpError signUpError) {
            signUpError.printStackTrace();
        }
        Assertions.assertEquals(existingUser.getUsername(), "new1");
    }

    @org.junit.jupiter.api.Test
    void userSignIn() {
        User userNew2 = null;
        try {
            userNew2 = User.signUp("new2", "123");
            userNew2 = User.signIn("new2", "123");
        } catch (User.SignInError e) {
            throw new RuntimeException(e);
        } catch (User.SignUpError e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(userNew2.getUsername(), "new2");
    }

    @org.junit.jupiter.api.Test
    void userScoreTest() {
        User userTest1, userTest2;
        try {
            userTest1 = User.signUp("test1", "12345");
            userTest2 = User.signUp("test2", "12345");
            Assertions.assertEquals(userTest1.scoreForGame(CheckersGame.class).getWinsNum(),0);
        } catch (User.SignUpError | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
