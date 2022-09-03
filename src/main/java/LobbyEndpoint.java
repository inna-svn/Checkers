import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

// https://github.dev/AnghelLeonard/JSF-2.3

@Named
@ApplicationScoped
public class LobbyEndpoint {
    // https://jakarta.ee/specifications/faces/4.0/apidocs/jakarta/faces/push/push
    @Inject
    @Push(channel = "lobby")
    private PushContext lobbyPushContext;

    /**
     * Send event to each user in the lobby. The event triggers Ajax reload of lobby users list.
     */
    public void sendLobby(Lobby lobby) {
        // TODO: By user ID, not username
        var userNames = lobby.getUsers().stream().map(User::getUsername).sorted().toList();
        var websocketUsernames = userNames.stream().map(u -> lobby.getGameName() + '_' + u).toList();
        lobbyPushContext.send(lobby.getGameName() + "_update", websocketUsernames);
//        lobbyPushContext.send("x");
    }
}
