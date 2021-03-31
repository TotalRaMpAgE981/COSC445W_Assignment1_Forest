package src.aiTypes;
import src.Direction;
/*  
  Goal of the smart player is to:
    1. Not return to any spot they have recently been to.
    2. Choose directions that don't result in them bumping into something.
    3. Prioritize getting to the middle of the forest.

  This agent can and will get stuck if a long wall is in front of it's path to the middle
*/

//! Something strange happened when saving where Integer.MIN_VALUE turned into -Integer.MIN_VALUE.
// If this is still the case after I submit this, then a technical issue happened.

public class SmartPlayer implements ActionCaller
{
  int[][] gridVals;
  public void setGrid(int[][] grid) 
  { 
    gridVals = new int[grid.length][grid[0].length];
    for(int i = 0; i < grid.length; i ++)
      for( int j = 0; j < grid[0].length; j++)
        if(grid[i][j] == 0 || grid[i][j] == 10 || grid[i][j] == 11) gridVals[i][j] = 0;
        else gridVals[i][j] = Integer.MIN_VALUE;
  }

  //Returns the direction that gets the closest to the middle of the grid
  public Direction doAction(int[][] grid, int[] pos)
  { 
    //Some smart thinking stuff
    Direction d = Direction.NONE;
    int xPos = pos[0];
    int yPos = pos[1];
    int xLimit = grid.length-1;
    int yLimit = grid[0].length-1;
    int max = Integer.MIN_VALUE;
    gridVals[xPos][yPos] = -500;
    for(Direction e: Direction.values())
    {
      int x = xPos;
      int y = yPos;
      switch(e)
      {
        case UP: x--; break;
        case DOWN: x++; break;
        case LEFT: y--; break;
        case RIGHT: y++; break;

        case UPRIGHT: x--; y++; break;
        case UPLEFT: x--; y--; break;
        case DOWNRIGHT: x++; y++; break;
        case DOWNLEFT: x++; y--; break;

        case NONE: continue;
      }

      // if(x < 0 || x >= xLimit || y < 0 || y >= yLimit) continue; //Impossible case
      // if(gridVals[x][y] == -500) continue; //Already skips if on position. Also impossible :P
      int i = grid[x][y];
      if(i != -1 && i != 1 && i != 2) 
      {
        if(i == 10 || i == 11) gridVals[x][y] = 5*(xLimit*yLimit);  //If close to other, go to spot.
        else gridVals[x][y] = heuristic(x, y, xLimit, yLimit); //Else determine which is closest to the center of the forest
        if(max < gridVals[x][y])
        {
          d = e;
          max = gridVals[x][y];
        }
      } else gridVals[x][y] = Integer.MIN_VALUE; //Unimportant Fail safe, not necessary but keeping it anyways
    }

    if(d == Direction.NONE) //Reset gridVals if stuck
      for(int i = 1; i < gridVals.length-1; i ++)
        for( int j = 1; j < gridVals[0].length-1; j++)
          if(gridVals[i][j] != Integer.MIN_VALUE)
            gridVals[i][j] = 0;
    //Debug stuff
    // System.out.println("dir: " + d.name());
    // for(int i = 0; i < gridVals.length; i ++)
    // {
    //   for( int j = 0; j < gridVals[0].length; j++)
    //       System.out.print(gridVals[i][j] + " ");
    //   System.out.println();
    // }
    return d;
  }

  //Determines which direction is best to go to.
  private int heuristic(double a, double b, double aLim, double bLim)
  {
    //Found out why it wasn't working
    //I re-found out ^ doesn't work as the power symbol.
    double aVal = -1*Math.pow(((aLim/2) - a), 2) + 10*aLim;
    double bVal = -1*Math.pow(((bLim/2) - b), 2) + 10*bLim;
    return (int)(aVal+bVal);
  }
}
