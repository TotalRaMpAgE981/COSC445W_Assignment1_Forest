package src;

import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import java.util.InputMismatchException;

/*  Documentation

    Title: LostInForest
    Course: COSC 445W
    Date Created: January 30, 2021
    Last Updated: February 6, 2021
    Author: Tesean Ferguson 
    Email: fergusont2@duq.edu
    External Recourses: 
      AStar algorithm, https://rosettacode.org/wiki/A*_search_algorithm#Java

    Program file: LostInForest/src/Main.java
    Required external files: N/A
    Produced external files: N/A 

    Description:
      This program simulates 2 "players" that are lost in a forest.
      On alternating turns, a player will move according to the user selected AI (ActionCaller)
      This will run until either 1 million turns are passed or the two players end up on the same forest cell.

    P.S: Comments that seem out of place or with special characters are for formatting and easier categorization of methods.
         They work with an extension I use.
         example: (//%, //!, //page,..) 
*/

class Main
{
  static int forestWidth = 5;   //Determines the width of the forest.
  static int forestHeight = 5;  //Determines the height of the forest.
  static int aiType = 0;        //Determines the type of AI the Players' will use. (i.e. Random, Smart, AStar)
  static long gameSeed = 0;     //A randomly generated long that chooses how the Random Player will move.
  static boolean printGame = true;    //Determines if the game will print every step (turn).
  static boolean pauseOnStep = true;  //Determines if the user has to give an input to start the next step. 
  static boolean clearOnStep = false; //Determines if the screen is cleared after each step. (Does not work on some machines/terminals)
  static boolean userPlaying = false; //If true, the Pat's ActionCaller will be taken over by the UserPlayer, allowing the user to input movements.
  static boolean allowObstacles = false;  //Determines whether obstacles are generated when the forest is generated.
  static ForestGame forest;  //Holds all the functions for the game itself. 

  static Scanner kb = new Scanner(System.in); //Input scanner.
  static String errorInput = "Please input a valid option.\n> ";  //General error output String for when an incorrect input is encountered.
  public static void main(String[] args)
  {
    boolean flag = true;
    while(flag) //Keeps the game running
    {
      gameSeed = new Random().nextLong();
      switch(gameMenu())
      {
        case(1): gameStart(); break;
        case(2): kb = new Scanner(System.in); break;  //If gameMenu returns 2, then an input that breaks the scanner was input. This resets the scanner.
        default: flag = false;  //Exit flag
      }
    }
    kb.close();
  }

  //Sets up the objects that will be used in the game.
  private static ForestGame setUpGame()
  {
    // AI Types:    1-Random    2-Smart    3-AStar 
    Player pat;   //Player 1, passed to forest
    Player chris; //Player 2, passed to forest
    if(aiType == 0)
    {
      pat = new Player(aiType, "Pat", gameSeed); 
      chris = new Player(aiType, "Chris", gameSeed);
    } else 
    {
      pat = new Player(aiType, "Pat"); 
      chris = new Player(aiType, "Chris");
    }
    if(userPlaying) pat = new Player(3, "User");
    return new ForestGame(forestWidth, forestHeight, pat, chris);
  }

  //The main menu for the game
  private static int gameMenu() 
  {
    try 
    {
      while(true) //Keeps the menu running
      {
        System.out.print("\nForest Assignment\nSeed: "+ gameSeed + "\n\t1: New Game\n\t2: Options\n\t3: Exit\n> ");
        switch(kb.nextInt())
        {
          case(1): forest = setUpGame();  return 1;
          case(2): gameOptions(); break;
          case(3): return -1; //Exit signal
          default: System.out.print(errorInput);
        }
      }
    } catch (InputMismatchException e) {  
      System.err.println("Your input caused an error: " + e + "\nPlease don't try to break the game!\n");
      return 2;
    } catch (Exception e) 
    { 
      System.err.println("An unknown error occurred:\n\t " + e);
      
      return -1; 
    }
  }

  //Runs the game
  private static void gameStart()
  {
    if(forest != null)
    {
      try
      {
        if(allowObstacles)
          forest.generateTerrain(gameSeed);

        while(!forest.finished && forest.turnNum < 1000000) //Runs game until won or 1 million turns.
        {
          clearScreen();
          pauseStep();
          if(printGame)
          {
            forest.printHeader();
            forest.printGrid();
          }
          if(userPlaying) forest.printFooter();
          forest.step();
        }

        forest.printHeader();
        forest.printGrid();

        if(forest.finished) System.out.println("They found each other! :D");
        else System.out.println("They tired themselves out and died. D:");
        forest = null;
        pauseStep();
      } catch(NullPointerException e) 
      { 
        System.out.println("\nThe forest was to dense to travel through."); 
      } catch(IllegalArgumentException e)
      {
        System.err.println("A fatal error occurred:");
        e.printStackTrace();
        System.err.println("\t" + e);
        System.err.println("\t" + e.getMessage());
        System.err.println("\tUnfortunately, the AStar agent was not able to comprehend paths that have equal probability of success.");
        System.err.print("\tI am unable to fix this as it is Java's sort method and I did not write the AStar algorithm, ");
        System.err.println("thus I don't want to mess with its comparison function and potentially break it.");
      }catch(Exception e)
      {
        System.err.println("An unknown error occurred:");
        e.printStackTrace();
        System.err.println("\t" + e);
        System.err.println("\t" + e.getMessage());
      }
    } else System.err.println("The forest could not be properly generated!");
  }

  //Allows user to modify game variables that modify the ActionCaller and forest generation
  static void gameOptions() 
  {
    try
    {
      boolean flag0 = true;
      while(flag0)
      { 
        System.out.print("\nOptions");
        System.out.print("\n\t1: Set forest size");
        System.out.print("\n\t\tForest Width: " + forestWidth + "\n\t\tForest Height: " + forestHeight);
        System.out.print("\n\n\t2: Toggle AI: ");
        switch(aiType)
        {
          case(0): System.out.print("Random"); break;
          case(1): System.out.print("A bit smart"); break;
          case(2): System.out.print("Master of small forests"); break;
          default: System.err.print("Incorrect value selected, defaulting to Random"); aiType = 0;
        }
        System.out.print("\n\t3: Toggle trees: "+ onOff(allowObstacles));
        System.out.print("\n\t4: Play as Pat: "+ onOff(userPlaying));
        System.out.print("\n\n\t5: Change Game Seed: "+ gameSeed);
        System.out.print("\n\t6: Toggle Print Game: "+ onOff(printGame));
        System.out.print("\n\t7: Pause each turn: "+ onOff(pauseOnStep));
        System.out.print("\n\t8: Clear screen each turn (May not work for some systems): "+ onOff(clearOnStep));
        System.out.print("\n\t0: Return");
        System.out.print("\n> ");
        switch(kb.nextInt())
        {
          case(1):      
            System.out.print("\n\nForest Width\n> ");
            forestWidth = kb.nextInt();
            System.out.print("Forest Height\n> ");
            forestHeight = kb.nextInt();

            //Keeps forest size within bounds
            forestWidth = Math.min(forestWidth, 50);
            forestWidth = Math.max(forestWidth, 2);
            forestHeight = Math.min(forestHeight, 50);
            forestHeight = Math.max(forestHeight, 2);
            break;
          case(2): aiType = (aiType+1)%3; break;
          case(3): allowObstacles = !allowObstacles; break;
          case(4): userPlaying = !userPlaying; break;

          case(5): 
            System.out.print("\nEnter New Seed\n> ");
            gameSeed = kb.nextLong(); 
            break;
          case(6): printGame = !printGame; break;
          case(7): pauseOnStep = !pauseOnStep; break;
          case(8): clearOnStep = !clearOnStep; break;

          //Secret option that allows forest bounds to be exceeded
          case(999):
            System.out.println("Enter Unlimited Forest Size");
            System.out.print("\n\nForest Width\n> ");
            forestWidth = kb.nextInt();
            System.out.print("Forest Height\n> ");
            forestHeight = kb.nextInt();
            break;

          case(0): flag0 = false; break;
          default: System.err.println(errorInput);
        }
      }
    }catch(Exception e) 
    { 
      System.err.println("\nError: " + e); 
    }
  }

  //Clears terminal screen on compatible devices.
  private static void clearScreen()
  {  
    if(!clearOnStep) return;
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
    System.out.println();
  }  

  //Pauses for user input.
  private static void pauseStep() throws IOException
  {
    if(pauseOnStep) 
    {
      System.out.print("Press any key to continue...");
      System.in.read();
      System.in.read();
    }
  }

  //A format converter for booleans to allow for prettier outputs 
  private static String onOff(boolean b)
  {
    return b ? "On" : "Off";
  }
}