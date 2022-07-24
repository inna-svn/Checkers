import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Simple lobby for exactly 2 users
 */
public class Lobby {

    static Map<Class<? extends Game>, Lobby> lobbies;
    static {
        Game.allGamesClasses().forEach(aClass -> lobbies.put(aClass, new Lobby()));
    }

    Set<User> users;

    public Lobby() {
        clearUsers();
    }

    public static Lobby forGame(Class<? extends Game> aClass) {
        return lobbies.get(aClass);
    }

    public static void forEach(BiConsumer<? super Class<? extends Game>, ? super Lobby> cb) {
        lobbies.forEach(cb);
    }

    private void clearUsers() {
        // Not doing users.clear() because the users can be returned by startGame()
        users = new HashSet<>();
    }

    public void addUser(User user) {
        Preconditions.checkState(!canStartGame(), "Adding extra User to a game that could already be started");
        users.add(user);
    }

    public void removeUser(User user) {
        Preconditions.checkArgument(users.contains(user), "Provided User is not in Lobby");
        users.remove(user);
    }

    public Set<User> getUsers() {
        return users;
    }

    public boolean canStartGame() {
        return users.size() == 2;
    }

    /**
     * Returns users and clears the lobby.
     */
    public Set<User> startGame() {
        Preconditions.checkState(canStartGame());
        var ret = users;
        clearUsers();
        return ret;
    }
}
