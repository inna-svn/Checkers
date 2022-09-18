import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CheckersKingPiece extends CheckersPiece {
    public CheckersKingPiece(@NotNull Board board, @NotNull Color pieceColor, @NotNull Location location) {
        super(board, pieceColor, location);
        resourceName ="King";
    }

    @Override
    public List<Move> listPossibleMoves() {
        List<Move> moves = new ArrayList<Move>();
        int colorMod = this.color == Color.WHITE ? 1 : -1;

        // Check down right
        if (location.x() != 7 && location.y() != 7 && isCellEmpty(this.location, 1, 1)) {
            moves.add(new Move(this.location, new Location(this.location.x() + 1, this.location.y() + 1), null));
        }
        if (location.x() != 7 && location.y() != 0 && isCellEmpty(this.location, 1, -1)) {
            moves.add(new Move(this.location, new Location(this.location.x() + 1, this.location.y() - 1), null));
        }
        if (location.x() != 0 && location.y() != 7 && isCellEmpty(this.location, -1, 1)) {
            moves.add(new Move(this.location, new Location(this.location.x() - 1, this.location.y() + 1), null));
        }
        if (location.x() != 0 && location.y() != 0 && isCellEmpty(this.location, -1, -1)) {
            moves.add(new Move(this.location, new Location(this.location.x() - 1, this.location.y() - 1), null));
        }

        moves = listPossibleCaptures(moves, this.location, new ArrayList<Location>(), new ArrayList<Location>());

        return moves;
    }

    private List<Move> listPossibleCaptures(List<Move> moves, Location location, List<Location> intermediates, List<Location> capturedPieces) {
        if (canCaptureUpRight(location, capturedPieces)) {
            Location captureLocation = new Location(location.x() - 2, location.y() + 2);
            intermediates.add(captureLocation);
            capturedPieces.add(new Location(location.x() - 1, location.y() + 1));
            moves.add(new Move(location, captureLocation, intermediates));
            moves = listPossibleCaptures(moves, captureLocation, intermediates, capturedPieces);
        }

        if (canCaptureUpLeft(location, capturedPieces)) {

            Location captureLocation = new Location(location.x() - 2, location.y() - 2);
            intermediates.add(captureLocation);
            capturedPieces.add(new Location(location.x() - 1, location.y() - 1));
            moves.add(new Move(location, captureLocation, intermediates));
            moves = listPossibleCaptures(moves, captureLocation, intermediates, capturedPieces);
        }

        if (canCaptureDownRight(location, capturedPieces)) {
            Location captureLocation = new Location(location.x() + 2, location.y() + 2);
            intermediates.add(captureLocation);
            capturedPieces.add(new Location(location.x() + 1, location.y() + 1));
            moves.add(new Move(location, captureLocation, intermediates));
            moves = listPossibleCaptures(moves, captureLocation, intermediates, capturedPieces);
        }

        if (canCaptureDownLeft(location, capturedPieces)) {
            Location captureLocation = new Location(location.x() + 2, location.y() - 2);
            intermediates.add(captureLocation);
            capturedPieces.add(new Location(location.x() + 1, location.y() - 1));
            moves.add(new Move(location, captureLocation, intermediates));
            moves = listPossibleCaptures(moves, captureLocation, intermediates, capturedPieces);
        }

        return moves;
    }

    private boolean canCaptureUpRight(Location currLocation, List<Location> capturedPieces) {
        if (!(currLocation.y() < 6 && currLocation.x() > 1)) {
            return false;
        }
        ;
        Location captureLocation = new Location(currLocation.x() - 1, currLocation.y() + 1);

        return (!capturedPieces.contains(captureLocation) &&
                !isCellEmpty(currLocation, -1, 1) &&
                this.color != this.board.getPiece(captureLocation).getColor() &&
                isCellEmpty(currLocation, -2, 2));
    }

    private boolean canCaptureUpLeft(Location currLocation, List<Location> capturedPieces) {
        if (!(currLocation.y() > 1 && currLocation.x() > 1)) {
            return false;
        }
        ;
        Location captureLocation = new Location(currLocation.x() - 1, currLocation.y() - 1);

        return (!capturedPieces.contains(captureLocation) &&
                !isCellEmpty(currLocation, -1, -1) &&
                this.color != this.board.getPiece(captureLocation).getColor() &&
                isCellEmpty(currLocation, -2, -2));
    }

    private boolean canCaptureDownRight(Location currLocation, List<Location> capturedPieces) {
        if (!(currLocation.y() < 6 && currLocation.x() < 6)) {
            return false;
        }
        ;
        Location captureLocation = new Location(currLocation.x() + 1, currLocation.y() + 1);

        return (!capturedPieces.contains(captureLocation) &&
                !isCellEmpty(currLocation, 1, 1) &&
                this.color != this.board.getPiece(captureLocation).getColor() &&
                isCellEmpty(currLocation, 2, 2));
    }

    private boolean canCaptureDownLeft(Location currLocation, List<Location> capturedPieces) {
        if (!(currLocation.y() > 1 && currLocation.x() < 6)) {
            return false;
        }
        ;
        Location captureLocation = new Location(currLocation.x() + 1, currLocation.y() - 1);

        return (!capturedPieces.contains(captureLocation) &&
                !isCellEmpty(currLocation, 1, -1) &&
                this.color != this.board.getPiece(captureLocation).getColor() &&
                isCellEmpty(currLocation, 2, -2));
    }

    private boolean isCellEmpty(Location location, int directionX, int directionY) {
        return this.board.getPiece(new Location(location.x() + directionX, location.y() + directionY)) == null;
    }


}