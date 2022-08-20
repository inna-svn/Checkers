import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User {

    Map<Class<? extends Game>, UserGameScore> scores = new HashMap<>(); // TODO: Load from DB?
    Game activeGame = null;
    Set<Lobby> lobbies = new HashSet<>();
    private String username;
    private int id;
    //private final String username;

    static User testUser1 = new User("test1");
    static User testUser2 = new User("test2");

    public User(String username) {
        this.username = username;
       // this.id = id;

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
//        throw new SignUpError("Password is too short");
        // TODO: Also log the user in
        // TODO: Validation

        try {

            PreparedStatement preparedStmt = Database.getDatabase().getConnection().prepareStatement("INSERT INTO users(userName,password) VALUES(?,?)");
            preparedStmt.setString(1,username);
            preparedStmt.setString(2,password);
            preparedStmt.executeUpdate();
            User.signIn(username,password);
        } catch (SQLException | SignInError sqlException) {
            throw new SignUpError("User with the given username already exists");
        }
        return null;
    }

    static User signIn(@NotNull String username, @NotNull String password) throws SignInError {
        // Lookup the User in the DB
        User user = null;
        try {
            PreparedStatement preparedStmt = Database.getDatabase().getConnection().prepareStatement("SELECT * FROM users WHERE userName=? AND password=? limit 1");
            preparedStmt.setString(1,username);
            preparedStmt.setString(2,password);
            ResultSet u = preparedStmt.executeQuery();
            if(u.next())
                return new User(u.getString("userName"));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
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
