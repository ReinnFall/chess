package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static ui.EscapeSequences.*;

public class TerminalChessBoard{

    public void printChessBoard(ChessGame game, ChessGame.TeamColor color, ChessPosition positionofPiece, Set<ChessPosition> highlightedPositions)  {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessBoard currentBoard = game.getBoard();
        char[] columns;
        int[] rows;

        if (color == ChessGame.TeamColor.WHITE){
            columns = new char[]{'a','b','c','d','e','f','g','h'};
        }else{
            columns = new char[]{'h','g','f','e','d','c','b','a'};
        }

        if (color == ChessGame.TeamColor.WHITE){
            rows = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        }else{
            rows = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        }
        //Top column letters
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print("   ");
        for (char column : columns){
            out.print(" " + column + " ");
        }
        out.print("   ");
        out.print(RESET_BG_COLOR);
        out.println();

        //Piece positions
        for (int row: rows){
            //Left row numbers
            out.print(SET_BG_COLOR_LIGHT_GREY  + " " + row + " " + RESET_BG_COLOR);
            for(char column: columns){
                int columnNumber = columnIndexer(column);
                ChessPosition currentPosition = new ChessPosition(row,columnNumber);
                ChessPiece currentPiece = currentBoard.getPiece(currentPosition);

                //Chess Background Color
                boolean isHighlighted = false;
                if(highlightedPositions != null){
                    if(highlightedPositions.contains(currentPosition)){
                        isHighlighted = true;
                    }
                }
                boolean isSelectedPiece = false;
                if(positionofPiece != null){
                    if(currentPosition == positionofPiece){
                        isSelectedPiece = true;
                    }
                }


                int sumPosition = row + column;
                boolean isEven = (sumPosition % 2 == 1);
                boolean isLight;

                if(isEven){
                    isLight = true;
                } else{
                    isLight = false;
                }

                String backgroundColor;

                if(isLight){
                    backgroundColor = SET_BG_COLOR_BLUE;
                } else {
                    backgroundColor = SET_BG_COLOR_BLACK;
                }
                if(isHighlighted){
                    backgroundColor = SET_BG_COLOR_GREEN;
                }
                if(isSelectedPiece){
                    backgroundColor = SET_BG_COLOR_GREEN;
                }
                out.print(backgroundColor);

                //Actually print out piece
                if(currentPiece == null){
                    out.print(EMPTY);
                } else{
                    out.print(convertToSymbol(currentPiece));
                }
            }
            //Right row numbers
            out.print(SET_BG_COLOR_LIGHT_GREY + " " + row + " " + RESET_BG_COLOR);
            out.println();
        }
        //Botton column letters
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print("   ");
        for (char column : columns){
            out.print(" " + column + " ");
        }
        out.print("   ");
        out.print(RESET_BG_COLOR);
        out.println();
    }
    private int columnIndexer(char columnLetter)  {
        int columnNumber = 0;

        switch(columnLetter){
            case 'a':
                columnNumber = 1;
                break;
            case 'b':
                columnNumber = 2;
                break;
            case 'c':
                columnNumber = 3;
                break;
            case 'd':
                columnNumber = 4;
                break;
            case 'e':
                columnNumber = 5;
                break;
            case 'f':
                columnNumber = 6;
                break;
            case 'g':
                columnNumber = 7;
                break;
            case 'h':
                columnNumber = 8;
                break;
            default:
                break;
        }
        return columnNumber;
    }
    private String convertToSymbol(ChessPiece piece){
        String pieceType = "";
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (piece.getPieceType()) {
                case KING:
                    pieceType = WHITE_KING;
                    break;
                case QUEEN:
                    pieceType = WHITE_QUEEN;
                    break;
                case ROOK:
                    pieceType = WHITE_ROOK;
                    break;
                case BISHOP:
                    pieceType = WHITE_BISHOP;
                    break;
                case KNIGHT:
                    pieceType = WHITE_KNIGHT;
                    break;
                case PAWN:
                    pieceType = WHITE_PAWN;
                    break;
            }
        } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            switch (piece.getPieceType()) {
                case KING:
                    pieceType = BLACK_KING;
                    break;
                case QUEEN:
                    pieceType = BLACK_QUEEN;
                    break;
                case ROOK:
                    pieceType = BLACK_ROOK;
                    break;
                case BISHOP:
                    pieceType = BLACK_BISHOP;
                    break;
                case KNIGHT:
                    pieceType = BLACK_KNIGHT;
                    break;
                case PAWN:
                    pieceType = BLACK_PAWN;
                    break;
            }
        }
        return pieceType;
    }

}