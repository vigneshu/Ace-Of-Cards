import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class State {
	public List<Card> hand;
	public List<Card> passedCards;
	public HashMap<Integer, List<Card>> otherPlayerCards;
	public HashMap<Integer, List<Suit>> otherPlayerEmptySuits;
	public int statePoints;
	public int playerNumber;
	public int playerCount;
	public double round;
	public Card cardPlayed;
	LinkedHashMap<Player, Card> cardsOnBoard;

	State(State s) {
		this.hand = new ArrayList<Card>(s.hand);
		this.passedCards = new ArrayList<Card>(s.passedCards);
		this.otherPlayerCards = new HashMap<Integer, List<Card>>(s.otherPlayerCards);
		this.cardsOnBoard = new LinkedHashMap<Player, Card>(s.cardsOnBoard);
		this.otherPlayerEmptySuits = new HashMap<Integer, List<Suit>>(s.otherPlayerEmptySuits);
		this.round = s.round;
		this.cardPlayed = s.cardPlayed;
		this.statePoints = s.statePoints;
		this.playerNumber = s.playerNumber;
	}
	State() {
		this.hand = new ArrayList<Card>();
		this.passedCards = new ArrayList<Card>();
		this.otherPlayerCards = new HashMap<Integer, List<Card>>();
		this.cardsOnBoard = new LinkedHashMap<Player, Card>();
		this.otherPlayerEmptySuits = new HashMap<Integer, List<Suit>>();
		this.round = 0;
		this.cardPlayed = new Card();
	}
	public String toString() {
		String str = " ";
		for(Card c:this.hand)
		{
			str = str + c.toString() + " ";
		}
		str = str +"playerNumber : "+playerNumber+"otherPlayerEmptySuits "+otherPlayerEmptySuits;
		return str;
	}

	public boolean removeCardInHand(Card c1) {
		Iterator<Card> it = this.hand.iterator();
		boolean present = false;
		while (it.hasNext()) {
			Card c2 = it.next();
			if (c1.equals(c2)) {
				present = true;
				it.remove();
				break;
			} 
			
		}
		return present;
	}
	
	public Double getHighCard() {
		Iterator<Card> it = this.hand.iterator();
		double max = Rank.NONE.getValue();
		while (it.hasNext()) {
			Card c2 = it.next();
			if (c2.getRank().getValue()>max) {
				max = c2.getRank().getValue();
			} 
			
		}
		return max;
	}
}
