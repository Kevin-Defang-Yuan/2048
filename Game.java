import javax.swing.JPanel;
import java.lang.Thread;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.lang.String;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;

// Responsive Branch
public class Game extends JPanel {
    
    public Board board;
    public ScoreManager scoreManager;
    int gameWidth;
    int gameHeight;
    public static int containerOffsetY = 150;
    String plainText = "Join the numbers and get to the ";
    public Game() {

       Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
       gameWidth = (int) dim.getWidth();
       gameHeight = (int) dim.getHeight();

       addKeyListener(new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}

          @Override
          public void keyPressed(KeyEvent e) {
             board.keyPressed(e); 
             System.out.println("Key Pressed!");
             System.out.println(e.getKeyCode());
          }

       });
       setFocusable(true);
    }

    public static void main(String args[]) throws InterruptedException {
       JFrame frame = new JFrame(); 
       Game game = new Game();
       game.board = new Board(game.gameWidth, game.gameHeight);
       game.scoreManager = new ScoreManager(game.board);
       game.setPreferredSize(new Dimension(game.gameWidth, game.gameHeight));
       frame.add(game);
       frame.pack();
       frame.setVisible(true);
       frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

       while (true) {
          game.repaint();
          game.move();
          Thread.sleep(3); 
       }
    }    


    public void move() {
       board.move();
    }

    public void paint(Graphics g) {

       Graphics2D g2d = (Graphics2D) g;
       g2d.setColor(new Color(251, 248, 241));
       g2d.fillRect(0, 0, gameWidth, gameHeight); 
       g2d.setColor(new Color(119, 110, 101));
       g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
       g2d.drawString("2048", this.board.getOffsetX(), containerOffsetY);
       g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
       g2d.drawString(plainText, this.board.getOffsetX(), containerOffsetY + 40);
       g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
       int plainTextSize = g2d.getFontMetrics().stringWidth(plainText);
       g2d.drawString("2048 tile!", this.board.getOffsetX() + plainTextSize - 20, containerOffsetY + 40); 
       this.board.paint(g2d);
       this.scoreManager.paint(g2d);

    }
}
