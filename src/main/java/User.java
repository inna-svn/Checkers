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
    private final int id;
    private final String username;

    static User testUser1 = new User(1, "test1");
    static User testUser2 = new User(2, "test2");

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
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
        // TODO: Validation
        User newUser;
        try (PreparedStatement preparedStmt = Database.getDatabase().getConnection().prepareStatement("INSERT INTO users(userName,password) VALUES(?,?)")) {
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, password);
            preparedStmt.executeUpdate();
            newUser = new User(preparedStmt.getResultSet().getInt("id"), username);
        } catch (SQLException e) {
            //   e.printStackTrace();
            throw new SignUpError(e.toString());
        }
        try {
            return signIn(username, password);
        } catch (SignInError exception) {
            exception.printStackTrace();
            throw new SignUpError(exception.toString());
        }
    }

    static User signIn(@NotNull String username, @NotNull String password) throws SignInError {
        // Lookup the User in the DB
        try {
            try (PreparedStatement preparedStmt = Database.getDatabase().getConnection().prepareStatement("SELECT * FROM users WHERE userName=? AND password=? limit 1")) {
                preparedStmt.setString(1, username);
                preparedStmt.setString(2, password);
                ResultSet u = preparedStmt.executeQuery();
                if (u.next()) {
                    return new User(u.getInt("id"), u.getString("userName"));
                }
                throw new SignInError("User not found or password does not match");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new SignInError("Database Failure");
        }
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

    UserGameScore scoreForGame(Class<? extends Game> gameClass) {
        // TODO: Check in database first, if exists in DB - return that

        return scores.computeIfAbsent(gameClass, c -> {
            var score = new UserGameScore(this, gameClass, 0, 0, 0.0F);
            // TODO: Save score
            return score;
        });

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
