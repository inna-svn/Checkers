import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class User {

    Map<Class<? extends Game>, UserGameScore> scores = new HashMap<>(); // TODO: Load from DB?
    Game activeGame = null;
    Set<Lobby> lobbies = new HashSet<>();
    private final String username;

    static User testUser1 = new User("test1");
    static User testUser2 = new User("test2");

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    static class SignUpError extends Exception {
        public SignUpError(String message) {
            super(message);
        }
    }

    static class SignInError extends Exception {
        public SignInError(String message) {
            super(message);
        }
    }

    static User signUp(@NotNull String username, @NotNull String password) throws SignUpError {
//        throw new SignUpError("User with the given username already exists");
//        throw new SignUpError("Password is too short");
        // TODO: Also log the user in
        // TODO: Store to DB
        String addUser = "INSERT INTO users VALUES(" + "'" + username + "'" + "," + "'" + password + "')";
        //db.execution(addUser);
        return new User(username);
    }

    static User signIn(@NotNull String username, @NotNull String password) throws SignInError {
        User user = null;
        if (username.equals("test1") && password.equals("test1")) {
            user = testUser1;
        }
        if (username.equals("test2") && password.equals("test2")) {
            user = testUser2;
        }
        // TODO: Lookup the User in the DB
        if (user == null) {
            throw new SignInError("User not found or password does not match");
        }
        return user;
    }

    void joinLobby(@NotNull Lobby lobby) {
        lobby.addUser(this);
        lobbies.add(lobby);
    }

    void abandonActiveGame() {
        if (activeGame != null) {
            activeGame.abandon(this);
            activeGame = null;
        }
    }

    void signOut() {
        lobbies.forEach((lobby) -> lobby.removeUser(this));
        abandonActiveGame();
    }

    UserGameScore scoreForGame(Class<? extends Game> gameClass, Game.Outcome outcome) {
        // TODO: Check in database first
        return scores.computeIfAbsent(gameClass, c -> new UserGameScore(this, gameClass, 0, 0, 0.0F));
    }

    /**
     * @return all lobbies that the user can join, there are no restrictions currently
     */
    public Collection<Lobby> getAvailableLobbies() {
        return Lobby.lobbies;
    }

    public Lobby getLobby() {
        Preconditions.checkState(lobbies.size() <= 1, "Programming error. Update when multiple lobbies are supported");
        return lobbies.stream().findFirst().orElse(null);
    }

}
