import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class CheckersPiece implements Piece {

    protected Board board;
    protected Location location;
    protected Color color;

    public CheckersPiece(@NotNull Board board, @NotNull Color pieceColor, @NotNull Location location) {
        this.board = board;
        this.location = location;
        this.color = pieceColor;
    }

    public Color getColor() { return this.color; }

    @Override
    public List<Move> listPossibleMoves() {
        List<Move> moves = new ArrayList<Move>();
        int colorMod = this.color == Color.WHITE ? 1 : -1;

        // Check basic right movement
        if (this.location.y() < 7 && isRowValid(1,location) && isNextRightCellEmpty(location)){
            moves.add(new Move(this.location, new Location(this.location.x() + colorMod, this.location.y() + 1), null));
        }

        // Check basic left movement
        if (this.location.y() > 0 && isRowValid(1,location) && isNextLeftCellEmpty(location)){
            moves.add(new Move(this.location, new Location(this.location.x() + colorMod, this.location.y() - 1), null));
        }

        moves = listPossibleCaptures(colorMod, moves, this.location, new ArrayList<Location>(),this.location);
        return moves;
    }

    private List<Move> listPossibleCaptures(int colorMod, List<Move> moves, Location location, List<Location> intermediates,Location initalLocation){
        if (canCaptureRight(colorMod, location)) {
            Location captureLocation = new Location(location.x() + colorMod * 2, location.y() + 2);
            intermediates.add(captureLocation);

            if (! canCaptureRight(colorMod, captureLocation) && ! canCaptureLeft(colorMod, captureLocation)){
                moves.add(new Move(initalLocation, captureLocation, getCopyOfIntermidiates(intermediates)));
                //intermediates.clear();
                intermediates.remove(captureLocation);
                //System.out.println("x: " + location.x() + ", y: " + location.y() + ", Move: " );
            }
            else {
                moves = listPossibleCaptures(colorMod, moves, captureLocation, intermediates, initalLocation);
                intermediates.remove(captureLocation);
            }
        }

        if (canCaptureLeft(colorMod, location))
        {
            Location captureLocation = new Location(location.x() + colorMod * 2, location.y() - 2);
            intermediates.add(captureLocation);
            if (! canCaptureRight(colorMod, captureLocation) && ! canCaptureLeft(colorMod, captureLocation)) {
                moves.add(new Move(initalLocation, captureLocation, getCopyOfIntermidiates(intermediates)));
                //intermediates.clear();
                intermediates.remove(captureLocation);
                //System.out.println("x: " + location.x() + ", y: " + location.y() + ", Move: " );
            }
            else {
                moves = listPossibleCaptures(colorMod, moves, captureLocation, intermediates, initalLocation);
                intermediates.remove(captureLocation);
            }
        }

        return moves;
    }

    private boolean canCaptureRight(int colorMod, Location location){
        return (location.y() < 6 && isRowValid(2,location) && !isNextRightCellEmpty(location) && isNextNextRightCellEmpty(location) &&
                this.color != this.board.getPiece(new Location(location.x() + colorMod, location.y() + 1)).getColor());
    }

    private boolean canCaptureLeft(int colorMod, Location location){
        return (location.y() > 1 && isRowValid(2,location) && !isNextLeftCellEmpty(location) && isNextNextLeftCellEmpty(location) &&
                this.color != this.board.getPiece(new Location(location.x() + colorMod, location.y() - 1)).getColor());
    }

    private boolean isRowValid(int rowDelta,Location location){
        return (this.color == Color.WHITE && location.x() <= 7 - rowDelta) ||
               (this.color == Color.BLACK && location.x() >= 0 + rowDelta);
    }

    private boolean isNextRightCellEmpty(Location location){
        int colorMod = this.color == Color.WHITE ? 1 : -1;
        return this.board.getPiece(new Location(location.x() + colorMod, location.y() + 1)) == null;
    }

    private boolean isNextLeftCellEmpty(Location location){
        int colorMod = this.color == Color.WHITE ? 1 : -1;
        return this.board.getPiece(new Location(location.x() + colorMod, location.y() - 1)) == null;
    }

    private boolean isNextNextRightCellEmpty(Location location){
        int colorMod = this.color == Color.WHITE ? 2 : -2;
        return this.board.getPiece(new Location(location.x() + colorMod, location.y() + 2)) == null;
    }

    private boolean isNextNextLeftCellEmpty(Location location){
        int colorMod = this.color == Color.WHITE ? 2 : -2;
        return this.board.getPiece(new Location(location.x() + colorMod, location.y() - 2)) == null;
    }

    public List<Location> getCopyOfIntermidiates(List <Location>intermediates){
        List <Location> temp= new ArrayList<>();
        intermediates.forEach(it->temp.add(it));
        return temp;
    }
}
