import java.applet.Applet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Player extends Applet {
	public List<Card> hand = new ArrayList<Card>();
	public List<Card> passedCards = new ArrayList<Card>();
	public HashMap<Integer,List<Card>> otherPlayerCards = new HashMap<Integer,List<Card>> ();
	private int playerNumber;
	private int playerCount;
	public int statePoints;
	
	Player(int pno) {
		this.playerNumber = pno;
		System.out.println("this.playerNumber "+this.playerNumber);
	}
	
	Player(int pno,int playerCount) {
		this.playerNumber = pno;
		this.playerCount = playerCount;
		for(int i=0;i<playerCount;i++)
		{
			if(i != playerNumber)
			{
				otherPlayerCards.put(i, null);
			}
		}
	}
	public int getPlayerNumber()
	{
		return this.playerNumber;
	}
	public int setPlayerNumber(int playernumber)
	{
		return this.playerNumber = playernumber;
	}
	public Card playTurn(LinkedHashMap<Player,Card> cardOnBoard)
	{
		return null;
	}
	
	 
}
