package com.mrjaffesclass.othello;

import java.util.ArrayList;

/**
 * Othello class project competition player HamiltonHotz to rule the world!
 */
public class HamiltonHotz extends Player {


    private final int DEPTH = 8;

    /**
     * Instantiates a new Hamilton hotz.
     *
     * @param color the color of the player (Constants.BLACK or Constants.WHITE)
     */
    public HamiltonHotz(int color) {
        super(color);
    }

    /**
     * Get an array of the possible moves for a player
     * @param currentBoard the current board state
     * @param player the player to find moves for
     * @return the int [ ] of possible moves
     */
    private ArrayList<Position> getAllPossibleMoves(Board currentBoard, Player player) {
        ArrayList<Position> moves = new ArrayList<>();

        for (int row = 0; row < Constants.SIZE; row++)
        {
            for (int col = 0; col < Constants.SIZE; col++)
            {
                if (currentBoard.isLegalMove(this, new Position(row, col)))
                {
                    moves.add(new Position(row, col));
                }

            }
        }
        return moves;
    }

    /**
     * Override the getMove method to return a move determined by the minimax algorithm
     * @param board Game board
     * @return Move to make
     */
    @Override
    public Position getNextMove(Board board) {
        return getBestMove(board, this, DEPTH);
    }

    /**
     * Get the current score of the board
     * @param currentBoard the current board state
     * @param player the player to get the score for
     * @return the board score
     */
    private int getScore(Board currentBoard, Player player) {
        return currentBoard.countSquares(player.getColor());
    }

    /**
     * Get the board after a move has been made
     *
     * @param oldBoard the current board state
     * @param move     the move to make
     * @param player   the player making the move
     * @return the new board
     */
    public Board getNewBoardAfterMove(Board oldBoard, Position move, Player player) {

        Board newBoard = new Board();
        copyBoard(oldBoard, newBoard);
        newBoard.makeMove(player, move);

        return newBoard;
    }

    /**
     * Copy the contents of one board to another board
     * @param oldBoard the board to copy from
     * @param newBoard the board to copy to
     * Cannot @return the new board because it is passed by reference
     */
    private void copyBoard(Board oldBoard, Board newBoard) {
        for (int row = 0; row < Constants.SIZE; row++)
        {
            for (int col = 0; col < Constants.SIZE; col++)
            {
                Position pos = new Position(row, col);
                Player status = new Player(oldBoard.getSquare(pos).getStatus());
                newBoard.setSquare(status, pos);

            }
        }
    }

    /**
     * Get the best move for the current player
     *
     * @param currentBoard the current board state
     * @param player       the current player making the move
     * @param depth        the depth of the search tree
     * @return the best move
     */
    public Position getBestMove(Board currentBoard, Player player,int depth){
        int bestScore = Integer.MIN_VALUE;
        Position bestMove = null;
        for(Position move : getAllPossibleMoves(currentBoard,player)){
            //create new board
            Board newBoard = getNewBoardAfterMove(currentBoard,move,player);
            //recursive call
            int childScore = MMAB(newBoard,player,depth-1,false,Integer.MIN_VALUE,Integer.MAX_VALUE);
            if(childScore > bestScore) {
                bestScore = childScore;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * Minimax with alpha beta pruning
     * @param currentBoard the current board state
     * @param player the player who is making the move
     * @param depth the depth of the tree
     * @param isMax if the player is maximizing
     * @param alpha the alpha value for alpha beta pruning
     * @param beta the beta value for alpha beta pruning
     * @return the score of the current board as an int
     */
    private int MMAB(Board currentBoard,Player player,int depth,boolean isMax,int alpha,int beta) {
        if (depth == 0 || currentBoard.noMovesAvailable(player))
        {
            return getScore(currentBoard, player);
        }

        Player opponent = new Player(player.getColor() == Constants.WHITE ? Constants.BLACK : Constants.WHITE);
        //if no moves available then forfeit turn
        if((isMax && currentBoard.noMovesAvailable(opponent)) || (!isMax && currentBoard.noMovesAvailable(opponent))){
            //System.out.println("Forfeit State Reached !");
            return MMAB(currentBoard,player,depth-1,!isMax,alpha,beta);
        }
        int score;
        if(isMax){
            //maximizing
            score = Integer.MIN_VALUE;
            for(Position move : getAllPossibleMoves(currentBoard,player)){ //my turn
                //create new node
                Board newBoard = getNewBoardAfterMove(currentBoard,move,player);
                //recursive call
                int childScore = MMAB(newBoard,player,depth-1,false,alpha,beta);
                if(childScore > score) score = childScore;
                //update alpha
                if(score > alpha) alpha = score;
                if(beta <= alpha) break; //Beta Cutoff
            }
        }
        else
        {
            //minimizing
            score = Integer.MAX_VALUE;
            for(Position move : getAllPossibleMoves(currentBoard,opponent)){ //opponent turn
                //create new node
                Board newBoard = getNewBoardAfterMove(currentBoard,move,opponent);
                //recursive call
                int childScore = MMAB(newBoard,player,depth-1,true,alpha,beta);
                if(childScore < score) score = childScore;
                //update beta
                if(score < beta) beta = score;
                if(beta <= alpha) break; //Alpha Cutoff
            }
        }
        return score;
    }
}
