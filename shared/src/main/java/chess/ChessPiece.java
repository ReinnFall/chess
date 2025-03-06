package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor; //Not sure how the color is set yet

        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
        //throw new RuntimeException("Not implemented");
    }
    private void promotionPawnMoves(ChessPosition myPosition,ChessPosition nextPosition, Collection<ChessMove> possibleMoves){
        possibleMoves.add(new ChessMove(myPosition,nextPosition,PieceType.QUEEN));
        possibleMoves.add(new ChessMove(myPosition,nextPosition,PieceType.ROOK));
        possibleMoves.add(new ChessMove(myPosition,nextPosition,PieceType.BISHOP));
        possibleMoves.add(new ChessMove(myPosition,nextPosition,PieceType.KNIGHT));

    }

    public void blackPawnMoves(ChessBoard board, ChessPosition myPosition, int limit, int rowDir, int colDir, Collection<ChessMove> possibleMoves, boolean isDiagonal){
        for (int i = 1; i <= limit; i++) {
            ChessPosition nextPiece = new ChessPosition((myPosition.getRow() + i * rowDir), (myPosition.getColumn() + i * colDir));
            if (nextPiece.getRow() < 1 || nextPiece.getRow() > 8 || nextPiece.getColumn() < 1 || nextPiece.getColumn() > 8) {
                break;
            }
            if(board.getPiece(nextPiece) == null && !isDiagonal) {
                if (nextPiece.getRow() == 1) {
                    promotionPawnMoves(myPosition,nextPiece,possibleMoves);
                }
                else{
                    possibleMoves.add(new ChessMove(myPosition,nextPiece,null));
                    if(myPosition.getRow() == 7){
                        ChessPosition nextPosition = new ChessPosition(myPosition.getRow() -2, myPosition.getColumn());
                        if(board.getPiece(nextPosition) == null){
                            possibleMoves.add(new ChessMove(myPosition,nextPosition,null));
                        }
                    }
                }
            }
            if(board.getPiece(nextPiece) != null){
                if(board.getPiece(nextPiece).getTeamColor() == ChessGame.TeamColor.WHITE){
                    if (nextPiece.getRow() == 1) {
                        promotionPawnMoves(myPosition,nextPiece,possibleMoves);
                    }
                    else{
                        possibleMoves.add(new ChessMove(myPosition,nextPiece,null));
                    }
                    break;
                }
            }
        }
    }

    public void whitePawnMoves(ChessBoard board, ChessPosition myPosition, int limit, int rowDir, int colDir, Collection<ChessMove> possibleMoves, boolean isDiagonal){
        for (int i = 1; i <= limit; i++) {
            ChessPosition nextPiece = new ChessPosition((myPosition.getRow() + i * rowDir), (myPosition.getColumn() + i * colDir));
            if (nextPiece.getRow() < 1 || nextPiece.getRow() > 8 || nextPiece.getColumn() < 1 || nextPiece.getColumn() > 8) {
                break;
            }
            if(board.getPiece(nextPiece) == null && isDiagonal == false) {
                if (nextPiece.getRow() == 8) {
                    promotionPawnMoves(myPosition,nextPiece,possibleMoves);
                }
                else{
                    possibleMoves.add(new ChessMove(myPosition,nextPiece,null));
                    if(myPosition.getRow() == 2){
                        ChessPosition nextPosition = new ChessPosition(myPosition.getRow() +2, myPosition.getColumn());
                        if(board.getPiece(nextPosition) == null){
                            possibleMoves.add(new ChessMove(myPosition,nextPosition,null));
                        }
                    }
                }
            }
            if(board.getPiece(nextPiece) != null){
                if(board.getPiece(nextPiece).getTeamColor() == ChessGame.TeamColor.BLACK ){
                    if(!isDiagonal){
                        break;
                    }
                    if (nextPiece.getRow() == 8) {
                        promotionPawnMoves(myPosition,nextPiece,possibleMoves);
                    }
                    else{
                        possibleMoves.add(new ChessMove(myPosition,nextPiece,null));
                    }
                    break;
                }
            }
        }
    }

    public void moveAcrossBoard(ChessPosition myPosition,ChessBoard board, int limit, int rowDir, int colDir, Collection<ChessMove> possibleMoves) {

        for (int i = 1; i <= limit; i++) {
            ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + i*rowDir, myPosition.getColumn()+ i*colDir);
            if (nextPosition.getRow() < 1 || nextPosition.getRow() > 8 || nextPosition.getColumn() < 1 || nextPosition.getColumn() > 8){
                break;
            }

            if (board.getPiece(nextPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, nextPosition, null));
            } else {
                if(board.getPiece(nextPosition).getTeamColor() != pieceColor){
                    possibleMoves.add(new ChessMove(myPosition,nextPosition,null));
                }
                break;
            }
        }

    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        int rowSame = 0;
        int rowUp = 1;
        int rowDown = -1;
        int rowUpTwo = 2;
        int rowDownTwo = -2;
        int colSame = 0;
        int colRight = 1;
        int colLeft = -1;
        int colRightTwo = 2;
        int colLeftTwo = -2;
        boolean diagonal = true;
        boolean notDiagonal = false;
        if (pieceType == PieceType.PAWN) {
            if(pieceColor == ChessGame.TeamColor.WHITE){
                int limit = 1;
                // Up One
                whitePawnMoves(board,myPosition,limit,rowUp,colSame,possibleMoves,notDiagonal);
                // Check Up 1 Right 1
                whitePawnMoves(board,myPosition,limit,rowUp,colRight,possibleMoves,diagonal);
                // Check Up 1 Left 1
                whitePawnMoves(board,myPosition,limit,rowUp,colLeft,possibleMoves,diagonal);
            }
            if(pieceColor == ChessGame.TeamColor.BLACK){
                int limit = 1;
                // Down One
                blackPawnMoves(board,myPosition,limit,rowDown,colSame,possibleMoves,notDiagonal);
                // Check Down 1 Right 1
                blackPawnMoves(board,myPosition,limit,rowDown,colRight,possibleMoves,diagonal);
                // Check Down 1 Left 1
                blackPawnMoves(board,myPosition,limit,rowDown,colLeft,possibleMoves,diagonal);
            }

        }
        if (pieceType == PieceType.ROOK){
            int limit = 8;
            // Rook Left Moves
            moveAcrossBoard(myPosition,board,limit,rowSame,colLeft,possibleMoves);
            // Rook Right Moves
            moveAcrossBoard(myPosition,board,limit,rowSame,colRight,possibleMoves);
            // Rook Up Moves
            moveAcrossBoard(myPosition,board,limit,rowUp,colSame,possibleMoves);
            // Rook Down Moves
            moveAcrossBoard(myPosition,board,limit,rowDown,colSame,possibleMoves);
        }
        if (pieceType == PieceType.KING){
            int limit = 1;
            // King Up Move
            moveAcrossBoard(myPosition,board,limit,rowUp,colSame,possibleMoves);
            // King Up Right Move
            moveAcrossBoard(myPosition,board,limit,rowUp,colRight,possibleMoves);
            // King Up Left Move
            moveAcrossBoard(myPosition,board,limit,rowUp,colLeft,possibleMoves);
            // King Right Move
            moveAcrossBoard(myPosition,board,limit,rowSame,colRight,possibleMoves);
            // King Left Move
            moveAcrossBoard(myPosition,board,limit,rowSame,colLeft,possibleMoves);
            // King Down Move
            moveAcrossBoard(myPosition,board,limit,rowDown,colSame,possibleMoves);
            // King Down Right Move
            moveAcrossBoard(myPosition,board,limit,rowDown,colRight,possibleMoves);
            // King Down Left Move
            moveAcrossBoard(myPosition,board,limit,rowDown,colLeft,possibleMoves);
        }
        if (pieceType == PieceType.BISHOP){
            int limit = 8;
            // Bishop Diagonal Up Right
            moveAcrossBoard(myPosition,board,limit,rowUp,colRight,possibleMoves);
            // Bishop Diagonal Up Left
            moveAcrossBoard(myPosition,board,limit,rowUp,colLeft,possibleMoves);
            // Bishop Diagonal Down Right
            moveAcrossBoard(myPosition,board,limit,rowDown,colRight,possibleMoves);
            // Bishop Diagonal Down Left
            moveAcrossBoard(myPosition,board,limit,rowDown,colLeft,possibleMoves);
        }
        if (pieceType == PieceType.QUEEN){
            int limit = 8;
            // Queen Up
            moveAcrossBoard(myPosition,board,limit,rowUp,colSame,possibleMoves);
            // Queen Down
            moveAcrossBoard(myPosition,board,limit,rowDown,colSame,possibleMoves);
            // Queen Right
            moveAcrossBoard(myPosition,board,limit,rowSame,colRight,possibleMoves);
            // Queen Left
            moveAcrossBoard(myPosition,board,limit,rowSame,colLeft,possibleMoves);
            // Queen Up Right
            moveAcrossBoard(myPosition,board,limit,rowUp,colRight,possibleMoves);
            // Queen Up Left
            moveAcrossBoard(myPosition,board,limit,rowUp,colLeft,possibleMoves);
            // Queen Down Right
            moveAcrossBoard(myPosition,board,limit,rowDown,colRight,possibleMoves);
            // Queen Down Left
            moveAcrossBoard(myPosition,board,limit,rowDown,colLeft,possibleMoves);
        }
        if (pieceType == PieceType.KNIGHT){
            int limit = 1;
            // Knight 2 Up 1 Right
            moveAcrossBoard(myPosition,board,limit,rowUpTwo,colRight,possibleMoves);
            // Knight 2 Up 1 Left
            moveAcrossBoard(myPosition,board,limit,rowUpTwo,colLeft,possibleMoves);
            // Knight 1 Up 2 Right
            moveAcrossBoard(myPosition,board,limit,rowUp,colRightTwo,possibleMoves);
            // Knight 1 Up 2 Left
            moveAcrossBoard(myPosition,board,limit,rowUp,colLeftTwo,possibleMoves);
            // Knight 2 Down 1 Right
            moveAcrossBoard(myPosition,board,limit,rowDownTwo,colRight,possibleMoves);
            // Knight 2 Down 1 Left
            moveAcrossBoard(myPosition,board,limit,rowDownTwo,colLeft,possibleMoves);
            // Knight 1 Down 2 Right
            moveAcrossBoard(myPosition,board,limit,rowDown,colRightTwo,possibleMoves);
            // Knight 1 Down 2 Left
            moveAcrossBoard(myPosition,board,limit,rowDown,colLeftTwo,possibleMoves);
        }
            return possibleMoves;
    }
}
