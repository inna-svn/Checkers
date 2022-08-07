import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class User {

    Map<Class<? extends Game>, UserGameScore> scores = new HashMap<>(); // TODO: Load from DB?
    Game activeGame = null;
    Map<Class<? extends Game>, Lobby> inLobbies = new HashMap<>();

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
        return new User();
    }

    static User signIn(@NotNull String username, @NotNull String password) throws SignInError {
//        throw new SignInError("User not found or password does not match");
        var user = new User();
        user.joinTheOnlyLobby();
        return user;
    }

    void joinLobby(Class<? extends Game> aClass) {
        Lobby.forGame(aClass).addUser(this);
        // TODO: Also update inLobbies
    }

    void joinTheOnlyLobby() {
        // Auto-join Lobby if there is only one
//        Preconditions.checkState(???.size() == 1, "Change here when supporting more than one Game");
    }

    void abandonActiveGame() {
        if(activeGame != null) {
            activeGame.abandon(this);
            activeGame = null;
        }
    }

    void signOut() {
        inLobbies.forEach((aClass, lobby) -> lobby.removeUser(this));
        abandonActiveGame();
    }

    UserGameScore scoreForGame(Class<? extends Game> gameClass, Game.Outcome outcome) {
        // TODO: Check in database first
        return scores.computeIfAbsent(gameClass, c -> new UserGameScore(this, gameClass, 0, 0, 0.0F));
    }

}
