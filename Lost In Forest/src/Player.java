package src;
import src.aiTypes.*;
import java.util.Random;
public class Player 
{
  long seed;            //Specifically for the random player, sets the randomizer's outputs
  String name = "Buh";  //The player's name. 
  ActionCaller a;       //Produces movements for the ForestGame to read
  public int x = -1;    //Internal x position.
  public int y = -1;    //Internal y position.

  //Constructors
  public Player(int i, String name){ this.name = name; a = setActionCaller(i); seed = new Random().nextLong(); }
  public Player(int i, String name, long seed) { this.name = name; this.seed = seed; a = setActionCaller(i); }

  //Creates a different ActionCaller based on the argument.
  private ActionCaller setActionCaller(int i) 
  {
    switch(i)
    {
      case(0): return new RandomPlayer(seed);
      case(1): return new SmartPlayer();
      case(2): return new AStarPlayer();
      case(3): return new UserPlayer();
      default: return new RandomPlayer();
    }
  }

  //Sets the internal grid for ActionCallers that use it.
  public void setGrid(int[][] grid)
  {
    a.setGrid(grid);
  }

  //Returns a Direction for the ForestGame to read.
  public Direction doAction(int[][] grid, int[] pos) 
    {
      if(pos.length == 2)
        return a.doAction(grid, pos);
      System.err.println("Position is invalid, returning null");
      return null;  
    }
}
