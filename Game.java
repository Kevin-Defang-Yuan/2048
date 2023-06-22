import javax.swing.JPanel;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.lang.Thread;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.lang.String;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;

// gameOver Branch
public class Game extends JPanel {
    static final int PLAY = 0;
    static final int LOSE = 1;
    static final int WIN = 2;
    static final int CONTINUE = 3;
    public int mode = PLAY;
    public Board board = new Board(this);
    public ScoreManager scoreManager;
    int gameWidth;
    int gameHeight;
    static int containerOffsetY;
    String plainText = "Join the numbers and get to the ";
    String gameOver = "Game over!";
    String youWin = "You win!";
    double containerOffsetPercent = 0.20;
    public int buttonHeight = 30;
    public int buttonWidth = 100;
    public int loseTryAgainButtonX;
    public int loseTryAgainButtonY;
    public int winTryAgainButtonX;
    public int winTryAgainButtonY;
    public int winKeepGoingButtonX;
    public int winKeepGoingButtonY;
    public Game() {

       Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
       gameWidth = (int) dim.getWidth();
       gameHeight = (int) dim.getHeight();
       containerOffsetY = (int) (dim.getHeight() * containerOffsetPercent);

       addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
             if (mode == LOSE) {
                int x = loseTryAgainButtonX;
                int y = loseTryAgainButtonY;
                if (e.getX() >= x && e.getX() <= x + buttonWidth && e.getY() >= y && e.getY() <= y + buttonHeight) {
                   board.newBoard();
                   mode = PLAY;
                   scoreManager.setCurrentScore(0);
                }
             }
             if (mode == WIN) {
                int x = winKeepGoingButtonX;
                int y = winKeepGoingButtonY;
                if (e.getX() >= x && e.getX() <= x + buttonWidth && e.getY() >= y && e.getY() <= y + buttonHeight) {
                   mode = CONTINUE;
                }
                
                x = winTryAgainButtonX;
                y = winTryAgainButtonY;
                if (e.getX() >= x && e.getX() <= x + buttonWidth && e.getY() >= y && e.getY() <= y + buttonHeight) {
                   mode = PLAY;
                   scoreManager.setCurrentScore(0);
                   board.newBoard();
                }


             }
          }

       });
       addKeyListener(new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}

          @Override
          public void keyPressed(KeyEvent e) {
             if (mode != LOSE && mode != WIN) {
                board.keyPressed(e);
             }
          }

       });
       setFocusable(true);

       addComponentListener(new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
             Dimension window = getBounds().getSize();
             board.resize((int) window.width, (int) window.height);
          }
       });
    }

    public static void main(String args[]) throws InterruptedException {
       JFrame frame = new JFrame(); 
       Game game = new Game();
       game.scoreManager = new ScoreManager(game.board);
       game.setPreferredSize(new Dimension(game.gameWidth, game.gameHeight));
       frame.add(game);
       frame.pack();
       frame.setVisible(true);
       frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

       while (true) {
          if (game.mode != CONTINUE && game.mode != LOSE) {
             if (game.board.isGameWon()) {
                game.mode = Game.WIN;
             }
          }

          if (game.mode == CONTINUE || game.mode == PLAY) {
             if (game.board.isGameLost()) {
                game.mode = LOSE;
             }

          }
          game.repaint();
          game.move();
          Thread.sleep(2); 
       }
    }    


    public void move() {
       board.move();
       scoreManager.move();
    }

    public void paint(Graphics g) {

       Dimension window = this.getBounds().getSize();
       //this.containerOffsetY = (int) (window.getHeight() * containerOffsetPercent);
       containerOffsetY = (int) ((window.getHeight() - board.backdropHeight) / 5 * 2.5);
       Graphics2D g2d = (Graphics2D) g;
       g2d.setColor(new Color(251, 248, 241));
       g2d.fillRect(0, 0, gameWidth, gameHeight); 
       g2d.setColor(new Color(119, 110, 101));
       g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
       g2d.drawString("2048", this.board.getOffsetX(), containerOffsetY);
       g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
       g2d.drawString(plainText, this.board.getOffsetX(), containerOffsetY + 50);
       g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
       int plainTextSize = g2d.getFontMetrics().stringWidth(plainText);
       g2d.drawString("2048 tile!", this.board.getOffsetX() + plainTextSize - 20, containerOffsetY + 50); 
       this.board.paint(g2d);
       this.scoreManager.paint(g2d);
       // Draw GameLose Screen
       if (mode == LOSE) {
          // draw overlay
          g2d.setColor(new Color(245, 226, 207, 150));
          g2d.fillRoundRect(board.getOffsetX(), board.getOffsetY(), board.getBackdropWidth(), board.getBackdropWidth(), Board.arc, Board.arc);
          //draw text
          g2d.setColor(Tile.blackText);
          g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
          int gameOverTextWidth = g2d.getFontMetrics().stringWidth(gameOver);
          int gameOverTextX = (int) ((board.getBackdropWidth() - gameOverTextWidth) / 2) + board.getOffsetX(); 
          int gameOverTextY = (int) ((board.getBackdropWidth() / 2 + board.getOffsetY() + 30));
          g2d.drawString(gameOver, gameOverTextX, gameOverTextY);
          // draw try again button
          loseTryAgainButtonX = (int) ((board.getBackdropWidth() - buttonWidth) / 2) + board.getOffsetX();
          loseTryAgainButtonY = (int) (gameOverTextY + 70);
          g2d.fillRoundRect(loseTryAgainButtonX, loseTryAgainButtonY, buttonWidth, buttonHeight, Board.arc, Board.arc);
          // Draw try again text
          g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
          g2d.setColor(Tile.whiteText);
          g2d.drawString("Try Again", loseTryAgainButtonX + 13, loseTryAgainButtonY + 20);

       }

       // Draw GameWin Screen
       if (mode == WIN) {
          g2d.setColor(new Color(229, 201, 68, 150));
          g2d.fillRoundRect(board.getOffsetX(), board.getOffsetY(), board.getBackdropWidth(), board.getBackdropWidth(), Board.arc, Board.arc);
          //draw text
          g2d.setColor(Tile.whiteText);
          g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
          int youWinTextWidth = g2d.getFontMetrics().stringWidth(youWin);
          int youWinTextX = (int) ((board.getBackdropWidth() - youWinTextWidth) / 2) + board.getOffsetX(); 
          int youWinTextY = (int) ((board.getBackdropWidth() / 2 + board.getOffsetY() + 30));
          g2d.drawString(youWin, youWinTextX, youWinTextY);
          // draw keep going button
          winKeepGoingButtonX = (int) ((board.getBackdropWidth() - buttonWidth) / 2) + board.getOffsetX() - 60;
          winKeepGoingButtonY = (int) (youWinTextY + 70);
          g2d.setColor(Tile.blackText);
          g2d.fillRoundRect(winKeepGoingButtonX, winKeepGoingButtonY, buttonWidth, buttonHeight, Board.arc, Board.arc);
          // Draw try again text
          g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
          g2d.setColor(Tile.whiteText);
          g2d.drawString("Keep Going", winKeepGoingButtonX + 5, winKeepGoingButtonY + 20);
          // draw try again button
          winTryAgainButtonX = (int) ((board.getBackdropWidth() - buttonWidth) / 2) + board.getOffsetX() + 60;
          winTryAgainButtonY = (int) (youWinTextY + 70);
          g2d.setColor(Tile.blackText);
          g2d.fillRoundRect(winTryAgainButtonX, winTryAgainButtonY, buttonWidth, buttonHeight, Board.arc, Board.arc);
          // Draw try again text
          g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
          g2d.setColor(Tile.whiteText);
          g2d.drawString("Try Again", winTryAgainButtonX + 12, winTryAgainButtonY + 20);

       }
    }
    public int getMode() {
       return this.mode;
    }
    public void setMode(int m) {
       this.mode = m;
    }
    public int getLoseTryAgainButtonX() {
       return loseTryAgainButtonX;
    }
    public int getLoseTryAgainButtonY() {
       return loseTryAgainButtonY;
    }
    public int getWinTryAgainButtonX() {
       return winTryAgainButtonX;
    }
    public int getWinTryAgainButtonY() {
       return winTryAgainButtonY;
    }
    public int getWinKeepGoingButtonX() {
       return winKeepGoingButtonX;
    }
    public int getWinKeepGoingButtonY() {
       return winKeepGoingButtonY;
    }
}
