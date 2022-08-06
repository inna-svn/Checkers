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
        this.board.setPiece(location, this);
        this.color = pieceColor;
    }

    public Color getColor() { return this.color; }

    @Override
    public List<Move> listPossibleMoves() {
        List<Move> moves = new ArrayList<Move>();
        int colorMod = this.color == Color.WHITE ? 1 : -1;

        // Check basic right movement
        if (this.location.y() < 7 && isRowValid(1) && isNextRightCellEmpty()){
            moves.add(new Move(this.location, new Location(this.location.x() + colorMod, this.location.y() + 1), null));
        }

        // Check basic left movement
        if (this.location.y() > 0 && isRowValid(1) && isNextLeftCellEmpty()){
            moves.add(new Move(this.location, new Location(this.location.x() + colorMod, this.location.y() - 1), null));
        }

        moves = listPossibleCaptures(colorMod, moves, this.location, new ArrayList<Location>());
        return moves;
    }

    private List<Move> listPossibleCaptures(int colorMod, List<Move> moves, Location location, List<Location> intermediates){
        if (canCaptureRight(colorMod, location)) {
            Location captureLocation = new Location(location.x() + colorMod * 2, location.y() + 2);
            intermediates.add(new Location(location.x() + colorMod, location.y() + 1));
            moves.add(new Move(location, captureLocation, intermediates));
            moves = listPossibleCaptures(colorMod, moves, captureLocation, intermediates);
        }

        if (canCaptureLeft(colorMod, location))
        {
            Location captureLocation = new Location(location.x() + colorMod * 2, location.y() - 2);
            intermediates.add(new Location(location.x() + colorMod, location.y() - 1));
            moves.add(new Move(location, captureLocation, intermediates));
            moves = listPossibleCaptures(colorMod, moves, captureLocation, intermediates);
        }

        return moves;
    }

    private boolean canCaptureRight(int colorMod, Location location){
        return (location.y() < 6 && isRowValid(2) && !isNextRightCellEmpty() && isNextNextRightCellEmpty() &&
                this.color != this.board.getPiece(new Location(location.x() + colorMod, location.y() + 1)).getColor());
    }

    private boolean canCaptureLeft(int colorMod, Location location){
        return (location.y() > 1 && isRowValid(2) && !isNextLeftCellEmpty() && isNextNextLeftCellEmpty() &&
                this.color != this.board.getPiece(new Location(location.x() + colorMod, location.y() - 1)).getColor());
    }

    private boolean isRowValid(int rowDelta){
        return (this.color == Color.WHITE && this.location.x() <= 7 - rowDelta) ||
               (this.color == Color.BLACK && this.location.x() >= 0 + rowDelta);
    }

    private boolean isNextRightCellEmpty(){
        int colorMod = this.color == Color.WHITE ? 1 : -1;
        return this.board.getPiece(new Location(location.x() + colorMod, this.location.y() + 1)) == null;
    }

    private boolean isNextLeftCellEmpty(){
        int colorMod = this.color == Color.WHITE ? 1 : -1;
        return this.board.getPiece(new Location(location.x() + colorMod, this.location.y() - 1)) == null;
    }

    private boolean isNextNextRightCellEmpty(){
        int colorMod = this.color == Color.WHITE ? 2 : -2;
        return this.board.getPiece(new Location(location.x() + colorMod, this.location.y() + 2)) == null;
    }

    private boolean isNextNextLeftCellEmpty(){
        int colorMod = this.color == Color.WHITE ? 2 : -2;
        return this.board.getPiece(new Location(location.x() + colorMod, this.location.y() - 2)) == null;
    }
}
