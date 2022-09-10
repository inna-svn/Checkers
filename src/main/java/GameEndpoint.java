import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Named
@ApplicationScoped
public class GameEndpoint {
    // https://jakarta.ee/specifications/faces/4.0/apidocs/jakarta/faces/push/push
    @Inject
    @Push(channel = "game")
    private PushContext gamePushContext;

    Map <Game, String> gamesToIds = new HashMap<>();
    Map <String, Game> idsToGames = new HashMap<>();

    synchronized public String idForGame(Game game) {
        gamesToIds.computeIfAbsent(game, g -> UUID.randomUUID().toString());
        String id = gamesToIds.get(game);
        idsToGames.put(id, game);
        return id;
    }
}
