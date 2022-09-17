import com.google.common.base.Preconditions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class GameEndpoint {
    // https://jakarta.ee/specifications/faces/4.0/apidocs/jakarta/faces/push/push
    @Inject
    @Push(channel = "game")
    private PushContext gamePushContext;

    record UserInGame(User user, Game game) {}

    Map<Game, String> gamesToIds = new HashMap<>();
    Map<String, Game> idsToGames = new HashMap<>();
    Map<UserInGame, Piece> selectedPieces = new HashMap<>();

    public static final class BoardLocation {

        private final Location location;
        private final Piece piece;
        private final boolean isSelected;
        private final boolean hasMoves;
        private final boolean isMoveTarget;

        BoardLocation(Location location, Piece piece, boolean isSelected, boolean hasMoves, boolean isMoveTarget) {
            this.location = location;
            this.piece = piece;
            this.isSelected = isSelected;
            this.hasMoves = hasMoves;
            this.isMoveTarget = isMoveTarget;
        }

        public Location getLocation() {return location;}
        public Piece getPiece() {return piece;}
        public boolean getIsSelected() {return isSelected;}
        public boolean getHasMoves() {return hasMoves;}
        public boolean getIsMoveTarget() {return isMoveTarget;}
    }

    synchronized public String idForGame(@NotNull Game game) {
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

    public List<List<BoardLocation>> getBoardState(@NotNull User perspectiveOfUser) {
        List<List<BoardLocation>> rows = new ArrayList<>(Board.SIZE);
        List<BoardLocation> rowPieces;

        Board board = getGame().getBoard();
        System.err.println("getBoardLocations");

        var selectedPiece = selectedPieces.get(new UserInGame(perspectiveOfUser, getGame()));
        Set<Location> moveTargetLocations;
        if(selectedPiece != null) {
            moveTargetLocations = selectedPiece.listPossibleMoves().stream().map(Move::end).collect(Collectors.toSet());
        } else {
            moveTargetLocations = new HashSet<>();
        }

        for (int row = 0; row < Board.SIZE; row++) {
            rowPieces = new ArrayList<>(Board.SIZE);
            for (int col = 0; col < Board.SIZE; col++) {
                Location location = new Location(row, col); // XXX: inverse row & col
                Piece piece = board.getPiece(location);
                boolean hasMoves = false;
                if(getGame().userCanMovePiece(perspectiveOfUser, piece)) {
                    hasMoves = piece.listPossibleMoves().size() > 0;
                }
                var isMoveTarget = moveTargetLocations.contains(location);
                var isSelected = piece != null && piece.equals(selectedPiece);
                rowPieces.add(new BoardLocation(location, piece, isSelected, hasMoves, isMoveTarget)); // Optimization to do: only for active user
            }
            rows.add(rowPieces);
        }

        // Rotate the board representation for the second player
        if(getGame().getWhiteUser().equals(perspectiveOfUser)) {
            Collections.reverse(rows);
            rows.forEach(Collections::reverse);
        }

        return rows;
    }

    public void selectPiece(@NotNull User user, @NotNull Piece piece) {
        var uig = new UserInGame(user, getGame());
        if(Objects.equals(piece, selectedPieces.get(uig))) {
            // Already selected -> unselect
            selectedPieces.remove(uig);
        } else {
            // Nothing was select or another piece was selected -> select
            selectedPieces.put(uig, piece);
        }
    }

}
