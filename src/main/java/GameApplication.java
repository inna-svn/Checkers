import com.google.common.base.Preconditions;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@ManagedBean
@Named
public class GameApplication {

    // Very not sure
    public Game startGame(@NotNull Lobby lobby,boolean endPreset,boolean kingPreset) {
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
        if (endPreset)
            game.presetEndGame(userList.get(0), userList.get(1));
        else if (kingPreset)
            game.presetKing(userList.get(0), userList.get(1));
        else
            game.start(userList.get(0), userList.get(1));

        return game;
    }

    Map<Piece, List<Move>> listPossibleMoves(User user, Game game) {
        Preconditions.checkState(game.getActiveUser() == user);
        return game.listPossibleMoves();
    }

    /**
     * The user picked a move in the UI
     */
    void move(User user, Game game, Move move) {
    }

}
