import java.awt.Graphics2D;
import java.lang.Math;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.util.Queue;
import java.awt.Point;
import java.util.Stack;
import java.util.LinkedList;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; 
import java.awt.geom.RoundRectangle2D;
import java.awt.Color;
public class Board {
   final int OLD = 0;
   final int NEW = 1;
   int gridWidth = 4;
   int gridHeight = 4;
   int offsetX;
   int offsetY;
   public static int tileDim = 100;
   public static int arc = 5;
   public static int gap = 15;
   public int backdropWidth = gridWidth * (tileDim + gap) + gap;
   public int backdropHeight = gridHeight * (tileDim + gap) + gap;
   ArrayList<ArrayList<ArrayList<Tile>>> tiles = new ArrayList<>(gridWidth);
   private Game game;
   public Board(Game game) {
      // this.offsetX = (int) ((gameWidth - backdropWidth) / 2);
      // this.offsetY = (int) ((gameHeight - backdropHeight) / 5 * 2.5);
      this.game = game;
      //clearBoard();
      newBoard();
      //addTile(2, 3, 0);
      //addTile(2, 1, 0);
      //addTile(4, 0, 0);
      //addTile(2, 2, 0);
      //losingBoardSetup();
      //winningBoardSetup();
   }

   private void losingBoardSetup() {
      addTile(8, 0, 0);
      addTile(64, 1, 0);
      addTile(16, 2, 0);
      addTile(4, 3, 0);
      addTile(16, 0, 1);
      addTile(256, 1, 1);
      addTile(32, 2, 1);
      addTile(2, 3, 1);
      addTile(8, 0, 2);
      addTile(1024, 1, 2);
      addTile(128, 2, 2);
      addTile(8, 3, 2);
      addTile(16, 0, 3);
      addTile(8, 1, 3);
      //addTile(4, 2, 3);
      
   }
   private void winningBoardSetup() {
      addTile(64, 1, 0);
      addTile(16, 2, 0);
      addTile(4, 3, 0);
      addTile(16, 0, 1);
      addTile(256, 1, 1);
      addTile(32, 2, 1);
      addTile(2, 3, 1);
      addTile(8, 0, 2);
      addTile(1024, 1, 2);
      addTile(128, 2, 2);
      addTile(8, 3, 2);
      addTile(16, 0, 3);
      addTile(8, 1, 3);
      addTile(1024, 2, 3);
      addTile(1024, 3, 3);
      //addTile(4, 2, 3);
      
   }
   public void resize(int windowWidth, int windowHeight) {
      this.offsetX = (int) ((windowWidth - backdropWidth) / 2);
      this.offsetY = (int) ((windowHeight - backdropHeight) / 5 * 4);
      for (int x = 0; x < gridWidth; x++) {
         for (int y = 0; y < gridHeight; y++) {
            for (Tile t : getTiles(x, y)) {
               t.adjustPosition(getX(x), getY(y));
            }
         }
      }
   }

   private ArrayList<Point> getEmptyCells() {
      ArrayList<Point> emptyCells = new ArrayList<>();
      for (int x = 0; x < gridWidth; x++) {
         for (int y = 0; y < gridHeight; y++) {
            if (getTiles(x, y).isEmpty()) {
               emptyCells.add(new Point(x, y));
            }
         }
      }
      return emptyCells;
   }
   private void spawnRandomTile() {
      ArrayList<Point> emptyCells = getEmptyCells();
      int randMax = emptyCells.size();
      if (randMax <= 0) {
         System.out.println("Cannot spawn new tile");
         return;
      }
      int randI = (int) (Math.random() * randMax);
      Point randCell = emptyCells.get(randI);
      addTile(2, (int) randCell.getX(), (int) randCell.getY());
   }
   private void addTile(int num, int x, int y) {
      this.tiles.get(x).get(y).add(new Tile(num, getX(x), getY(y), getX(x), getY(y)));
   }
   public void paint(Graphics2D g2d) {
      g2d.setColor(new Color(195, 179, 153));
      g2d.fillRoundRect(offsetX, offsetY, backdropWidth, backdropHeight, arc, arc);
      g2d.setColor(new Color(205, 193, 180));
      for (int i = 0; i < gridWidth; i++) {
         for (int j = 0; j < gridHeight; j++) {
            int tileX = getX(i); 
            int tileY = getY(j); 
            // g2d.fillRect(tileX, tileY, tileDim, tileDim);
            g2d.fillRoundRect(tileX, tileY, tileDim, tileDim, arc, arc);
         }

      }
      for (int i = 0; i < gridWidth; i++) {
         for (int j = 0; j < gridHeight; j++) {
            for (Tile t : tiles.get(i).get(j)) {
               if (t.getShrinked()) {
                  t.shrinkedPaint(g2d);
               }
               else {
                  t.paint(g2d);
               }
               // If expanded, draw that one
               if (t.getExpanded()) {
                  t.expandedPaint(g2d);
               }
            }
         }
      }

   }

   private ArrayList<Tile> getTiles(int x, int y) {
      return this.tiles.get(x).get(y);
   }
   public void keyPressed(KeyEvent e) {
      System.out.println("Before:");
      printBoard();
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
         slideRight();
      }
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
         slideLeft();
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
         slideDown();
      }
      if (e.getKeyCode() == KeyEvent.VK_UP) {
         slideUp();
      }
      System.out.println("After:");
      printBoard();
      if (!getEmptyCells().isEmpty()) {
         spawnRandomTile();
      }

   }
   // return True if there is a match
   // return False if no match
   private boolean compareCellRight(int x, int y) {
      if (x == gridWidth - 1) {
         return false;
      }
      if (getCellTileNum(x, y) == getCellTileNum(x+1, y)) {
         return true;
      }
      return false;
      
   }

   // return True if there is a match
   // return false if no match
   private boolean compareCellDown(int x, int y) {
      if (y == gridHeight - 1) {
         return false;
      }
      if (getCellTileNum(x, y) == getCellTileNum(x, y+1)) {
         return true;
      }
      return false;
   }
   private int getCellTileNum(int x, int y) {
      //Assumes that the cell has a tile
      return getTiles(x,y).get(0).getNum();
   }
   public boolean isGameWon() {
      for (int x = 0; x < gridWidth; x++) {
         for (int y = 0; y < gridHeight; y++) {
            // We are only concerned once the tile actually merges
            // so we only grab the 0th. 
            if (getTiles(x,y).size() > 0 && getTiles(x,y).get(0).getNum() == 2048) {
               return true;
            }
         }
      }
      return false;
   }
   public boolean isGameLost() {
      // If there are no empty cells
      if (getEmptyCells().isEmpty()) {
         for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
               // if there is a not merged cell, return false
               if (getTiles(x,y).size() > 1) {
                  return false;
               }
               if (compareCellRight(x, y) || compareCellDown(x, y)) {
                  // if there is a match, return
                  return false;
               }
            }
         }
         //if no matches and board is full
         return true;
      }
      return false;
      
   } 
   private void printBoard() {
      for (int row = 0; row < gridHeight; row++) {
         for (int col = 0; col < gridWidth; col++) {
            System.out.print("[");
            for (Tile t : getTiles(col, row)) {
               System.out.print(t.num);
            }
            System.out.print("]");
         }
         System.out.println();
      }
   }

   public void slideRight() {
      //tiles.get(0).get(0).get(0).setDestY(getY(0));
      //tiles.get(0).get(0).get(0).setDestX(getX(3));
      
      //tiles.get(3).get(0).add(tiles.get(0).get(0).get(0));
      //tiles.get(0).get(0).remove(0);
      for (int y = 0; y < gridHeight; y++) {
         Stack<Point> tileStack = new Stack<Point>();
         Queue<Point> cellQueue = new LinkedList<Point>();
         for (int x = gridWidth - 1; x >= 0; x--) {

            calcMoves(x, y, tileStack, cellQueue);
         }
      }
   }
   public void slideLeft() {
      for (int y = 0; y < gridHeight; y++) {
         Stack<Point> tileStack = new Stack<Point>();
         Queue<Point> cellQueue = new LinkedList<Point>();
         for (int x = 0; x < gridWidth; x++) {
            calcMoves(x, y, tileStack, cellQueue);
         }
      }
   }
   public void slideDown() {
      for (int x = 0; x < gridWidth; x++) {
         Stack<Point> tileStack = new Stack<Point>();
         Queue<Point> cellQueue = new LinkedList<Point>();
         for (int y = gridHeight - 1; y >= 0; y--) {
            calcMoves(x, y, tileStack, cellQueue);
         } 
      }
   }
   public void slideUp() {
      for (int x = 0; x < gridWidth; x++) {
         Stack<Point> tileStack = new Stack<Point>();
         Queue<Point> cellQueue = new LinkedList<Point>();
         for (int y = 0; y < gridHeight; y++) {
            calcMoves(x, y, tileStack, cellQueue);
         }
      }
   }
   private void calcMoves(int x, int y, Stack<Point> tileStack, Queue<Point> cellQueue) {
      if (!getTiles(x,y).isEmpty()) {
         Tile tActive = getTiles(x, y).get(0);
         if (!tileStack.empty()) {
            // Check for merge
            Point p = tileStack.peek();

            Tile tStatic = getTiles((int) p.getX(), (int) p.getY()).get(0);
            //if merge is valid
            if (tStatic.getNum() == tActive.getNum()) {
               tileStack.pop();
               Point newCellXY = new Point(getX((int) p.getX()), getY((int) p.getY()));
               tActive.setDest(newCellXY);

               //upddate the grid
               getTiles((int) p.getX(), (int) p.getY()).add(tActive);
               getTiles(x, y).remove(0);
               tActive.setMerge(true);
               
               //update queue
               cellQueue.add(new Point(x, y));
               
               return;
            }
         }
         if (!(cellQueue.peek() == null)) {
            // First update the tile object
            Point newCell = cellQueue.remove();
            Point newCellXY = new Point(getX((int) newCell.getX()), getY((int) newCell.getY()));
            tActive.setDest(newCellXY);
            
            // Next update the grid, add to new cell and remove from old cell
            getTiles((int) newCell.getX(), (int) newCell.getY()).add(tActive);
            getTiles(x, y).remove(0);
            cellQueue.add(new Point(x, y));
            tileStack.add(newCell);
            return;


         }
         tileStack.add(new Point(x, y));
      }
      else {
         cellQueue.add(new Point(x, y));
      }

   }

   public void move() {
      // Keep track of all merge scores
      int mergeScore = 0;
      for (int i = 0; i < gridWidth; i++) {
         for (int j = 0; j < gridHeight; j++) {
            ArrayList<Tile> tileArray = tiles.get(i).get(j);
            for (Tile t : tileArray) {
               t.move();
            }
            // check for merges
            if (tileArray.size() >= 2 && tileArray.get(NEW).getMergeComplete())  {
               tileArray.remove(OLD);
               // Expand tile, the new is now in the place of old
               tileArray.get(OLD).setExpanded(true);
               tileArray.get(OLD).mergeNum();
               tileArray.get(OLD).setMergeComplete(false);
               printBoard();
               // update score
               game.scoreManager.addToScore(tileArray.get(OLD).getNum());
               mergeScore += tileArray.get(OLD).getNum();
            }
         }
      }
      // if mergescore is greater than 0, display
      if (mergeScore > 0) {
         game.scoreManager.setFadeNum(mergeScore);

      }
   }
   public int getX(int col) {
      return gap + col * (gap + tileDim) + offsetX; 
   }

   public int getY(int row) {
      return gap + row * (gap + tileDim) + offsetY;
   }

   public void newBoard() {
      clearBoard();
      spawnRandomTile();
      spawnRandomTile();
   }
   public void clearBoard() {

      tiles = new ArrayList<>(gridWidth);
      for (int i = 0; i < gridWidth; i++) {
         tiles.add(new ArrayList<ArrayList<Tile>>(gridHeight));
         for (int j = 0; j < gridHeight; j++) {
            tiles.get(i).add(new ArrayList<Tile>());
         }
      }
   }

   public int getOffsetX() {
      return this.offsetX;
   }   

   public int getOffsetY() {
      return this.offsetY;
   }

   public int getBackdropWidth() {
      return this.backdropWidth; 
   }

}
