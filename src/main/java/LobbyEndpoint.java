import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

// https://github.dev/AnghelLeonard/JSF-2.3

@Named
@ApplicationScoped
public class LobbyEndpoint {
    // https://jakarta.ee/specifications/faces/4.0/apidocs/jakarta/faces/push/push
    @Inject
    @Push(channel = "lobby")
    private PushContext lobbyPushContext;

    @Inject
    private GameEndpoint gameEndpoint;

    @NotNull
    private static List<String> getLobbyUserNames(Lobby lobby, Collection<User> users) {
        var userNames = users.stream().map(User::getUsername).sorted().toList();
        return userNames.stream().map(u -> lobby.getGameName() + '_' + u).toList();
    }


    /**
     * Send event to each user in the lobby. The event triggers Ajax reload of lobby users list.
     */
    public void onLobbyUserListChange(Lobby lobby) {
        // TODO: By user ID, not username
        var cmd = new HashMap<String, Object>();
        cmd.put("func", "renderList");
        cmd.put("args", new String[]{});
        lobbyPushContext.send(cmd, getLobbyUserNames(lobby, lobby.getUsers()));
        // TODO: move this code somewhere else, it does not belong to sendLobby()
    }

    public void startGame(Lobby lobby, Game game, Set<User> users) {
        var cmd = new HashMap<String, Object>();
        cmd.put("func", "startGame");
        var id = gameEndpoint.idForGame(game);
        cmd.put("args", new String[]{"game.xhtml?id=" + id});
        lobbyPushContext.send(cmd, getLobbyUserNames(lobby, users));
    }
}
