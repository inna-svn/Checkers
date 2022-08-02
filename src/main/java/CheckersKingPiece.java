import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CheckersKingPiece extends CheckersPiece {
    public CheckersKingPiece(@NotNull Board board, @NotNull Color pieceColor,@NotNull Location location) {
        super(board, pieceColor,location);
    }

    @Override
    public List<Move> listPossibleMoves() {
        List<Move> moves = new ArrayList<Move>();


        if (isNextCellEmpty(1, 1)){
            moves.add(new Move(this.location, new Location(this.location.getX() + 1, this.location.getY() + 1), null));
        }
        else if (isAbleToCapture(2, 2)){
            moves.add(new Move(this.location, new Location(this.location.getX() + 2, this.location.getY() + 2),
                      new Location(this.location.getX() + 1, this.location.getY() + 1)));
        }

        if (isNextCellEmpty(1, -1)){
            moves.add(new Move(this.location, new Location(this.location.getX() + 1, this.location.getY() - 1), null));
        }
        else if (isAbleToCapture(2, -2)){
            moves.add(new Move(this.location, new Location(this.location.getX() + 2, this.location.getY() - 2),
                      new Location(this.location.getX() + 1, this.location.getY() - 1)));
        }

        if (isNextCellEmpty(-1, 1)){
            moves.add(new Move(this.location, new Location(this.location.getX() - 1, this.location.getY() + 1), null));
        }
        else if (isAbleToCapture(-2, 2)){
            moves.add(new Move(this.location, new Location(this.location.getX() - 2, this.location.getY() + 2),
                      new Location(this.location.getX() - 1, this.location.getY() + 1)));
        }

        if (isNextCellEmpty(-1, -1)){
            moves.add(new Move(this.location, new Location(this.location.getX() - 1, this.location.getY() - 1), null));
        }
        else if (isAbleToCapture(-2, -2)){
            moves.add(new Move(this.location, new Location(this.location.getX() + 2, this.location.getY() + 2),
                      new Location(this.location.getX() - 1, this.location.getY() - 1)));
        }

        return super.listPossibleMoves();
    }

    private boolean isNextCellEmpty(int directionX, int directionY){
        if (this.board.getPiece(new Location(location.getX() + directionX, this.location.getY() + directionY)) == null){
            return true;
        }
        else { return false; }
    }

    private boolean isAbleToCapture(int directionX, int directionY){
        //
        if (((directionY < 0 && this.location.getY() > 1) || (directionY > 0 && this.location.getY() < 6)) &&
            ((directionX < 0 && this.location.getX() > 1) || (directionX > 0 && this.location.getX() < 6)) &&
            (!isNextCellEmpty(directionX/2, directionY/2)) && (isNextCellEmpty(directionX, directionY)) &&
            ((directionY < 0 && this.location.getY() > 1) || (directionY > 0 && this.location.getY() < 6)) &&
            ((directionX < 0 && this.location.getX() > 1) || (directionX > 0 && this.location.getX() < 6)) &&
            (this.board.getPiece(new Location(this.location.getX() + directionX / 2, this.location.getY() + directionY / 2))).getColor() != this.getColor() ){
            return true;
            //last row checks if the piece that i can eat is enemy colored
            // 1st condition checks that the eat action wont leave the board on cols
            // 2nd conditions checks that eat action wont leave the board on rows
            // 3rd condition: checks if there is a cell next to me (not empty)
            // 4nd: checks if the cell after it is empty so i can eat
            // 5th: checks if the cell that i can eat is enemy color
        }
        else { return false; }
    }
}
