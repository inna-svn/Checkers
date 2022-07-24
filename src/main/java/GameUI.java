import com.google.common.base.Preconditions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameUI {

    void signUp(String username, String password) throws User.SignUpError {
        User user = User.signUp(username, password);
        // TODO: Check if any lobby has enough players to start
        //       The lobby where u joined might be ready.
        //       Then startGame()
    }

    void signIn(String username, String password) throws User.SignInError {
        // Note: auto-joins lobby
        User user = User.signIn(username, password);
        // TODO: Check if any lobby has enough players to start
        //       The lobby where u joined might be ready.
        //       Then startGame()
    }

    void signOut(User user) {
        user.signOut();
    }

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
