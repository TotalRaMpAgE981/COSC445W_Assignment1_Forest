package src.aiTypes;
import src.Direction;
import java.util.List;
import java.util.LinkedList;

//Uses an AStar algorithm to get the best path to the other player.
public class AStarPlayer implements ActionCaller
{
  int[][] gridVals;
  public void setGrid(int[][] grid) 
  {
    updateGridVals(grid);
  }

  //Returns a Direction based on the AStar algorithm's output.
  public Direction doAction(int[][] grid, int[] pos) throws IndexOutOfBoundsException
  { 
    //Some smart thinking stuff
    updateGridVals(grid);
    AStar finder = new AStar(gridVals, pos[1], pos[0], true);
    List<AStar.Node> l = new LinkedList<AStar.Node>();
    for(int i = 1; i < grid.length-1; i++)
      for(int j = 1; j < grid[0].length-1; j++)
        if((grid[i][j] == 10 || grid[i][j] == 11) && !(i == pos[0] && j == pos[1]))
          l = finder.findPathTo(j, i);

    return getDir(l.get(1).x - pos[1], l.get(1).y - pos[0]);
  }

  //Updates the grid to allow the AStar algorithm to return the correct outputs.
  private void updateGridVals(int[][] grid)
  {
    gridVals = new int[grid.length][grid[0].length];
    for(int i = 0; i < grid.length; i ++)
      for(int j = 0; j < grid[0].length; j++)
        if(grid[i][j] == 0 || grid[i][j] == 10 || grid[i][j] == 11) gridVals[i][j] = 0;
        else gridVals[i][j] = -1;

    
    //Debug stuff
    // for(int i = 0; i < gridVals.length; i ++)
    // {
    //   for( int j = 0; j < gridVals[0].length; j++)
    //       System.out.printf("%2d  ", gridVals[i][j]);
    //   System.out.println();
    // }
  }

  //Uses the new position from the AStar algorithm to return a direction.
  private Direction getDir(int x, int y)
  {
    int i = -1;
    switch(x)
    {
      case(0):
        switch(y)
        {
          case(0): i = -1; break;
          case(1): i = 2; break;
          case(-1): i = 8; break;
          default: i = -1;
        }
        break;

      case(1):
        switch(y)
        {
          case(0): i = 6; break;
          case(1): i = 3; break;
          case(-1): i = 9; break;
          default: i = -1;
        }
        break;

      case(-1):
        switch(y)
        {
          case(0): i = 4; break;
          case(1): i = 1; break;
          case(-1): i = 7; break;
          default: i = -1;
        }
        break;
        
      default: i = -1;
    }
    for(Direction e : Direction.values())
      if(e.dir == i)
        return e;
    return Direction.NONE;
  }
}


