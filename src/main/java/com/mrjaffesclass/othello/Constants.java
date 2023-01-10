package com.mrjaffesclass.othello;


/**
 * Game constants
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Constants
{
  // Constants used in game
  public final static String VERSION = "1.04";
  
  public final static int SIZE = 8;
  public final static int MOVE_COUNT = Constants.SIZE * Constants.SIZE - 4;

  public final static int WHITE = -1;
  public final static int BLACK = 1;
  public final static int EMPTY = 0;
  
  public final static int DELAY = 200;
          //1500;  // Milliseconds
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";
}
