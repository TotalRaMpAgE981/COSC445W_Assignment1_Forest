package src.aiTypes;
import src.Direction;
import java.util.Random;

//Returns a random Direction on each call to doAction. A seed can be input into the Randomizer.
public class RandomPlayer implements ActionCaller
{
  Random r;
  public void setGrid(int[][] grid) { /*Does nothing*/ }
  public RandomPlayer() { r = new Random(); }
  public RandomPlayer(long seed) { r = new Random(seed); }
  public Direction doAction(int[][] grid, int[] pos) { return Direction.values()[r.nextInt(Direction.values().length)];}
}
