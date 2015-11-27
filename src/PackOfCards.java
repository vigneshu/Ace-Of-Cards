import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class PackOfCards {
	
	public static Boolean sameSuit(List<Card> cardOnBoard) {
		Iterator<Card> it = cardOnBoard.iterator();
		Card card = it.next();
		Suit prevSuit = card.getSuit();
		while(it.hasNext())
		{
			card = it.next();
			Suit suit = card.getSuit();
			if(prevSuit != suit)
			{
				return false;
			}
			
		}
		return true;
	}
	public static Integer getPlayerWithHighCard(LinkedHashMap<Player,Card> cardOnBoard,Suit s) {
		Iterator<Player> it = cardOnBoard.keySet().iterator();
		Rank maxRank = Rank.TWO;
		Player maxPlayer = null;
		while(it.hasNext())
		{
			Player p = it.next();
			Card card = cardOnBoard.get(p);
			Rank r = card.getRank();
			Suit s1 = card.getSuit();
			if(r.points>=maxRank.points && s1==s)
			{
				maxRank = r;
				maxPlayer = p;
			}
		}
		return maxPlayer.getPlayerNumber();
	}
}
