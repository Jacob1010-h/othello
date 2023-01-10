/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mrjaffesclass.othello;

import java.util.ArrayList;

/**
 *
 * @author kekec
 */
public class MoritaWebb extends Player{
    
    private Player opponent;

  /**
   * Constructor
   * @param color Player color: one of Constants.BLACK or Constants.WHITE
   */
  public MoritaWebb(int color) {
    super(color);
    opponent = new Player(-color);
  }

  /**
   *
   * @param board
   * @return The player's next move
   */
  @Override
  public Position getNextMove(Board board) {
     /* Your code goes here */

     return minimax(board, 8, Integer.MIN_VALUE,Integer.MAX_VALUE, true).getBestMove();
     
  }
  
  public minimaxReturn minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer){
      Position bestMove = null;
      int bestScore;
      int tmp;
      ArrayList<Position> possibleMoves = getPossibleMoves(board);
      if(depth == 0 || possibleMoves.isEmpty()){
          return new minimaxReturn(null, score_board(board));
      }
      if(maximizingPlayer){
          bestScore = Integer.MIN_VALUE;
          for(Position move:possibleMoves){
              Board copyBoard = copyBoard(board);
              copyBoard.makeMove(this, move);
              tmp = minimax(copyBoard, depth - 1, alpha ,beta , false).getBestScore();
              if(bestScore < tmp){
                  bestScore = tmp;
                  bestMove = move;
              }
              if(bestScore>=beta){
                  break;
              }
              alpha = Math.max(bestScore, alpha);
          }
      }
      else{
          bestScore = Integer.MAX_VALUE;
          for(Position move: possibleMoves){
              Board copyBoard = copyBoard(board);
              copyBoard.makeMove(opponent, move);
              tmp = minimax(copyBoard, depth - 1, alpha, beta, true).getBestScore();
              if(bestScore > tmp){
                  bestScore = tmp;
                  bestMove = move;
              }
              if(bestScore <= alpha){
                  break;
              }
              beta = Math.min(bestScore, beta);
          }
      }
      return new minimaxReturn(bestMove,bestScore);
  }
  
  public ArrayList<Position> getPossibleMoves(Board board){
      Position checkPos;
      ArrayList<Position> legalMoves = new ArrayList<Position>();
      for(int column = 0; column < Constants.SIZE; column++){
          for(int row = 0; row < Constants.SIZE; row++){
              checkPos = new Position(row,column);
              if(board.isLegalMove(this, checkPos)){
                  legalMoves.add(checkPos);
              }
          }
      }
      return legalMoves;
  }
  
  public int evaluateBoard(Board board){
      int score = (board.countSquares(this.getColor()) - board.countSquares(-this.getColor()));
      int cornors[] = {board.getSquare(this, 0, 0).getStatus(),board.getSquare(this, 0, Constants.SIZE-1).getStatus(),board.getSquare(this, Constants.SIZE-1, 0).getStatus(),board.getSquare(this, Constants.SIZE-1, Constants.SIZE-1).getStatus()};
      for(int cornor:cornors){
          if(cornor != Constants.EMPTY){
             score += cornor*this.getColor();
          }
      }
      
      return score;
   }
  
  public Board copyBoard(Board board){
      Position checkPos;
      int squareColor;
      Board copy = new Board();
      for (int column = 0; column < Constants.SIZE; column++) {
          for (int row = 0; row < Constants.SIZE; row++) {
              checkPos = new Position(row,column);
              squareColor = board.getSquare(checkPos).getStatus();
              if(squareColor == this.getColor()){   
                copy.setSquare(this, checkPos);
              } else if(squareColor == opponent.getColor()){
                copy.setSquare(opponent,checkPos);
              }
           }
       }
      return copy;
  }

  public float average(int[] data){
        int total = 0;
        int e = 0;
        float avg = 0;
        for (e = 0; e < data.length; e++){
          total = total + data[e];
        }
        avg = total/e;
        return avg;
      }
  
  //public int countEnemyMoves(Board board)
  
  public int score_board(Board board){
      
      int cornersOwned = 0;
      int edgesOwned = 0;
      int movesPossible = 0;
      Position posCheck;
      for (int column = 0; column < Constants.SIZE; column++) {
          for (int row = 0; row < Constants.SIZE; row++) {
            posCheck = new Position(row,column);
              
            if (column == 0 && row == 0 || column == 0 && row == 7 || column == 7 && row == 0 || column == 7 && row == 7){
              cornersOwned++;
            }
            if (column == 0 || row == 0 || column == 7 || row == 7){
              edgesOwned++;
            }
            if (board.isLegalMove(this, posCheck)){
              movesPossible++;
            }
          }
      }
      int data[] = {cornersOwned, edgesOwned, movesPossible};
      return Math.round(average(data)*(edgesOwned*3));
    }
}

class minimaxReturn{
    
    private Position bestMove;
    private int bestScore;
    
    minimaxReturn(Position bestMove, int bestScore){
        this.bestMove = bestMove;
        this.bestScore = bestScore;
    }
    
    public int getBestScore(){
        return this.bestScore;
    }
    
    public Position getBestMove(){
        return this.bestMove;
    }
    
}

