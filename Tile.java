import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Hashtable;


public class Tile {
   int vel =  ((Board.gap + Board.tileDim) / 5);
   boolean merge = false;
   final int EXPANSION = 2;
   boolean mergeComplete = false;
   int destX;
   int destY;
   int x;
   int y;
   int num;
   boolean expanded = false;
   int expandedCount = 20;
   Hashtable<Integer, Color> colormap = new Hashtable<Integer, Color>();
   Color whiteText = new Color(249, 246, 242);
   Color blackText = new Color(119, 110, 101);

   public Tile(int num, int x, int y, int destX, int destY) {


      colormap.put(2, new Color(238, 228, 218));
      colormap.put(4, new Color(237, 224, 199));
      colormap.put(8, new Color(242, 177, 121));
      colormap.put(16, new Color(245, 149, 99));
      colormap.put(32, new Color(246, 124, 95));
      colormap.put(64, new Color(246, 94, 59));
      colormap.put(128, new Color(237, 207, 114));
      colormap.put(256, new Color(237, 204, 97));
      colormap.put(512, new Color(237, 200, 80));
      colormap.put(1024, new Color(237, 197, 63));
      colormap.put(2048, new Color(237, 194, 46));
      colormap.put(4096, new Color(60, 58, 25));
      this.x = x;
      this.y = y;
      this.num = num;
      this.destX = destX;
      this.destY = destY;
      

   }

   public void move() {
      if (x < destX) {
         x += vel;
      }
      if (x > destX) {
         x -= vel;
      }
      if (y < destY) {
         y += vel;
      }
      if (y > destY) {
         y -= vel;
      }

      // Check if merge flag is set 
      if (merge == true && x == destX && y == destY) {
         this.setMergeComplete(true);
         this.setMerge(false);
      }
   }

   public void paint(Graphics2D g2d) {
      g2d.setColor(getColor(num));
      g2d.fillRoundRect(x, y, Board.tileDim, Board.tileDim, Board.arc, Board.arc);
      // g2d.fillRect(x, y, Board.tileDim, Board.tileDim); 
      g2d.setColor(getFontColor(num));
      g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, calculateFontSize(num)));
      int fontXDelta = (int) ((Board.tileDim - calculateFontSize(num) * Integer.toString(num).length() / 1.6) / 2);
      int fontYDelta = (int) ((Board.tileDim - calculateFontSize(num)) / 1.7);
      g2d.drawString(Integer.toString(num), x + fontXDelta, y+Board.tileDim-fontYDelta);
      
   }

   public void expandedPaint(Graphics2D g2d) {
      g2d.setColor(getColor(num));
      int newX = (int) (x - Board.tileDim * 0.2);
      int newY = (int) (y - Board.tileDim * 0.2);
      g2d.fillRoundRect(newX, newY, (int) (Board.tileDim * 1.4), (int) (Board.tileDim * 1.4), Board.arc, Board.arc);
      expandedCount -= 1;
      if (expandedCount <= 0) {
         expanded = false;
         expandedCount = 20;
      }
   }
   public void setExpanded(boolean e) {
      expanded = e;
   }
   public boolean getExpanded() {
      return expanded;
   }

   public Color getColor(int num) {
      if (colormap.containsKey(num)) {
         return colormap.get(num);
      }
      return colormap.get(4096); 
   }
   public Color getFontColor(int num) {
      if (num <= 4) {
         return blackText;
      }
      else {
         return whiteText;
      }
   }
   public void setDestX(int x) {
      this.destX = x;
   }
   public void setDestY(int y) {
      this.destY = y;
   }
   public int calculateFontSize(int num) {
      if (num <= 8) {
         return 50;
      }

      else if (num <= 64) {
         return 50;
      }
      else if (num <= 128) {
         return 40;
      }
      else {
         return (int) (1.3*(Board.tileDim / (Integer.toString(num)).length()));
      }
   }
   public void setMerge(boolean m) {
      this.merge = m;
   }

   public boolean getMerge() {
      return this.merge;
   } 

   public void setMergeComplete(boolean m) {
      this.mergeComplete = m;
   }
   public boolean getMergeComplete() {
      return this.mergeComplete;
   }
   public void setDest(Point p) {
      this.setDestX((int) p.getX());
      this.setDestY((int) p.getY());
   }
   public int getNum() {
      return this.num;
   }
   
   public void mergeNum() {
      this.num *= 2;
   }
   public void adjustPosition(int x, int y) {
      if (this.destX == this.x && this.destY == this.y) {
         this.destX = x;
         this.destY = y;
      }
      this.x = x;
      this.y = y;
   }
}
