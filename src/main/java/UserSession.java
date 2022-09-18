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

    public boolean isSignedIn() {
        return user != null;
    }

    // Possible improvement: allow being in multiple lobbies simultaneously
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

    public Collection<Lobby> getAvailableLobbies() {
        return user.getAvailableLobbies();
    }

    public String joinLobby(Lobby lobby,boolean endPreset,boolean kingPreset) {
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
                    Game game;
                    if (endPreset)
                    game = gameApplication.startGame(lobby,true,false);
                    else if (kingPreset)
                    game = gameApplication.startGame(lobby,false,true);
                    else
                    game = gameApplication.startGame(lobby,false,false);

                    // TODO: make /game/{id} URLs work
                    // TODO: pass the new game URL so that the page could redirect to
                    lobbyEndpoint.startGame(lobby, game, users);
                }
            }).start();
        }

        return "lobby.xhtml?faces-redirect=true";
    }

    public void test() {
        System.err.println("UserSession#test()");
    }

}
