import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collection;

@SessionScoped
@ManagedBean
@Named
public class UserSession implements Serializable {

    @Inject
    LobbyEndpoint lobbyEndpoint;

    @Inject
    GameApplication gameApplication;

    private String username;
    private String password;
    private User user;
    private String errorMessage = null;

    private UserGameScore score;

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

    public User getUser() {
        return user;
    }

    public UserGameScore getScore() { return score;}
    public boolean isSignedIn() {
        return user != null;
    }

    public Lobby getLobby() {
        return user.getLobby();
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

    public void scoreForGame(Class<? extends Game> gameClass){
        this.score = user.scoreForGame(gameClass);
    }

    public Collection<Lobby> getAvailableLobbies() {
        return user.getAvailableLobbies();
    }

    public String joinLobby(Lobby lobby) {
        user.joinLobby(lobby);
        lobbyEndpoint.onLobbyUserListChange(lobby);
        if(lobby.canStartGame()) {
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(lobby.canStartGame()) {
                    var users = lobby.getUsers();
                    Game game = gameApplication.startGame(lobby);
                    // TODO: make /game/{id} URLs work
                    // TODO: pass the new game URL so that the page could redirect to
                    lobbyEndpoint.startGame(lobby, game, users);
                }
            }).start();
        }

        return "lobby.xhtml?faces-redirect=true";
    }

}
