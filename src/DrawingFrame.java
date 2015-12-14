import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JFrame;

public class DrawingFrame extends JFrame  {
	Player[] players;
	LinkedHashMap<Player, Card> cardsOnBoard;
	DrawingFrame(Player[] players)
	{
		this.players = players;
		this.cardsOnBoard = new LinkedHashMap<Player, Card> ();
	}
	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getForeground());
		for (Integer i = 0;i< players.length;i++) {
			Player player = players[i];
			List<Card> hand = player.state.hand;
			int playerNumber = player.getPlayerNumber(); 
			
			Font bigFont = new Font("Serif", Font.BOLD, 14);
			Font smallFont = new Font("SansSerif", Font.PLAIN, 12);
			g.setFont(bigFont);
			g.setColor(Color.green);
			g.drawString("Change this message", 10, getSize().height - 10);

			g.setFont(smallFont);
			Iterator handIt = hand.iterator();
			int j = 0;
			while (handIt.hasNext()) {
				drawCard(g, (Card)handIt.next(), 10 + j * 90, 110*(playerNumber+1));
				j++;
			}
			Iterator cardsOnBoardIt = this.cardsOnBoard.keySet().iterator();
			int m = 0;
			while (cardsOnBoardIt.hasNext()) {
				Player p = (Player) cardsOnBoardIt.next();
				Card c = (Card) this.cardsOnBoard.get(p );
				drawCard(g,c, 10 + m * 90, 110*(players.length+1));
				m++;
			}
			
		}
		

		}

	static void drawCard(Graphics g, Card card, int x, int y) {
        // Draws a card as a 80 by 100 rectangle with
        // upper left corner at (x,y).  The card is drawn
        // in the graphics context g.  If card is null, then
        // a face-down card is drawn.  (The cards are 
        // rather primitive.)
   if (card == null) {  
          // Draw a face-down card
      g.setColor(Color.blue);
      g.fillRect(x,y,80,100);
      g.setColor(Color.white);
      g.drawRect(x+3,y+3,73,93);
      g.drawRect(x+4,y+4,71,91);
   }
   else {
      g.setColor(Color.white);
      g.fillRect(x,y,80,100);
      g.setColor(Color.gray);
      g.drawRect(x,y,79,99);
      g.drawRect(x+1,y+1,77,97);
      if (card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS)
         g.setColor(Color.red);
      else
         g.setColor(Color.black);
      g.drawString(String.valueOf(card.getRank().getValue()), x + 10, y + 30);
      g.drawString("of", x+ 10, y + 50);
      g.drawString(card.getSuit().getValue(), x + 10, y + 70);
   }
	 }  // end drawCard()


}
