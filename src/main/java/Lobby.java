import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple lobby for exactly 2 users
 */
public class Lobby {

    protected static final Collection<Lobby> lobbies = new ArrayList<>();
    static {
        Game.allGamesClasses().forEach(gameClass -> lobbies.add(new Lobby(gameClass)));
    }

    Set<User> users;
    private final Class<? extends Game> gameClass;

    Lobby(Class<? extends Game> gameClass) {
        this.gameClass = gameClass;
        clearUsers();
    }

    public String getGameName() {
        // Is there better way? It is known that Game interface has NAME...
        try {
            return gameClass.getField("NAME").get(null).toString();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<User> getUsers() {
        return users;
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


    public boolean canStartGame() {
        return users.size() == 2;
    }

    /**
     * Returns users and clears the lobby.
     */
    public Set<User> startGame() {
        // TODO: return game with the users from lobby
        Preconditions.checkState(canStartGame());
        var ret = users;
        clearUsers();
        return ret;
    }
}
