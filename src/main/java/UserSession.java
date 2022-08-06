import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@SessionScoped
@ManagedBean
@Named
public class UserSession implements Serializable {

    private String username;
    private String password;
    private User user;
    private String errorMessage = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    void signUp() {
        try {
            user = User.signUp(username, password);
            errorMessage = null;
        } catch (User.SignUpError e) {
            errorMessage = e.getMessage();
        }
        // TODO: Check if any lobby has enough players to start
        //       The lobby where u joined might be ready.
        //       Then startGame()
    }

    public String signIn() {
        // Note: auto-joins lobby
        // TODO: Check if any lobby has enough players to start
        //       The lobby where u joined might be ready.
        //       Then startGame()
        errorMessage = null;
        try {
            user = User.signIn(username, password);
            return "home.html?faces-redirect=true";
        } catch (User.SignInError e) {
            errorMessage = e.getMessage();
        }
        return null;
    }

    public String signOut() {
        user.signOut();
        user = null;
        return "index.html?faces-redirect=true";
    }

}
