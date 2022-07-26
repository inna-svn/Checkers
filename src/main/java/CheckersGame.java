import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


public class CheckersGame extends Game {

    static public final String NAME = "Checkers";

    User activeUser, inactiveUser;
    User blackUser, whiteUser;

    // Should be on Game.getName but there is a bug - https://github.com/jakartaee/expression-language/issues/43

    @Override
    public void start(@NotNull User user1, @NotNull User user2, StartType startType) {
        activeUser = user1;
        inactiveUser = user2;
        whiteUser = activeUser;
        blackUser = inactiveUser;

        // Default game pieces
        Piece.Color currColor = Piece.Color.WHITE;

        // Initialize board with game pieces
        for (int row = 0; row < 8; row++) {
            // For rows 0,1,2 color is white, skip row 3-4, then use color 5
            if (row == 3) {
                row = 5;
                currColor = Piece.Color.BLACK;
            }
            // Go over cols and place soldiers
            for (int col = ((row % 2) == 0) ? 1 : 0; col < 8; col = col + 2) {
                // Place a soldier at current location with the right color
                Location currLocation = new Location(row, col);
                board.setPiece(currLocation, new CheckersPiece(board, currColor, currLocation));
            }
        }

        switch (startType) {
            case TEST1 -> presetKing(user1, user2);
            case TEST2 -> presetEndGame(user1, user1);
        }
    }


    public void presetEndGame(@NotNull User user1, @NotNull User user2){
        //remove all white soldiers expect 2,7
        for (int row = 0; row < 3; row++) {
            for (int col = ((row % 2) == 0) ? 1 : 0; col < 8; col = col + 2) {
                if (col != 7 || row != 2) getBoard().removePiece(new Location(row, col));
            }
        }
        //move black soldiers in order to block the white soldier
        makeCommonPresetMoves();
    }

    private void makeCommonPresetMoves() {
        makeMove(activeUser, new Move(new Location(7, 0), new Location(3, 6), null));
        makeMove(activeUser, new Move(new Location(7, 2), new Location(4, 5), null));
        makeMove(activeUser, new Move(new Location(3, 6), new Location(2, 5), null));
        makeMove(activeUser, new Move(new Location(2, 7), new Location(3, 6), null));
    }

    public void presetKing(@NotNull User user1, @NotNull User user2){
        //remove all white soldiers expect 2,7
        for (int row = 0; row < 3; row++) {
            for (int col = ((row % 2) == 0) ? 1 : 0; col < 8; col = col + 2) {
                if (! ((col == 7 && row == 2) || (col == 2 && row == 1))) getBoard().removePiece(new Location(row, col));
            }
        }

        //move black soldiers in order to block the white soldier
        makeCommonPresetMoves();
        makeMove(activeUser, new Move(new Location(7, 4), new Location(3, 2), null));
        makeMove(activeUser, new Move(new Location(2, 5), new Location(1, 6), null));
        makeMove(activeUser, new Move(new Location(3, 6), new Location(4, 7), null));
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }

    @Override
    public User getBlackUser() {
        return blackUser;
    }

    @Override
    public User getWhiteUser() {
        return whiteUser;
    }

    @Override
    public void abandon(User user) {
        status = Status.FINISHED;
    }

    @Override
    public Map<Piece, List<Move>> listPossibleMoves() {
        Preconditions.checkState(status == Status.IN_PROGRESS);
        Map<Piece, List<Move>> currMap = new HashMap<>();

        // Go over all cells to check all available pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece currPiece = this.board.getPiece(new Location(row, col));

                // Check if current location is a game piece
                if (currPiece != null) {
                    currMap.put(currPiece, currPiece.listPossibleMoves());
                }
            }
        }

        return currMap;
    }

    @Override
    public void makeMove(@NotNull User user, @NotNull Move move) {
        // Set piece to end goal
        if (this.board.getPiece(move.start()) instanceof CheckersKingPiece)
            this.board.setKingPiece(move.end(), this.board.getPiece(move.start()));
        else
            this.board.setPiece(move.end(), this.board.getPiece(move.start()));
        // Remove all relevant pieces
        this.board.removePiece(move.start());
        if (move.intermediates() != null) {
            Location eatenPiece = new Location((move.start().x() + move.intermediates().get(0).x()) / 2,
                    (move.start().y() + move.intermediates().get(0).y()) / 2);
            this.board.removePiece(eatenPiece);

            for (int i = 1; i < move.intermediates().size(); i++) {
                eatenPiece = new Location((move.intermediates().get(i).x() + move.intermediates().get(i - 1).x()) / 2,
                        (move.intermediates().get(i).y() + move.intermediates().get(i - 1).y()) / 2);
                this.board.removePiece(eatenPiece);
            }
            //move.intermediates().forEach((currLocation) -> this.board.removePiece(currLocation));
        }
        // Make King Section
        Piece currPiece = this.board.getPiece(move.end());
        if ((currPiece.getColor() == Piece.Color.BLACK && move.end().x() == 0) ||
                (currPiece.getColor() == Piece.Color.WHITE && move.end().x() == 7)) {
            this.board.removePiece(move.end());
            this.board.setKingPiece(move.end(), new CheckersKingPiece(this.board, currPiece.getColor(), move.end()));
        }

        User temp = this.activeUser;
        this.activeUser = this.inactiveUser;
        inactiveUser = temp;
    }

    void processPossibleGameEnd() {
        boolean doesWhiteHavePieces = allPieces().anyMatch(piece -> piece.getColor() == Piece.Color.WHITE);
        boolean doesBlackHavePieces = allPieces().anyMatch(piece -> piece.getColor() == Piece.Color.BLACK);

        Map<Piece, List<Move>> currMapp = listPossibleMoves();
        if (!doesBlackHavePieces || !doesBlackHaveMoves(currMapp)) {
            winner = whiteUser;
            loser = blackUser;
        } else if (!doesWhiteHavePieces || !doesWhiteHaveMoves(currMapp)) {
            winner = blackUser;
            loser = whiteUser;
        }

        if (winner == null) {
            return;
        }
        UserGameScore currentWinnerScore = winner.scoreForGame(CheckersGame.class);
        currentWinnerScore.updateFromGameOutcome(Outcome.WON);
        UserGameScore currentLoserScore = loser.scoreForGame(CheckersGame.class);
        currentLoserScore.updateFromGameOutcome(Outcome.LOST);
        status = Status.FINISHED;
    }

    Stream<Piece> allPieces() {
        return Location.stream().map(location -> this.board.getPiece(location)).filter(Objects::nonNull);
    }

    public boolean doesBlackHaveMoves(Map<Piece, List<Move>> currMapp) {
        for (Map.Entry<Piece, List<Move>> item : currMapp.entrySet()) {
            if (item.getKey() != null && item.getKey().getColor() == Piece.Color.BLACK) {
                if (!item.getValue().isEmpty()) return true;
            }
        }
        return false;
    }

    public boolean doesWhiteHaveMoves(Map<Piece, List<Move>> currMapp) {
        for (Map.Entry<Piece, List<Move>> item : currMapp.entrySet()) {
            if (item.getKey() != null && item.getKey().getColor() == Piece.Color.WHITE) {
                if (!item.getValue().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}