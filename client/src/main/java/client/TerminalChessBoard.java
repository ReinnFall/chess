package client;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class TerminalChessBoard{

    public void printChessBoard(ChessGame game,ChessGame.TeamColor color){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        char[] columns;
        if (color == ChessGame.TeamColor.WHITE){
            columns = new char[]{'a','b','c','d','e','f','g','h'};
        }else{
            columns = new char[]{'h','g','f','e','d','c','b','a'};
        }

//        out.print(SET_BG_COLOR_LIGHT_GREY);
//        out.print("a b c d e f g h");
//        out.print(RESET_BG_COLOR);
//        out.println();

        out.print(SET_BG_COLOR_LIGHT_GREY);
        for (char column : columns){
            out.print(" " + column + " ");
        }
        out.print(RESET_BG_COLOR);
        out.println();

    }
}