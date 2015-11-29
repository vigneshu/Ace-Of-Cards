import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * @author Vignesh
 *
 */
public class main extends JPanel {
	static int playerCount = 4;
	private static Player[] players = new Player[playerCount];
	static Deck cards = new Deck();
	static JFrame jp1 = new JFrame();
	List<Card> cardsPassed = new ArrayList<Card>();
	static Boolean gameEnd = false;

    
	
	
	public static void main(String[] args) {
		int perPlayer = 52 / playerCount;
		for (int i = 0; i < playerCount; i++) {
			Player player = new Player(i,playerCount);
			player.state.hand = new ArrayList<Card>(cards.getDeck().subList(i * perPlayer, (i * perPlayer + perPlayer)));
			// jp1.getContentPane().add(BorderLayout.CENTER, player);
			player.setPlayerNumber(i);
			players[i] = player;
			
		}
		
		int round = 1;
		while(!gameEnd)//Until game ends
		{
			System.out.println("round "+round);
			round++;
			LinkedHashMap<Player,Card> cardOnBoard = new LinkedHashMap<Player,Card>();
			Suit prevSuit = null;
			State[] state = new State[playerCount];
			Card[] actions = new Card[playerCount];
			Double[] reward = new Double[playerCount];
			for(int i =0;i<players.length;i++)// Until round ends
			{
				players[i].state.cardsOnBoard = new LinkedHashMap<Player,Card>(cardOnBoard);
				state[i] = new State(players[i].state);
				
//				System.out.println("State before play "+players[i].state.toString());
				Card c1 = players[i].playTurn();//TODO write playTurn
				Suit suit = c1.getSuit();
				System.out.println("Player " +i+" Card Played: " +c1);
				actions[i] = new Card(c1.getRank(),suit);
				cardOnBoard.put(players[i],c1);
				players[i].state.cardsOnBoard = new LinkedHashMap<Player,Card>(cardOnBoard);
				
				
				if(prevSuit != null)
				{
					if(suit!= prevSuit)
					{
						System.out.println("Card cut  by player "+ i+ "Going tonext round.Played card "+c1.toString());
						break;
					}
				}
				prevSuit = suit;
				
			}
			if(!gameEnd)
			{
				reward = decideCards(cardOnBoard);
				for(int i =0;i<cardOnBoard.size();i++)//Update weights for every playerplayed
				{
//					System.out.println("PlayerNumber : "+i+" ");
//					System.out.println("prev State "+state[i].toString()+" ");
//					System.out.println("prev actions "+actions[i].toString()+" ");
//					System.out.println("next state "+players[i].state.toString()+" ");
//					System.out.println("reward[i] "+reward[i]+"\n");
					players[i].updateWeights(state[i],actions[i],players[i].state,reward[i]);//TODO update reward[i]
	//				System.out.println("\n");
				}
			}
			if(gameEnd)
			{
				System.out.println("Weights after calculation "+players[0].stateFeatures.getWeights());
			}
		}
		
		jp1.add(new main());
		jp1.setSize(new Dimension(1500, 1000));
		jp1.setVisible(true);

		// Graphics g = getGraphics();
		// Card.drawCard(g, new Card(Card.Rank.ACE,Card.Suit.CLUBS), 50, 50);
	}
	public static Double[] decideCards(LinkedHashMap<Player,Card> cardOnBoard)
	{
		Collection<Card> cardsPlayed = cardOnBoard.values();
		boolean sameSuit = PackOfCards.sameSuit(cardsPlayed);
		Double[] reward = new Double[playerCount];
		if(sameSuit)
		{
			for (int i = 0;i< players.length;i++)
			{
				players[i].state.passedCards.addAll(cardsPlayed);
				reward[i] = 10.0;
				if(players[i].state.hand.size() == 0)
				{
					gameEnd = true;
				}
			}
		}
		else
		{
			Suit s = cardsPlayed.iterator().next().getSuit();
			Integer playerNumber = PackOfCards.getPlayerWithHighCard(cardOnBoard,s);
			players[playerNumber].state.hand.addAll(cardsPlayed); 
			for (Integer i = 0;i< players.length;i++)
			{
//				HashMap<Integer,List<Card>> otherCards = players[i].state.otherPlayerCards;
//				if(otherCards.containsKey(i))
//				{
//					List<Card> cards = otherCards.get(i);
//					cards.addAll(playerNumber, cardsPlayed);
//					reward[i] = 15.0;
//				}
				if(!playerNumber.equals(i))
				{
					HashMap<Integer,List<Card>> otherCards = players[i].state.otherPlayerCards;
					List<Card> cards = otherCards.get(playerNumber);
					cards.addAll( cardsPlayed);
					if(players[i].state.hand.size() == 0)
					{
						gameEnd = true;
					}
				}
			}	
			Iterator<Player> playerIt = cardOnBoard.keySet().iterator();
			int i = 0;
			while(playerIt.hasNext())
			{
				Player p = playerIt.next();
				if(playerNumber.equals(p.getPlayerNumber()))
				{
					reward[i] = -10.0;
				}
				else
				{
					reward[i] = 15.0;
				}
				i++;
			}
		}
		return reward;
	}
	public void printPlayers(Player[] p)
	{
		for(int i = 0;i<p.length;i++)
		{
			System.out.println(p.toString()+ " ");
		}
	}
	public void printCards(Card[] c)
	{
		for(int i = 0;i<c.length;i++)
		{
			System.out.println(c.toString()+ " ");
		}
	}

	public void paint(Graphics g) {

		
		for (Integer i = 0;i< players.length;i++) {

			Player player = players[i];
			List<Card> hand = player.state.hand;
			int playerNumber = player.getPlayerNumber();
			// The paint method shows the message at the bottom of the
			// canvas, and it draws all of the dealt cards spread out
			// across the canvas.
			Font bigFont = new Font("Serif", Font.BOLD, 14);
			Font smallFont = new Font("SansSerif", Font.PLAIN, 12);
			g.setFont(bigFont);
			g.setColor(Color.green);
			g.drawString("Change this message", 10, getSize().height - 10);

			// // Draw labels for the two sets of cards.
			//
			g.drawString("Dealer's Cards:", 10, 23);
			g.drawString("Your Cards:", 10, 153);

			// Draw dealer's cards. Draw first card face down if
			// the game is still in progress, It will be revealed
			// when the game ends.

			g.setFont(smallFont);
			// if (gameInProgress)
			// drawCard(g, null, 10, 30);
			// else
			// drawCard(g, dealerHand.getCard(0), 10, 30);
			Iterator handIt = hand.iterator();
			int j = 0;
			while (handIt.hasNext()) {
//				g.drawString(((Card) handIt.next()).getSuit().getValue() + "  ", i * 10, playerNumber * 70);
				 drawCard(g, (Card)handIt.next(), 10 + j * 90, 120*playerNumber);
				j++;
			}
//			jp1.getContentPane().add(g);
			// Draw the user's cards.

			// for (int i = 0; i < playerHand.getCardCount(); i++)
			// drawCard(g, playerHand.getCard(i), 10 + i * 90, 160);

		}

		// The paint method shows the message at the bottom of the
		// canvas, and it draws all of the dealt cards spread out
		// across the canvas.
		// Font bigFont = new Font("Serif", Font.BOLD, 14);
		// Font smallFont = new Font("SansSerif", Font.PLAIN, 12);
		// g.setFont(bigFont);
		// g.setColor(Color.green);
		// g.drawString("Change this message", 10, getSize().height - 10);
		//
		// // Draw labels for the two sets of cards.
		//
		// g.drawString("Dealer's Cards:", 10, 23);
		// g.drawString("Your Cards:", 10, 153);

		// Draw dealer's cards. Draw first card face down if
		// the game is still in progress, It will be revealed
		// when the game ends.

		// g.setFont(smallFont);
		// if (gameInProgress)
		// drawCard(g, null, 10, 30);
		// else
		// drawCard(g, dealerHand.getCard(0), 10, 30);
		// Iterator it = hand.iterator();
		// int i = 0;
		// while(it.hasNext())
		// {
		// drawCard(g, (Card)it.next(), 10 + i * 90, 60*playerNumber);
		// i++;
		//
		// }

		// Draw the user's cards.

		// for (int i = 0; i < playerHand.getCardCount(); i++)
		// drawCard(g, playerHand.getCard(i), 10 + i * 90, 160);

	} // end paint();
	 void drawCard(Graphics g, Card card, int x, int y) {
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
