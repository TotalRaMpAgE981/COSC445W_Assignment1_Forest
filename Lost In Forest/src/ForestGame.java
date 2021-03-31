package src;
import java.util.Random;

public class ForestGame 
{
  //! I accidentally made x-y (up,down) and y-x (left,right). I'm not fixing this.

  //Holds an x,y position. This is for comfort and is unnecessary. 
  private class Position
  {
    int x;
    int y;
    Position(int i, int j) { x = i; y = j; } 
  }

  //Holds Icons for printing.
  private enum Icon
  {
    GROUND(' '), TREE('*'), ROCK('o'), FENCE('#');
    char c;
    Icon(char c) { this.c = c;}
  }
  
  public int turnNum = 1; //The current turn number.

  private int[][] grid;   //The internal grid for the game.
  private Player playerA; //Player 1
  private Player playerB; //Player 2
  private char playerA_char; //Player 1's icon
  private char playerB_char; //Player 2's icon 
  private int playerTurn = 0; //Determines which player's turn it is
  public boolean finished = false; //Determines if the game is finished.

  /*
    Grid number meanings:
      0: ground
      -1: fence
      1: tree
      2: rock
      10: player 1
      11: player 2
  */

  //Constructor
  public ForestGame(int x, int y, Player a, Player b)
  {
    x = x+2; 
    y = y+2;
    grid = new int[x][y];
    playerA = a;
    playerB = b;
    playerA_char = a.name.charAt(0);
    playerB_char = b.name.charAt(0);

    for(int i = 0; i < x; i++)
      for(int j = 0; j < y; j++)
      {
        if(i == 0 || i == x-1 || j == 0 || j == y-1) grid[i][j] = -1;
        else grid[i][j] = 0;
      }
    grid[1][1] = 10;
    playerA.x = 1;
    playerA.y = 1;

    grid[x-2][y-2] = 11;
    playerB.x = x-2;
    playerB.y = y-2;
    setPlayerGrid();
  }

  //page                                                                                                         
  //^ General functions
  
  //Simulates a turn
  public void step()
  {
    turnNum++;
    Player p;
    if(playerTurn == 0) p = playerA;
    else p = playerB;

    playerTurn = (playerTurn+1)%2;

    Position pos = new Position(p.x, p.y); //Does not change, used for moving the player.
    Direction dest = Direction.NONE;      // The destination of the player. (Is set to NONE as it is an illegal move and will make the player do the first move) 
    while(!legalMove(new Position(p.x, p.y), dest))
      dest = p.doAction(grid, new int[] {pos.x, pos.y});

    Position newPos = moveTo(new Position(p.x, p.y), dest); //The new position the player will move to
    movePlayer(pos, newPos);
    updatePlayerPos(p, newPos);

    //#Debugging stuff
    // System.out.println("Dest: " + dest.name());
    // System.out.println("Player pos: " + pos.x + "," + pos.y);
    // System.out.println("New pos: " + newPos.x + "," + newPos.y);
    
    if(checkPlayersFound())
      finished = true;
  }

  //Generates trees within the forest, blocking movements. They act the same as fences
  public void generateTerrain(long seed)
  {
    int xL = grid.length;
    int yL = grid[0].length;

    double treeProb = (xL+yL+0.0)/((xL/2.0)*(yL/2.0));
    
    Random r = new Random(seed);
    double pTree = r.nextDouble();
    
    for(int i = 1; i < xL-1; i++)
      for(int j = 1; j < yL-1; j++)
      {
        if((i == 1 && j == 1 ) || (i == xL-2 && j == yL-2)) continue; 
        if(treeProb <= pTree)
        {
          pTree -= r.nextDouble()*2;
          grid[i][j] = 1;
        }else
          pTree = r.nextDouble();
      }

    //Creates a path if there is none //! This was scrapped due to the AStar algorithm being unreliable 
    // try
    // {
    //   Player a = new Player(2, "Non");
    //   a.x = 1;
    //   a.y = 1;
    //   a.doAction(grid, new int[] {a.x, a.y});
    // }catch(NullPointerException e)
    // {
    //   //Creates a possible path
    //   for(int i = 2; i < grid[0].length-3; i++)
    //   { 
    //     grid[2][i] = 0;
    //     grid[grid.length-3][i] = 0;
    //   }
    //   for(int i = 2; i < grid.length-3; i++)
    //   { 
    //     grid[i][2] = 0;
    //     grid[i][grid[0].length-3] = 0;
    //   }
    // }
    setPlayerGrid();
  }

  //page                                                                                                         
  //^ Internal functions
  
  //Checks if the players found each other
  private boolean checkPlayersFound()
  {
    boolean flag0 = true;
    boolean flag1 = true;
    for(int i = 0; i < grid.length; i++)
      for(int j = 0; j < grid[0].length; j++)
      {
        if(grid[i][j] == 10) flag0 = false;
        if(grid[i][j] == 11) flag1 = false;
      }

    return flag0 || flag1;
  }
  
  //Checks if a move in a specific direction is allowed
  private boolean legalMove(Position pos, Direction mov)
  {
    Position old = new Position(pos.x, pos.y);
    moveTo(pos, mov);
    if(pos == null || old == null) return false;
    if((old.x != pos.x || old.y != pos.y) && 
       (grid[pos.x][pos.y] == 0 || grid[pos.x][pos.y] == 10 || grid[pos.x][pos.y] == 11))
        return true;
    return false;
  }

  //Determines the new position a player will be in if they move a Direction 
  private Position moveTo(Position pos, Direction mov)
  {
    switch(mov)
    {
      case UP: pos.x--; break;
      case DOWN: pos.x++; break;
      case LEFT: pos.y--; break;
      case RIGHT: pos.y++; break;
      case UPRIGHT: pos.x--; pos.y++; break;
      case UPLEFT: pos.x--; pos.y--; break;
      case DOWNRIGHT: pos.x++; pos.y++; break;
      case DOWNLEFT: pos.x++; pos.y--; break;
      case NONE: break;
    }
    return pos;
  }

  //Moves the player to the new position
  private void movePlayer(Position pos, Position dest) 
  {
    grid[dest.x][dest.y] = grid[pos.x][pos.y];
    grid[pos.x][pos.y] = 0; //Players can only move on ground.
  }

  //Updates the grid for players
  private void setPlayerGrid()
  {
    playerA.setGrid(grid);
    playerB.setGrid(grid);
  }

  //Updates a players position as they do not know what they are on the grid
  private void updatePlayerPos(Player p, Position pos)
  {
    p.x = pos.x;
    p.y = pos.y;
  }

  //page                                                                                                         
  //^ External functions (Auxillary)
  
  //Prints turn count and who's turn it is
  public void printHeader()
  {
    System.out.println("\nTurn: " + turnNum);
    if(playerTurn == 0) System.out.println(playerA.name + "'s turn to move.");
    else System.out.println(playerB.name + "'s turn to move.");
  }

  //Prints the grid
  public void printGrid()
  {
    for(int i = 0; i < grid.length; i++)
    {
      System.out.print("\n\t");
      for(int j = 0; j < grid[0].length; j++)
      {
        System.out.print(" ");
        switch(grid[i][j])
        {
          case(0): System.out.print(Icon.GROUND.c); break;
          case(1): System.out.print(Icon.TREE.c); break;
          case(2): System.out.print(Icon.ROCK.c); break;
          case(-1): System.out.print(Icon.FENCE.c); break;
          case(10): System.out.print(playerA_char); break;
          case(11): System.out.print(playerB_char); break;
          default: System.out.print(" ");
        }
      }
    }
    System.out.println("\n");
  }

  //Prints controls is the user is playing
  public void printFooter()
  {
    System.out.println("\n");
    for(int i = 0; i < grid.length*3; i++)
      System.out.print("-");
    System.out.println("\nControls:\n\tNumber Pad - Movement\n\tor");
    System.out.print("\tN - North");
    System.out.print("    NE - North East");
    System.out.print("    E - East");
    System.out.print("    SE - South East");
    System.out.print("    S - South");
    System.out.print("    SW - South West");
    System.out.print("    W - West");
    System.out.println("    NW - North West");
  }
}
