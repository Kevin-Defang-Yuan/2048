import java.awt.Graphics2D;
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
   int backdropWidth = gridWidth * (tileDim + gap) + gap;
   int backdropHeight = gridHeight * (tileDim + gap) + gap;
   ArrayList<ArrayList<ArrayList<Tile>>> tiles = new ArrayList<>(gridWidth);
   public Board(int gameWidth, int gameHeight) {
      this.offsetX = (int) ((gameWidth - backdropWidth) / 2);
      this.offsetY = (int) ((gameHeight - backdropHeight) / 5 * 2.5);
      clearBoard();
      addTile(2, 3, 0);
      addTile(2, 1, 0);
      addTile(4, 0, 0);
      //tiles.get(0).get(0).add(new Tile(2, getX(0), getY(0), getX(0), getY(0)));

      //tiles.get(2).get(0).add(new Tile(2, getX(2), getY(0), getX(2), getY(0)));
      // tiles.get(2).get(0).add(new Tile(4, getX(2), getY(0), getX(2), getY(0)));
      //tiles[0][0].add(new Tile(2, getX(0), getY(0), getX(0), getY(0)));
      
      //tiles[0][1] = new Tile(4, getX(0), getY(1));
      //tiles[0][2] = new Tile(8, getX(0), getY(2));
      //tiles[0][3] = new Tile(16, getX(0), getY(3));
      //tiles[1][0] = new Tile(32, getX(1), getY(0));
      //tiles[1][1] = new Tile(64, getX(1), getY(1));
      //tiles[1][2] = new Tile(128, getX(1), getY(2));
      //tiles[1][3] = new Tile(256, getX(1), getY(3));
      //tiles[2][0] = new Tile(512, getX(2), getY(0));
      //tiles[2][1] = new Tile(1024, getX(2), getY(1));
      //tiles[2][2] = new Tile(2048, getX(2), getY(2));
      //tiles[2][3] = new Tile(4096, getX(2), getY(3));
      //tiles[3][0] = new Tile(8192, getX(3), getY(0));
      //tiles[3][1] = new Tile(16384, getX(3), getY(1));
      //tiles[3][2] = new Tile(131072, getX(3), getY(2));
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
               t.paint(g2d);
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
            }
         }
      }
   }
   public int getX(int col) {
      return gap + col * (gap + tileDim) + offsetX; 
   }

   public int getY(int row) {
      return gap + row * (gap + tileDim) + offsetY;
   }

   public void clearBoard() {
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
