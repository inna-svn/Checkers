import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;


public class CheckersGame implements Game {

    static public final String NAME = "Checkers";

    User activeUser, inactiveUser;
    User blackUser , whiteUser;
    Board board = new Board();

    @Override
    public void start(@NotNull User user1, @NotNull User user2) {
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
    }

    @Override
    public User getActiveUser() {
        return activeUser;
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

        //to do CHECK IF GAME ENDED
        User winner = getWinner();
        if (winner != null) {
            User temp = this.activeUser;
            this.activeUser = this.inactiveUser;
            inactiveUser = temp;
        }
        else {
         //   getWinner();

        }
    }

    public Board getBoard() {
        return this.board;
    }

    public void PrintBoard() {
        for (int row = 0; row < 8; row++) {
            System.out.println("\n----------------");
            System.out.print("|");
            for (int col = 0; col < 8; col++) {
                Piece currPiece = this.board.getPiece(new Location(row, col));
                // Check if current location is a game piece
                if (currPiece != null) {
                    if (currPiece.getColor() == Piece.Color.BLACK) {
                        System.out.print("B");
                    } else {
                        System.out.print("W");
                    }
                } else {
                    System.out.print(" ");
                }
                System.out.print("|");
            }
        }

        System.out.println("");
    }

    Stream<Piece> allPieces() {
        return Location.stream().map(location -> this.board.getPiece(location)).filter(Objects::nonNull);
    }

   /* public boolean isGameEnded() {
        boolean doesWhiteHavePieces = allPieces().anyMatch(piece -> piece.getColor() == Piece.Color.WHITE);
        boolean doesBlackHavePieces = allPieces().anyMatch(piece -> piece.getColor() == Piece.Color.BLACK);

        Map<Piece, List<Move>> currMapp = listPossibleMoves();
        return (!doesBlackHavePieces || !doesWhiteHavePieces || !doesBlackHaveMoves(currMapp) || !doesWhiteHaveMoves(currMapp));
    }*/
    public User getWinner() {
        boolean doesWhiteHavePieces = allPieces().anyMatch(piece -> piece.getColor() == Piece.Color.WHITE);
        boolean doesBlackHavePieces = allPieces().anyMatch(piece -> piece.getColor() == Piece.Color.BLACK);

        Map<Piece, List<Move>> currMapp = listPossibleMoves();
        if (!doesBlackHavePieces ||  !doesBlackHaveMoves(currMapp) ) {
            Status status = Status.FINISHED;
            return whiteUser;
        }
        else if (!doesWhiteHavePieces ||  !doesWhiteHaveMoves(currMapp) ) {
            Status status = Status.FINISHED;
            return blackUser;
        }
        return null;

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