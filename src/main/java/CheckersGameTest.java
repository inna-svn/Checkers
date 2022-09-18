import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;


class CheckersGameTest {
    CheckersGame game;
    User activeUser;
    User inactiveUser;

    @BeforeEach
    @org.junit.jupiter.api.Test
    public void setUp() throws User.SignInError {
        activeUser = User.signIn("inna", "12345");
        inactiveUser = User.signIn("ilya", "12345");
        game = new CheckersGame();
        game.start(activeUser, inactiveUser, Game.StartType.REGULAR);
    }

    @org.junit.jupiter.api.Test
    void start() {
        Piece.Color expectedColor;
        for (int row = 0; row < 8; row++) {
            for (int col = ((row % 2) == 0) ? 1 : 0; col < 8; col = col + 2) {
                if (row == 3) row = 5;
                expectedColor = row > 4 ? Piece.Color.BLACK : Piece.Color.WHITE;
                Assertions.assertEquals(game.getBoard().getPiece(new Location(row, col)).getColor(), expectedColor);
            }
        }
    }

    @org.junit.jupiter.api.Test
    void listPossibleMovesWithSingleCapture() {
        Map<Piece, List<Move>> currMapp;
        game.makeMove(activeUser, new Move(new Location(5, 6), new Location(4, 7), null));
        game.makeMove(activeUser, new Move(new Location(4, 7), new Location(3, 6), null));
        currMapp = game.listPossibleMoves();
        List<Move> moves_1 = currMapp.get(game.getBoard().getPiece(new Location(2, 7)));
        List<Move> moves_2 = currMapp.get(game.getBoard().getPiece(new Location(2, 5)));
        Assertions.assertEquals(moves_1.get(0).start(), new Location(2, 7));
        Assertions.assertEquals(moves_1.get(0).end(), new Location(4, 5));
        Assertions.assertEquals(moves_1.get(0).intermediates().get(0), new Location(4, 5));
        Assertions.assertEquals(moves_2.get(1).start(), new Location(2, 5));
        Assertions.assertEquals(moves_2.get(1).end(), new Location(4, 7));
    }

    @org.junit.jupiter.api.Test
    void listPossibleMovesWithMultipleCapture() {
        Map<Piece, List<Move>> currMapp;
        game.makeMove(activeUser, new Move(new Location(5, 6), new Location(4, 7), null));
        game.makeMove(activeUser, new Move(new Location(4, 7), new Location(3, 6), null));
        game.getBoard().removePiece(new Location(6, 3));
        currMapp = game.listPossibleMoves();
        List<Move> moves = currMapp.get(game.getBoard().getPiece(new Location(2, 7)));
        Assertions.assertEquals(moves.get(0).start(), new Location(2, 7));
        Assertions.assertEquals(moves.get(0).end(), new Location(6, 3));
        Assertions.assertEquals(moves.get(0).intermediates().get(0), new Location(4, 5));
        Assertions.assertEquals(moves.get(0).intermediates().get(1), new Location(6, 3));

    }

    @org.junit.jupiter.api.Test
    void makeMove() {
        Piece sourcePiece = game.getBoard().getPiece(new Location(2, 1));
        game.makeMove(activeUser, new Move(new Location(2, 1), new Location(3, 1), null));
        Assertions.assertNull(game.getBoard().getPiece(new Location(2, 1)));
        Assertions.assertNotNull(game.getBoard().getPiece(new Location(3, 1)));
        Assertions.assertEquals(sourcePiece.getColor(), game.getBoard().getPiece(new Location(3, 1)).getColor());
    }

    @org.junit.jupiter.api.Test
    void doesWhiteHaveMoves() {
        Map<Piece, List<Move>> currMapp;
        currMapp = game.listPossibleMoves();
        Assertions.assertTrue(game.doesWhiteHaveMoves(currMapp));

        //remove all white soldiers expect 2,7
        for (int row = 0; row < 3; row++) {
            for (int col = ((row % 2) == 0) ? 1 : 0; col < 8; col = col + 2) {
                if (col != 7 || row != 2) game.getBoard().removePiece(new Location(row, col));
            }
        }
        currMapp = game.listPossibleMoves();
        Assertions.assertTrue(game.doesWhiteHaveMoves(currMapp));

        //move black soldiers in order to block the white soldier
        game.makeMove(activeUser, new Move(new Location(7, 0), new Location(3, 6), null));
        game.makeMove(activeUser, new Move(new Location(7, 2), new Location(4, 5), null));
        currMapp = game.listPossibleMoves();
        Assertions.assertFalse(game.doesWhiteHaveMoves(currMapp));
    }

    @org.junit.jupiter.api.Test
    void getWinner() {
        activeUser.scoreForGame(CheckersGame.class);
        inactiveUser.scoreForGame(CheckersGame.class);
        doesWhiteHaveMoves();
        Assertions.assertEquals(game.getWinner().getUsername(), "ilya");
    }
}