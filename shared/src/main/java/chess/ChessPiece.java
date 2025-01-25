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
        Set<ChessMove> possibleMoves = new HashSet<>();
        if (pieceType == PieceType.PAWN) {
            /* Checks if White Pawns can move forward 1 and/or 2 spaces */
            if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.WHITE) {

                ChessPosition pieceAbove = new ChessPosition(3, myPosition.getColumn());
                ChessPosition twoPiecesAbove = new ChessPosition(4, myPosition.getColumn());

                if (board.getPiece(pieceAbove) == null) {
                    ChessMove forwardOne = new ChessMove(myPosition, pieceAbove, null);
                    possibleMoves.add(forwardOne);

                    if (board.getPiece(twoPiecesAbove) == null) {
                        ChessMove forwardTwo = new ChessMove(myPosition, twoPiecesAbove, null);
                        possibleMoves.add(forwardTwo);
                    }
                }
            }
            /* Checks if Black Pawns can move forward 1 and/or 2 spaces */
            if (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.BLACK) {

                ChessPosition pieceBelow = new ChessPosition(6, myPosition.getColumn());
                ChessPosition twoPiecesBelow = new ChessPosition(5, myPosition.getColumn());

                if (board.getPiece(pieceBelow) == null) {
                    ChessMove forwardOne = new ChessMove(myPosition, pieceBelow, null);
                    possibleMoves.add(forwardOne);

                    if (board.getPiece(twoPiecesBelow) == null) {
                        ChessMove forwardTwo = new ChessMove(myPosition, twoPiecesBelow, null);
                        possibleMoves.add(forwardTwo);
                    }
                }
            }
            /* Black Pawn Promotion Moves */
            if (myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.BLACK) {

                ChessPosition pieceBelow = new ChessPosition(1, myPosition.getColumn());

                if (board.getPiece(pieceBelow) == null) {
                    ChessMove promoQueen = new ChessMove(myPosition, pieceBelow, PieceType.QUEEN);
                    ChessMove promoRook = new ChessMove(myPosition, pieceBelow, PieceType.ROOK);
                    ChessMove promoKnight = new ChessMove(myPosition, pieceBelow, PieceType.KNIGHT);
                    ChessMove promoBishop = new ChessMove(myPosition, pieceBelow, PieceType.BISHOP);

                    possibleMoves.add(promoQueen);
                    possibleMoves.add(promoRook);
                    possibleMoves.add(promoKnight);
                    possibleMoves.add(promoBishop);
                }
                if (myPosition.getColumn() != 1){
                    ChessPosition pieceDiagonalLeft = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn() - 1);
                    if(board.getPiece(pieceDiagonalLeft) != null ) {
                        if(board.getPiece(pieceDiagonalLeft).getTeamColor() == ChessGame.TeamColor.WHITE){
                            ChessMove diagonalLeftPromoQueen = new ChessMove(myPosition,pieceDiagonalLeft,PieceType.QUEEN);
                            ChessMove diagonalLeftPromoRook = new ChessMove(myPosition,pieceDiagonalLeft,PieceType.ROOK);
                            ChessMove diagonalLeftPromoBishop = new ChessMove(myPosition,pieceDiagonalLeft,PieceType.BISHOP);
                            ChessMove diagonalLeftPromoKnight = new ChessMove(myPosition,pieceDiagonalLeft,PieceType.KNIGHT);

                            possibleMoves.add(diagonalLeftPromoQueen);
                            possibleMoves.add(diagonalLeftPromoRook);
                            possibleMoves.add(diagonalLeftPromoKnight);
                            possibleMoves.add(diagonalLeftPromoBishop);
                        }
                    }
                }
                if (myPosition.getColumn() != 8){
                    ChessPosition pieceDiagonalRight = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn() + 1);
                    if(board.getPiece(pieceDiagonalRight) != null) {
                        if(board.getPiece(pieceDiagonalRight).getTeamColor() == ChessGame.TeamColor.WHITE ) {
                            ChessMove diagonalRightPromoQueen = new ChessMove(myPosition, pieceDiagonalRight, PieceType.QUEEN);
                            ChessMove diagonalRightPromoRook = new ChessMove(myPosition, pieceDiagonalRight, PieceType.ROOK);
                            ChessMove diagonalRightPromoBishop = new ChessMove(myPosition, pieceDiagonalRight, PieceType.BISHOP);
                            ChessMove diagonalRightPromoKnight = new ChessMove(myPosition, pieceDiagonalRight, PieceType.KNIGHT);

                            possibleMoves.add(diagonalRightPromoQueen);
                            possibleMoves.add(diagonalRightPromoRook);
                            possibleMoves.add(diagonalRightPromoKnight);
                            possibleMoves.add(diagonalRightPromoBishop);
                        }
                    }
                }
            }
            /* White Pawn Promotion Moves */
            if (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.WHITE) {

                ChessPosition pieceAbove = new ChessPosition(8, myPosition.getColumn());

                if (board.getPiece(pieceAbove) == null) {
                    ChessMove promoQueen = new ChessMove(myPosition, pieceAbove, PieceType.QUEEN);
                    ChessMove promoRook = new ChessMove(myPosition, pieceAbove, PieceType.ROOK);
                    ChessMove promoKnight = new ChessMove(myPosition, pieceAbove, PieceType.KNIGHT);
                    ChessMove promoBishop = new ChessMove(myPosition, pieceAbove, PieceType.BISHOP);

                    possibleMoves.add(promoQueen);
                    possibleMoves.add(promoRook);
                    possibleMoves.add(promoKnight);
                    possibleMoves.add(promoBishop);
                }
                if (myPosition.getColumn() != 1){
                    ChessPosition pieceDiagonalLeft = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn() - 1);
                    if(board.getPiece(pieceDiagonalLeft) != null ) {
                        if(board.getPiece(pieceDiagonalLeft).getTeamColor() == ChessGame.TeamColor.BLACK ) {
                            ChessMove diagonalLeftPromoQueen = new ChessMove(myPosition, pieceDiagonalLeft, PieceType.QUEEN);
                            ChessMove diagonalLeftPromoRook = new ChessMove(myPosition, pieceDiagonalLeft, PieceType.ROOK);
                            ChessMove diagonalLeftPromoBishop = new ChessMove(myPosition, pieceDiagonalLeft, PieceType.BISHOP);
                            ChessMove diagonalLeftPromoKnight = new ChessMove(myPosition, pieceDiagonalLeft, PieceType.KNIGHT);

                            possibleMoves.add(diagonalLeftPromoQueen);
                            possibleMoves.add(diagonalLeftPromoRook);
                            possibleMoves.add(diagonalLeftPromoKnight);
                            possibleMoves.add(diagonalLeftPromoBishop);
                        }
                    }
                }
                if (myPosition.getColumn() != 8){
                    ChessPosition pieceDiagonalRight = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn() + 1);
                    if(board.getPiece(pieceDiagonalRight) != null) {
                        if(board.getPiece(pieceDiagonalRight).getTeamColor() == ChessGame.TeamColor.BLACK ) {

                            ChessMove diagonalRightPromoQueen = new ChessMove(myPosition, pieceDiagonalRight, PieceType.QUEEN);
                            ChessMove diagonalRightPromoRook = new ChessMove(myPosition, pieceDiagonalRight, PieceType.ROOK);
                            ChessMove diagonalRightPromoBishop = new ChessMove(myPosition, pieceDiagonalRight, PieceType.BISHOP);
                            ChessMove diagonalRightPromoKnight = new ChessMove(myPosition, pieceDiagonalRight, PieceType.KNIGHT);

                            possibleMoves.add(diagonalRightPromoQueen);
                            possibleMoves.add(diagonalRightPromoRook);
                            possibleMoves.add(diagonalRightPromoKnight);
                            possibleMoves.add(diagonalRightPromoBishop);
                        }
                    }
                }
            }
            /* White Pawn Normal Moving and Capture Logic */
            if ((myPosition.getRow() == 2 || myPosition.getRow() == 3 || myPosition.getRow() == 4 || myPosition.getRow() == 5 || myPosition.getRow() == 6) && pieceColor == ChessGame.TeamColor.WHITE){

                ChessPosition pieceAbove = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if (board.getPiece(pieceAbove) == null) {
                    ChessMove forwardOne = new ChessMove(myPosition, pieceAbove, null);
                    possibleMoves.add(forwardOne);
                }
                if (myPosition.getColumn() != 1){
                    ChessPosition pieceDiagonalLeft = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn() - 1);
                    if(board.getPiece(pieceDiagonalLeft) != null ) {
                        if(board.getPiece(pieceDiagonalLeft).getTeamColor() == ChessGame.TeamColor.BLACK ) {
                            ChessMove diagonalLeftCapture = new ChessMove(myPosition, pieceDiagonalLeft, null);
                            possibleMoves.add(diagonalLeftCapture);
                        }
                    }
                }
                if (myPosition.getColumn() != 8){
                    ChessPosition pieceDiagonalRight = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn() + 1);
                    if(board.getPiece(pieceDiagonalRight) != null) {
                        if(board.getPiece(pieceDiagonalRight).getTeamColor() == ChessGame.TeamColor.BLACK ) {
                            ChessMove diagonalRightCapture = new ChessMove(myPosition, pieceDiagonalRight, null);
                            possibleMoves.add(diagonalRightCapture);
                        }
                    }
                }
            }
            /* Black Pawn
             Normal Moving and Capture Logic */
            if ((myPosition.getRow() == 7 || myPosition.getRow() == 6 || myPosition.getRow() == 5 || myPosition.getRow() == 4 || myPosition.getRow() == 3) && pieceColor == ChessGame.TeamColor.BLACK){

                ChessPosition pieceBelow = new ChessPosition(myPosition.getRow() -1, myPosition.getColumn());
                if (board.getPiece(pieceBelow) == null) {
                    ChessMove forwardOne = new ChessMove(myPosition, pieceBelow, null);
                    possibleMoves.add(forwardOne);
                }
                if (myPosition.getColumn() != 1){
                    ChessPosition pieceDiagonalLeft = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn() - 1);
                    if(board.getPiece(pieceDiagonalLeft) != null ) {
                        if(board.getPiece(pieceDiagonalLeft).getTeamColor() == ChessGame.TeamColor.WHITE ) {
                            ChessMove diagonalLeftCapture = new ChessMove(myPosition, pieceDiagonalLeft, null);
                            possibleMoves.add(diagonalLeftCapture);
                        }
                    }
                }
                if (myPosition.getColumn() != 8){
                    ChessPosition pieceDiagonalRight = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn() + 1);
                    if(board.getPiece(pieceDiagonalRight) != null) {
                        if(board.getPiece(pieceDiagonalRight).getTeamColor() == ChessGame.TeamColor.WHITE ) {
                            ChessMove diagonalRightCapture = new ChessMove(myPosition, pieceDiagonalRight, null);
                            possibleMoves.add(diagonalRightCapture);
                        }
                    }
                }
            }
        }
        if (pieceType == PieceType.ROOK){


        }
        if(pieceType == PieceType.KING){
            /// Corner White Kings

            /// Bottom Left
            if(myPosition.getRow() == 1 && myPosition.getColumn() == 1 && pieceColor == ChessGame.TeamColor.WHITE){

                ChessPosition top = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition diagonal = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);

                if(board.getPiece(top) == null){
                    ChessMove corner = new ChessMove(myPosition,top,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(top)!= null){
                    if(board.getPiece(top).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,top,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(diagonal) == null){
                    ChessMove corner = new ChessMove(myPosition,diagonal,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(diagonal)!= null){
                    if(board.getPiece(diagonal).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,diagonal,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(right) == null){
                    ChessMove corner = new ChessMove(myPosition,right,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(right)!= null){
                    if(board.getPiece(right).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,right,null);
                        possibleMoves.add(corner);
                    }
                }
            }
            /// Bottom Right
            if(myPosition.getRow() == 1 && myPosition.getColumn() == 8 && pieceColor == ChessGame.TeamColor.WHITE){

                ChessPosition top = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition diagonal = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);

                if(board.getPiece(top) == null){
                    ChessMove corner = new ChessMove(myPosition,top,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(top)!= null){
                    if(board.getPiece(top).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,top,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(diagonal) == null){
                    ChessMove corner = new ChessMove(myPosition,diagonal,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(diagonal)!= null){
                    if(board.getPiece(diagonal).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,diagonal,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(left) == null){
                    ChessMove corner = new ChessMove(myPosition,left,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(left)!= null){
                    if(board.getPiece(left).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,left,null);
                        possibleMoves.add(corner);
                    }
                }
            }
            /// Top Right
            if(myPosition.getRow() == 8 && myPosition.getColumn() == 8 && pieceColor == ChessGame.TeamColor.WHITE){

                ChessPosition bottom = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition diagonal = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);

                if(board.getPiece(bottom) == null){
                    ChessMove corner = new ChessMove(myPosition,bottom,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(bottom)!= null){
                    if(board.getPiece(bottom).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,bottom,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(diagonal) == null){
                    ChessMove corner = new ChessMove(myPosition,diagonal,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(diagonal)!= null){
                    if(board.getPiece(diagonal).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,diagonal,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(left) == null){
                    ChessMove corner = new ChessMove(myPosition,left,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(left)!= null){
                    if(board.getPiece(left).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,left,null);
                        possibleMoves.add(corner);
                    }
                }
            }
            /// Top Left
            if(myPosition.getRow() == 8 && myPosition.getColumn() == 8 && pieceColor == ChessGame.TeamColor.WHITE){

                ChessPosition bottom = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition diagonal = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);

                if(board.getPiece(bottom) == null){
                    ChessMove corner = new ChessMove(myPosition,bottom,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(bottom)!= null){
                    if(board.getPiece(bottom).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,bottom,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(diagonal) == null){
                    ChessMove corner = new ChessMove(myPosition,diagonal,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(diagonal)!= null){
                    if(board.getPiece(diagonal).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,diagonal,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(right) == null){
                    ChessMove corner = new ChessMove(myPosition,right,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(right)!= null){
                    if(board.getPiece(right).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessMove corner = new ChessMove(myPosition,right,null);
                        possibleMoves.add(corner);
                    }
                }
            }
            /// Corner Black Kings

            /// Bottom Left
            if(myPosition.getRow() == 1 && myPosition.getColumn() == 1 && pieceColor == ChessGame.TeamColor.BLACK){

                ChessPosition top = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition diagonal = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
                ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);

                if(board.getPiece(top) == null){
                    ChessMove corner = new ChessMove(myPosition,top,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(top)!= null){
                    if(board.getPiece(top).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,top,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(diagonal) == null){
                    ChessMove corner = new ChessMove(myPosition,diagonal,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(diagonal)!= null){
                    if(board.getPiece(diagonal).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,diagonal,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(right) == null){
                    ChessMove corner = new ChessMove(myPosition,right,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(right)!= null){
                    if(board.getPiece(right).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,right,null);
                        possibleMoves.add(corner);
                    }
                }
            }
            /// Bottom Right
            if(myPosition.getRow() == 1 && myPosition.getColumn() == 8 && pieceColor == ChessGame.TeamColor.BLACK){

                ChessPosition top = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                ChessPosition diagonal = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
                ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);

                if(board.getPiece(top) == null){
                    ChessMove corner = new ChessMove(myPosition,top,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(top)!= null){
                    if(board.getPiece(top).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,top,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(diagonal) == null){
                    ChessMove corner = new ChessMove(myPosition,diagonal,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(diagonal)!= null){
                    if(board.getPiece(diagonal).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,diagonal,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(left) == null){
                    ChessMove corner = new ChessMove(myPosition,left,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(left)!= null){
                    if(board.getPiece(left).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,left,null);
                        possibleMoves.add(corner);
                    }
                }
            }
            /// Top Right
            if(myPosition.getRow() == 8 && myPosition.getColumn() == 8 && pieceColor == ChessGame.TeamColor.BLACK){

                ChessPosition bottom = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition diagonal = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
                ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);

                if(board.getPiece(bottom) == null){
                    ChessMove corner = new ChessMove(myPosition,bottom,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(bottom)!= null){
                    if(board.getPiece(bottom).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,bottom,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(diagonal) == null){
                    ChessMove corner = new ChessMove(myPosition,diagonal,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(diagonal)!= null){
                    if(board.getPiece(diagonal).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,diagonal,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(left) == null){
                    ChessMove corner = new ChessMove(myPosition,left,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(left)!= null){
                    if(board.getPiece(left).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,left,null);
                        possibleMoves.add(corner);
                    }
                }
            }
            /// Top Left
            if(myPosition.getRow() == 8 && myPosition.getColumn() == 8 && pieceColor == ChessGame.TeamColor.BLACK){

                ChessPosition bottom = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                ChessPosition diagonal = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
                ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);

                if(board.getPiece(bottom) == null){
                    ChessMove corner = new ChessMove(myPosition,bottom,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(bottom)!= null){
                    if(board.getPiece(bottom).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,bottom,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(diagonal) == null){
                    ChessMove corner = new ChessMove(myPosition,diagonal,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(diagonal)!= null){
                    if(board.getPiece(diagonal).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,diagonal,null);
                        possibleMoves.add(corner);
                    }
                }
                if(board.getPiece(right) == null){
                    ChessMove corner = new ChessMove(myPosition,right,null);
                    possibleMoves.add(corner);
                }
                if(board.getPiece(right)!= null){
                    if(board.getPiece(right).getTeamColor() == ChessGame.TeamColor.WHITE){
                        ChessMove corner = new ChessMove(myPosition,right,null);
                        possibleMoves.add(corner);
                    }
                }
            }
            
        }
            return possibleMoves;
    }
}

////int colPosition = myPosition.getColumn();
////                ChessPosition pieceToTheLeft = new ChessPosition(myPosition.getRow(),myPosition.getColumn() -1);
////                do {
////                    if(board.getPiece(pieceToTheLeft) == null){
////                        ChessMove emptyLeft = new ChessMove(myPosition,pieceToTheLeft,null);
////                        possibleMoves.add(emptyLeft);
////                    }
////
////                    colPosition--;
////                    ChessPosition pieceToTheLeft = new ChessPosition(myPosition.getRow(),myPosition.getColumn() -1);
////                } while (board.getPiece(pieceToTheLeft) == null && colPosition > 1)