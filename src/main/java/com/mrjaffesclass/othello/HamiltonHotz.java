package com.mrjaffesclass.othello;

import java.util.ArrayList;
import java.util.Timer;


/**
 * The type Hamilton hotz.
 */
public class HamiltonHotz extends Player {

    private final int DEPTH = 6;
    /**
     * Instantiates a new Hamilton hotz.
     *
     * @param color the color
     */
    public HamiltonHotz(int color) {
        super(color);
    }

    // add the list of available moves of the current board to the list of moves
    private ArrayList<Position> getAllPossibleMoves(Board currentBoard, Player player) {
        ArrayList<Position> moves = new ArrayList<>();
        for(int row = 0; row < Constants.SIZE; row++) {

            for(int col = 0; col < Constants.SIZE; col++) {

                if (currentBoard.isLegalMove(this, new Position(row, col))) {
                    moves.add(new Position(row, col));
                }

            }
        }
        return moves;
    }

    @Override
    public Position getNextMove(Board board) {
        // Create a timer to record how long the algorithm takes to run
        Timer timer = new Timer();
        timer.run();
        Position bestMove =  getBestMove(board, this, DEPTH);
        timer.stop();
        System.out.println("Time: " + timer.getElapsedTime() + " ms");
        return bestMove;
    }

    //Get the score of the current board
    private int getScore(Board currentBoard, Player player) {
        int score = 0;

        score += currentBoard.countSquares(player.getColor());

        return score;
    }

    public Board getNewBoardAfterMove(Board oldBoard, Position move, Player player) {

        Board newBoard = new Board();
        copyBoard(oldBoard, newBoard);
        newBoard.makeMove(player, move);

        return newBoard;
    }

    private void copyBoard(Board oldBoard, Board newBoard) {
        for(int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position pos = new Position(row, col);
                Player status = new Player(oldBoard.getSquare(pos).getStatus());

                newBoard.setSquare(status, pos);
            }
        }
    }

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
