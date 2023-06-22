import java.awt.Graphics2D;
import java.lang.String;
import java.awt.Color;
import java.awt.Font;

public class ScoreManager {

   private int bestScore = 100;
   private int currentScore = 0;
   private int scoreGap = 5;
   private int horizontalBuffer = 20;
   private int verticalBuffer = 5;
   private int scoreBoxHeight = 60;
   private int cBoxWidth = 60;
   private int bBoxWidth = 60;
   private String cLabel = "SCORE";
   private String bLabel = "BEST";
   private Font scoreLabelFont = new Font(Font.SANS_SERIF, Font.BOLD, 10);
   private Font scoreNumFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
   private Board board;
   private boolean cFadeAway = false;
   private boolean bFadeAway = false;
   
   private int fadeAwayY; 
   private String fadeNum;
   private int scoreNumY;
   private final int fadeAwayOffset = 10;
   private final int fadeBoundaryY = 0;
   private final int fadeVelocity = 1;

   public ScoreManager(Board board) {
      this.board = board;


   }

   public void setFadeNum(int n) {
      this.fadeNum = "+" + Integer.toString(n);
   }
   public void move() {
      if (bFadeAway || cFadeAway) {
         fadeAwayY -= fadeVelocity;
      }

      if (fadeAwayY <= fadeBoundaryY) {
         bFadeAway = false;
         cFadeAway = false;
      }

   }
   public void paint(Graphics2D g2d) {
      int cScoreNumWidth = getTextWidth(g2d, Integer.toString(currentScore), scoreNumFont);
      int bScoreNumWidth = getTextWidth(g2d, Integer.toString(bestScore), scoreNumFont);

      int cBoxWidth = getCBoxWidth(cScoreNumWidth);
      int bBoxWidth = getBBoxWidth(bScoreNumWidth);

      int scoreContainerX = getScoreContainerX(cBoxWidth, bBoxWidth);
      
      int cBoxX = scoreContainerX;

      int cLabelWidth = getTextWidth(g2d, cLabel, scoreLabelFont);
      int cLabelX = (int) ((cBoxWidth - cLabelWidth) / 2 + cBoxX);
      
      int cScoreNumX = (int) ((cBoxWidth - cScoreNumWidth) / 2 + cBoxX);
      
      int bBoxX = cBoxX + cBoxWidth + scoreGap;

      int bLabelWidth = getTextWidth(g2d, bLabel, scoreLabelFont);
      int bLabelX = (int) ((bBoxWidth - bLabelWidth) / 2 + bBoxX);

      int bScoreNumX = (int) ((bBoxWidth - bScoreNumWidth) / 2 + bBoxX);
      
      int boxY = Game.containerOffsetY - 50;
      int labelY = boxY + verticalBuffer + getTextHeight(g2d, scoreLabelFont); 
      scoreNumY = labelY + verticalBuffer + 25;

      g2d.setColor(new Color(195, 179, 153));
      g2d.fillRoundRect(cBoxX, boxY, cBoxWidth, scoreBoxHeight, Board.arc, Board.arc);
      g2d.fillRoundRect(bBoxX, boxY, bBoxWidth, scoreBoxHeight, Board.arc, Board.arc);
      g2d.setFont(scoreLabelFont);
      g2d.setColor(new Color(249, 246, 242));
      g2d.drawString(cLabel, cLabelX, labelY);
      g2d.drawString(bLabel, bLabelX, labelY); 

      g2d.setFont(scoreNumFont);
      g2d.drawString(Integer.toString(currentScore), cScoreNumX, scoreNumY);
      g2d.drawString(Integer.toString(bestScore), bScoreNumX, scoreNumY);
      
      // Draw the fadeaways
      if (cFadeAway) {
         g2d.setColor(getFadeColor());
         g2d.drawString(fadeNum, cScoreNumX - fadeAwayOffset, fadeAwayY);
      }

      if (bFadeAway) {
         g2d.setColor(getFadeColor());
         g2d.drawString(fadeNum, bScoreNumX - fadeAwayOffset, fadeAwayY);
      }

   }

   private Color getFadeColor() {
      // Generates alpha based on Y
      int alpha = fadeAwayY;
      return new Color(0, 0, 0, alpha);

   }
   public void setBestScore(int num) {
      this.bestScore = num;
   }

   public void setCurrentScore(int num) {
      this.currentScore = num;
   }
   
   
   public void addToScore(int num) {
      currentScore += num;
      if (currentScore > bestScore) {
         bestScore = currentScore;
         // set best fadeaway flag to true
         bFadeAway = true;
      }
      // Set fadeaway flag to true
      cFadeAway = true; 
      fadeAwayY = scoreNumY; 
   }
   private int getTextWidth(Graphics2D g2d, String txt, Font font) {
      g2d.setFont(font);
      return g2d.getFontMetrics().stringWidth(txt);
   }
   private int getTextHeight(Graphics2D g2d, Font font) {
      g2d.setFont(font);
      return g2d.getFontMetrics().getHeight();
   }
   private int getScoreContainerX(int cBoxWidth, int bBoxWidth) {
      int rightBoundaryX = (int) (board.getOffsetX() + board.getBackdropWidth());
      return rightBoundaryX - cBoxWidth - bBoxWidth - scoreGap; 
       
   }

   private int getCBoxWidth(int scoreNumWidth) {
      if (scoreNumWidth + 2 * horizontalBuffer > cBoxWidth) {
         cBoxWidth = scoreNumWidth + 2 * horizontalBuffer;
      }

      return this.cBoxWidth;
   }

   private int getBBoxWidth(int scoreNumWidth) {
      if (scoreNumWidth + 2 * horizontalBuffer > bBoxWidth) {
         bBoxWidth = scoreNumWidth + 2 * horizontalBuffer;
      }

      return this.bBoxWidth; 
   }
}
