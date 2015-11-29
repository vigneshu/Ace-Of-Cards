import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Deck {
	private List<Card> deck = new ArrayList<Card>();
	  Deck()
	{
		for(Rank r : Rank.values())
		{
			for(Suit s : Suit.values())
				{
					if(r!=Rank.NONE && s!=Suit.NONE)
					{
						deck.add(new Card(r,s));
					}
				}
		}
		Collections.shuffle( deck );
	}
	public void shuffle()
	{
		Collections.shuffle( deck );
	}
	public List<Card> getDeck()
	{
		return deck;
	}
	public String toString()
	{
		Iterator it = deck.iterator();
		String str = "";
		while(it.hasNext())
		{
			Card card = (Card) it.next();
			str += String.valueOf(card.getRank().getValue()) + " " + card.getSuit().getValue() + "\n";
		}
		return str;
	}
	
}
