package src.aiTypes;
import src.Direction;

//Determines which Direction will be returned.
public interface ActionCaller {
  public void setGrid(int[][] grid);
  public Direction doAction(int[][] grid, int[] pos);  
}
