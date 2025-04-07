package client;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class TerminalChessBoard{

    public void printChessBoard(ChessGame game,ChessGame.TeamColor color){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(SET_BG_COLOR_DARK_GREY);
        out.print("a b c d e f g h");

    }
}