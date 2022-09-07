import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class User {
    Map<Class<? extends Game>, UserGameScore> scores = new HashMap<>(); // TODO: Load from DB?
    Game activeGame = null;
    Set<Lobby> lobbies = new HashSet<>();
    private int id;
    private final String username;

    static User testUser1 = new User(55, "test1");
    static User testUser2 = new User(56, "test2");

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

    static void signUp(@NotNull String username, @NotNull String password) throws SignUpError {
//        throw new SignUpError("Password is too short");
        // TODO: Validation

        //create user in DB only
        Database.getDatabase().createUser(username, password);

    }

    static User signIn(@NotNull String username, @NotNull String password) throws SignInError {
        // Lookup the User in the DB

        User user;

        user = Database.getDatabase().userSignIn(username, password);

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

    UserGameScore scoreForGame(Class<? extends Game> gameClass) {
        // TODO: Check in database first, if exists in DB - return that

        return scores.computeIfAbsent(gameClass, c -> {
            var score = new UserGameScore(this, gameClass, 0, 0, 0.0F);
            // TODO: Save score

            Database.getDatabase().createScore(this, gameClass.getName(), 0,0,0.0F);

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
