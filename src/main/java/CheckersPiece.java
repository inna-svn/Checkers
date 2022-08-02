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

        // Check right movement
        if (this.location.getY() < 7 && isRowValid(1) && isNextRightCellEmpty()){
            moves.add(new Move(this.location, new Location(this.location.getX() + colorMod, this.location.getY() + 1), null));
        }
        else if (this.location.getY() < 6 && isRowValid(2) && !isNextRightCellEmpty() && isNextNextRightCellEmpty() &&
                this.color != this.board.getPiece(new Location(this.location.getX() + colorMod, this.location.getY() + 1)).getColor()){
            moves.add(new Move(this.location, new Location(this.location.getX() + colorMod * 2, this.location.getY() + 2),
                      new Location(this.location.getX() + colorMod, this.location.getY() + 1)));
        }
        // Check left movement
        if (this.location.getY() > 0 && isRowValid(1) && isNextLeftCellEmpty()){
            moves.add(new Move(this.location, new Location(this.location.getX() + colorMod, this.location.getY() - 1), null));
        }
        else if (this.location.getY() > 1 && isRowValid(2) && !isNextLeftCellEmpty() && isNextNextLeftCellEmpty() &&
                this.color != this.board.getPiece(new Location(this.location.getX() + colorMod, this.location.getY() - 1)).getColor()){
            moves.add(new Move(this.location, new Location(this.location.getX() + colorMod * 2, this.location.getY() + 2),
                      new Location(this.location.getX() + colorMod, this.location.getY() - 1)));
        }

        return moves;
    }

    private boolean isRowValid(int rowDelta){
        if (this.color == Color.WHITE && this.location.getX() <= 7 - rowDelta){
            return true;
        }
        else if (this.color == Color.BLACK && this.location.getX() >= 0 + rowDelta){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isNextRightCellEmpty(){
        int colorMod = this.color == Color.WHITE ? 1 : -1;
        if (this.board.getPiece(new Location(location.getX() + colorMod, this.location.getY() + 1)) == null){
            return true;
        }
        else { return false; }
    }

    private boolean isNextLeftCellEmpty(){
        int colorMod = this.color == Color.WHITE ? 1 : -1;
        if (this.board.getPiece(new Location(location.getX() + colorMod, this.location.getY() - 1)) == null){
            return true;
        }
        else{ return false; }
    }

    private boolean isNextNextRightCellEmpty(){
        int colorMod = this.color == Color.WHITE ? 2 : -2;
        if (this.board.getPiece(new Location(location.getX() + colorMod, this.location.getY() + 2)) == null){
            return true;
        }
        else { return false; }
    }

    private boolean isNextNextLeftCellEmpty(){
        int colorMod = this.color == Color.WHITE ? 2 : -2;
        if (this.board.getPiece(new Location(location.getX() + colorMod, this.location.getY() - 2)) == null){
            return true;
        }
        else{ return false; }
    }
}
