package src.aiTypes;
import src.Direction;
import java.util.Scanner;

public class UserPlayer implements ActionCaller
{
  Scanner kb = new Scanner(System.in); //An input scanner
  public void setGrid(int[][] grid) { /*Does nothing*/ }
  
  //Returns the direction corelating the the user's input.
  public Direction doAction(int[][] grid, int[] pos)
  { 
    System.out.print("\nWhere will you move? \n> ");
    String s = kb.next();
    s = s.strip().toLowerCase();
    switch(s)
    {
      case("8"):
      case("n"):
      case("north"):
      case("up"):
        return Direction.UP;
        
      case("9"):
      case("ne"):
      case("northeast"):
      case("north east"):
      case("upright"):
      case("up right"):
        return Direction.UPRIGHT;

      case("7"):
      case("nw"):
      case("northwest"):
      case("north west"):
      case("upleft"):
      case("up left"):
        return Direction.UPLEFT;

      case("2"):
      case("s"):
      case("south"):
      case("down"):
        return Direction.DOWN;

      case("3"):
      case("se"):
      case("southeast"):
      case("south east"):
      case("downright"):
      case("down right"):
        return Direction.DOWNRIGHT;
        
      case("1"):
      case("sw"):
      case("southwest"):
      case("south west"):
      case("downleft"):
      case("down left"):
        return Direction.DOWNLEFT;

      case("a"):
      case("left"):
        return Direction.LEFT;

      case("d"):
      case("right"):
        return Direction.RIGHT;

      default: return Direction.NONE;
    }
  }
}
