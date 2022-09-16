import com.google.common.base.Preconditions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Named
@ApplicationScoped
public class GameEndpoint {
    // https://jakarta.ee/specifications/faces/4.0/apidocs/jakarta/faces/push/push
    @Inject
    @Push(channel = "game")
    private PushContext gamePushContext;

    Map<Game, String> gamesToIds = new HashMap<>();
    Map<String, Game> idsToGames = new HashMap<>();

    public static final class BoardLocation {

        public final Location location;
        public final Piece piece;
        public final List<Move> moves;

        BoardLocation(Location location, Piece piece, List<Move> moves) {
            this.location = location;
            this.piece = piece;
            this.moves = moves;
        }

        public Location getLocation() {
            return location;
        }

        public Piece getPiece() {
            return piece;
        }

        public List<Move> getMoves() {
            return moves;
        }
    }

    synchronized public String idForGame(Game game) {
        gamesToIds.computeIfAbsent(game, g -> UUID.randomUUID().toString());
        String id = gamesToIds.get(game);
        idsToGames.put(id, game);
        return id;
    }

    synchronized public Game gameForId(@NotNull String id) {
        Preconditions.checkNotNull(id);
        Game game = idsToGames.get(id);
        Preconditions.checkNotNull(game);
        return game;
    }

    public Game getGame() {
        var gameId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        return gameForId(gameId);
    }

    public List<List<BoardLocation>> getBoardLocations(User perspectiveOfUser) {
        List<List<BoardLocation>> rows = new ArrayList<>(Board.SIZE);
        List<BoardLocation> rowPieces;

        Board board = getGame().getBoard();

        for (int row = 0; row < Board.SIZE; row++) {
            rowPieces = new ArrayList<>(Board.SIZE);
            for (int col = 0; col < Board.SIZE; col++) {
                Location location = new Location(row, col); // XXX: inverse row & col
                Piece piece = board.getPiece(location);
                List<Move> moves;
                if(perspectiveOfUser == getGame().getActiveUser() && piece != null) {
                    moves = piece.listPossibleMoves();
                } else {
                    moves = Collections.emptyList();
                }
                rowPieces.add(new BoardLocation(location, piece, moves)); // Optimization to do: only for active user
            }
            rows.add(rowPieces);
        }

        // Rotate the board representation for the second player
        if(getGame().getBlackUser().equals(perspectiveOfUser)) {
            Collections.reverse(rows);
            rows.forEach(Collections::reverse);
        }

        return rows;
    }

}
