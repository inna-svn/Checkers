import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;


public class DatabaseTest {


    @org.junit.jupiter.api.Test
    void userSignIn() {
        User existingUser = null;
        try {
            existingUser = User.signIn("new", "123");
        } catch (User.SignInError e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(existingUser.getUsername(), "new");
    }

    @org.junit.jupiter.api.Test
    void userSignUp() {
        try {
            User.signUp("new1", "123");
            User newUser = User.signIn("new1", "123");

            System.out.println("user: newUser signed up");
        } catch (User.SignUpError signUpError) {
            signUpError.printStackTrace();
        } catch (User.SignInError e) {
            throw new RuntimeException(e);
        }
    }

    //@Transactional(rollbackFor = { SQLException.class })
    //@Rollback(true)
//    void userSignUp() throws User.SignUpError {
//        //User existingUser = User.signUp("new", "123");
//    }


}
