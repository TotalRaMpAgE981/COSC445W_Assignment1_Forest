package src;
public enum Direction
{
  UP(8), DOWN(2), LEFT(4), RIGHT(6), UPLEFT(7), UPRIGHT(9), DOWNLEFT(1), DOWNRIGHT(3), NONE(-1);
  public final int dir;
  Direction(int i) { dir = i; }
}
