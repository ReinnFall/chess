package chess;

import java.util.Collection;
import java.util.HashSet;
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
    public ChessGame(TeamColor turn,ChessBoard board){
        teamTurn = turn;
        gameBoard = board;
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
    public ChessBoard createSimBoard(){
        ChessBoard simBoard = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition tempPosition = new ChessPosition(i,j);
                if(gameBoard.getPiece(tempPosition) == null){
                    simBoard.addPiece(tempPosition,null);
                }
                else{
                    simBoard.addPiece(tempPosition,gameBoard.getPiece(tempPosition));
                }
            }
        }
        return simBoard;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> pieceMoves;
        Collection<ChessMove> updatedValidMoves = new HashSet<>();

        ChessPiece currentPiece = gameBoard.getPiece(startPosition);
        if (currentPiece == null){
            return null;
        }
        pieceMoves = currentPiece.pieceMoves(gameBoard,startPosition);
        for (ChessMove move : pieceMoves){
            /// Create a new board that I can simulate each piece move
            ChessBoard simBoard = createSimBoard();
            ChessPosition newPosition =  move.getEndPosition();

            /// Move the piece to the piece move
            simBoard.addPiece(newPosition,currentPiece);
            /// Make the space it moved from null
            simBoard.addPiece(startPosition,null);

            TeamColor turn = currentPiece.getTeamColor();

            ChessGame gameSim = new ChessGame(turn,simBoard);

            if(gameSim.isInCheck(turn) == false){
                updatedValidMoves.add(move);
            }
        }
        return updatedValidMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece currentPiece = gameBoard.getPiece(startPosition);
        if(currentPiece == null){
            throw new InvalidMoveException ("Move not accepted");
        }
        if (currentPiece.getTeamColor() != teamTurn){
            throw new InvalidMoveException ("Move not accepted");
        }


        /// Find all the valid moves for the piece in the current starting position
        Collection<ChessMove> possibleMoves = validMoves(startPosition);


        for(ChessMove eachPossibleMove : possibleMoves){
            if(eachPossibleMove.getEndPosition().equals(endPosition)){
                if(move.getPromotionPiece() == null) {
                    /// Move the piece
                    gameBoard.addPiece(endPosition, currentPiece);
                    /// Make the space it moved from null
                    gameBoard.addPiece(startPosition, null);

                    if (teamTurn == TeamColor.WHITE) {
                        teamTurn = TeamColor.BLACK;
                    } else {
                        teamTurn = TeamColor.WHITE;
                    }
                    return; /// Exit function
                }
                else{
                    /// Make new Chess Piece
                    ChessPiece promotionPiece = new ChessPiece(teamTurn,move.getPromotionPiece());
                    /// Move the piece
                    gameBoard.addPiece(endPosition, promotionPiece);
                    /// Make the space it moved from null
                    gameBoard.addPiece(startPosition, null);

                    if (teamTurn == TeamColor.WHITE) {
                        teamTurn = TeamColor.BLACK;
                    } else {
                        teamTurn = TeamColor.WHITE;
                    }
                    return; /// Exit function
                }
            }
        }

        throw new InvalidMoveException ("Move not accepted");
    }
    public ChessPosition findKing(TeamColor teamColor){
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                if(gameBoard.getPiece(currentPosition) != null) {
                    ChessPiece currentPiece = gameBoard.getPiece(currentPosition);
                    if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor) {
                        return currentPosition;
                    }
                }
            }
        }
        throw new RuntimeException("King not found");
    }
    public Collection<ChessMove> addEnemyMoves (TeamColor teamColor, Collection<ChessMove> enemyMoves){
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
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
            if(move.getEndPosition().equals(kingPosition) ){ /// check if this works
                return true;
            }
        }
       return false;
    }
    private Collection<ChessMove> getAllValidMoves(TeamColor teamColor){
        Collection<ChessMove> possibleMoves = new HashSet<>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                if(gameBoard.getPiece(currentPosition) != null){
                    ChessPiece currentPiece = gameBoard.getPiece(currentPosition);
                    if (currentPiece.getTeamColor() == teamColor) {
                        possibleMoves.addAll(validMoves(currentPosition));
                    }
                }
            }
        }
        return possibleMoves;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        // If no there are no valid moves left for the team color, then its checkmate
        // Iterate through each piece and call validMoves
        //  If that list is empty, then checkmate
        possibleMoves = getAllValidMoves(teamColor);
        if (possibleMoves.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        possibleMoves = getAllValidMoves(teamColor);
        if (possibleMoves.isEmpty() && isInCheck(teamColor) == false){
            return true;
        }
        else{
            return false;
        }
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
