package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard gameBoard;

    public ChessGame() {
        teamTurn = TeamColor.WHITE; ///White team starts

        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, gameBoard);
    }

    /// @return Which team's turn it is
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

   /// Enum identifying the 2 possible teams in a chess game
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currentPiece = gameBoard.getPiece(startPosition);
        if (currentPiece == null){
            return null;
        }
        ///  Need to iterate through each of the piece moves and check if moving that to that position leaves the king open
        ///  Could make a copy of the board and move the piece to the potential spot then check if King is in check/
        /// I would need to check if the king was in check beforehand
        return currentPiece.pieceMoves(gameBoard,startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }
    public ChessPosition findKing(TeamColor teamColor){
        for (int i = 1; i <= 8; i++){
            for (int j = 0; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentPiece = gameBoard.getPiece(currentPosition);
                if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor){
                    return currentPosition;
                }
            }
        }
        throw new RuntimeException("King not found");
    }
    public Collection<ChessMove> addEnemyMoves (TeamColor teamColor, Collection<ChessMove> enemyMoves){
        for (int i = 1; i <= 8; i++){
            for (int j = 0; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                if(gameBoard.getPiece(currentPosition) != null) {
                    ChessPiece currentPiece = gameBoard.getPiece(currentPosition);
                    if(currentPiece.getTeamColor() != teamColor) {
                        enemyMoves.addAll(currentPiece.pieceMoves(gameBoard, currentPosition));
                    }
                }
            }
        }
        return enemyMoves;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> enemyMoves = new HashSet<>();

        /// Find King
        ChessPosition kingPosition = findKing(teamColor);
        /// Add all enemy moves to our collection
        enemyMoves = addEnemyMoves(teamColor, enemyMoves);
        /// Iterate through all enemy moves and check if their end position matches the Kings current position
        for(ChessMove move : enemyMoves){
            if(move.getEndPosition() == kingPosition){ /// check if this works
                return true;
            }
        }
       return false;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> enemyMoves = new HashSet<>();
        if (isInCheck(teamColor) == true){
            /// Find King
            ChessPosition kingPosition = findKing(teamColor);
            /// Add all enemy moves to our collection
            enemyMoves = addEnemyMoves(teamColor, enemyMoves);
            /// Find valid moves of King and if it has none return true
        }
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
