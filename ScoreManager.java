import java.awt.Graphics2D;
import java.lang.String;
import java.awt.Color;
import java.awt.Font;

public class ScoreManager {

   private int bestScore = 1000;
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

   public ScoreManager(Board board) {
      this.board = board;


   }

   public void paint(Graphics2D g2d) {
      int cScoreNumWidth = getTextWidth(g2d, Integer.toString(currentScore), scoreNumFont);
      int bScoreNumWidth = getTextWidth(g2d, Integer.toString(bestScore), scoreNumFont);

      int cBoxWidth = getCBoxWidth(cScoreNumWidth);
      int bBoxWidth = getBBoxWidth(bScoreNumWidth);

      int scoreContainerX = getScoreContainerX(cBoxWidth, bBoxWidth);
      
      int cBoxX = scoreContainerX;
      int cBoxY = Game.containerOffsetY - 50;

      int cLabelWidth = getTextWidth(g2d, cLabel, scoreLabelFont);
      int cLabelX = (int) ((cBoxWidth - cLabelWidth) / 2 + cBoxX);
      int cLabelY = cBoxY + verticalBuffer + getTextHeight(g2d, scoreLabelFont);
      
      int cScoreNumX = (int) ((cBoxWidth - cScoreNumWidth) / 2 + cBoxX);
      int cScoreNumY = cLabelY + verticalBuffer + 25;
      
      int bBoxX = cBoxX + cBoxWidth + scoreGap;
      int bBoxY = cBoxY;

      int bLabelWidth = getTextWidth(g2d, bLabel, scoreLabelFont);
      int bLabelX = (int) ((bBoxWidth - bLabelWidth) / 2 + bBoxX);
      int bLabelY = bBoxY + verticalBuffer + getTextHeight(g2d, scoreLabelFont);

      int bScoreNumX = (int) ((bBoxWidth - bScoreNumWidth) / 2 + bBoxX);
      int bScoreNumY = bLabelY + verticalBuffer + 25;


      g2d.setColor(new Color(195, 179, 153));
      g2d.fillRoundRect(cBoxX, cBoxY, cBoxWidth, scoreBoxHeight, Board.arc, Board.arc);
      g2d.fillRoundRect(bBoxX, bBoxY, bBoxWidth, scoreBoxHeight, Board.arc, Board.arc);
      g2d.setFont(scoreLabelFont);
      g2d.setColor(new Color(249, 246, 242));
      g2d.drawString(cLabel, cLabelX, cLabelY);
      g2d.drawString(bLabel, bLabelX, bLabelY);

      g2d.setFont(scoreNumFont);
      g2d.drawString(Integer.toString(currentScore), cScoreNumX, cScoreNumY);
      g2d.drawString(Integer.toString(bestScore), bScoreNumX, bScoreNumY);



      // int cScoreTextX = cScoreBoxX + horizontalBuffer;
      // int cScoreTextY = cScoreBoxY + cScoreBoxHeight - verticalBuffer; 
// 
      // int bScoreBoxX = cScoreBoxX + cScoreBoxWidth + scoreGap;
      // int bScoreBoxY = cScoreBoxY;
      // int bScoreBoxWidth = bScoreNumWidth + 2 * horizontalBuffer;
     //  
      // int bScoreTextX = bScoreBoxX + horizontalBuffer;
      // int bScoreTextY = bScoreBoxY + bScoreBoxHeight - verticalBuffer;
// 
      // g2d.setColor(new Color(195, 179, 153));
      // g2d.fillRoundRect(cScoreBoxX, cScoreBoxY, cScoreBoxWidth, cScoreBoxHeight, Board.arc, Board.arc);
      // g2d.fillRoundRect(bScoreBoxX, bScoreBoxY, bScoreBoxWidth, bScoreBoxHeight, Board.arc, Board.arc);
// 
      // g2d.setColor(new Color(249, 246, 242));
      // g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
// 
      // g2d.drawString(Integer.toString(currentScore), cScoreTextX, cScoreTextY);
      // g2d.drawString(Integer.toString(bestScore), bScoreTextX, bScoreTextY);

      

   }
   public void setBestScore(int num) {
      this.bestScore = num;
   }

   public void setCurrentScore(int num) {
      this.currentScore = num;
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
