import com.google.common.base.Preconditions;
import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@ManagedBean
@Named
public class GameApplication {

    // Very not sure
    void startGame(Class<? extends Game> gameClass, Lobby lobby) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Preconditions.checkState(lobby.canStartGame());
        var users = lobby.startGame();
        Game game = gameClass.getConstructor().newInstance();
        var userList = new ArrayList<>(users); // Note: unknown order
        game.start(userList.get(0), userList.get(1));
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
