import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple lobby for exactly 2 users
 */
public class Lobby {
    Set<User> users = new HashSet<>();

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
}
