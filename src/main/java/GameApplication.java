import com.google.common.base.Preconditions;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@ApplicationScoped
@ManagedBean
@Named
public class GameApplication {

    // Very not sure
    public Game startGame(@NotNull Lobby lobby, Game.StartType startType) {
        Preconditions.checkState(lobby.canStartGame());
        var users = lobby.startGame();
        users.forEach(u -> u.partLobby(lobby));
        var gameClass = lobby.getGameClass();
        Game game;
        try {
            game = gameClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        var userList = new ArrayList<>(users); // Note: unknown order
        game.start(userList.get(0), userList.get(1), startType);

        return game;
    }

}
