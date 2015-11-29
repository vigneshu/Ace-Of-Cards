import java.applet.Applet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Player{
	public State state;
	double discount = 0.05;//TODO change discount alpha and value
	double alpha = 0.05;
	private int playerNumber;
	private int playerCount;
	public static Features  stateFeatures = new Features();
	
	Player(int pno) {
		this.state = new State();
		this.playerNumber = pno;
	}
	
	Player(int pno,int playerCount) {
		this.state = new State();
		this.playerNumber = pno;
		this.playerCount = playerCount;
		for(int i=0;i<playerCount;i++)
		{
			if(i != playerNumber)
			{
				this.state.otherPlayerCards.put(i, new ArrayList<Card>());
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
	public Card playTurn()
	{
		HashMap<Card,Double> actionAndValue = getFutureQValue(state);
		Card maxAction =  actionAndValue.keySet().iterator().next();
		boolean removed = this.state.removeCardInHand(maxAction);
		return maxAction;
	}	
	public List<Card> remove(Suit s)
	{
		List<Card> filteredCards = new ArrayList<Card>();
		Iterator<Card> cardIterator = this.state.hand.iterator();
		while(cardIterator.hasNext())
		{
			Card c =  cardIterator.next();
			if(c.getSuit() == s)
			{
				 filteredCards.add(c);
			}
			
		}
		return filteredCards;
	}
	public List<Card> getSuitCards(Suit s)
	{
		List<Card> filteredCards = new ArrayList<Card>();
		Iterator<Card> cardIterator = this.state.hand.iterator();
		while(cardIterator.hasNext())
		{
			Card c =  cardIterator.next();
			if(c.getSuit() == s)
			{
				 filteredCards.add(c);
			}
			
		}
		return filteredCards;
	}
	public List<Card> getLegalActions(State s)
	{
		Collection<Card> cardsPlayed =  s.cardsOnBoard.values();
		if(cardsPlayed.size() == 0)
		{
			return this.state.hand;
		}
		else
		{
			
			Player p1 = s.cardsOnBoard.keySet().iterator().next();
			Card c1 = s.cardsOnBoard.get(p1);
			List<Card> suitCards = getSuitCards(c1.getSuit());
			if(suitCards.size() == 0)
			{
				return this.state.hand;
			}
			else
			{
				return suitCards;
			}
		}
	}


	public Double getQValue(State s,Card action)
	{
		Double sum = 0.0;
		HashMap<String,Double> features = stateFeatures.getFeatures(action);
		HashMap<String,Double> weights = stateFeatures.getWeights();
		Iterator<String> featureIterator = features.keySet().iterator();
		while(featureIterator.hasNext())
		{
			String feature = featureIterator.next();
			sum +=  features.get(feature) * weights.get(feature);
		}
		return sum;
	}	
	public HashMap<Card,Double> getFutureQValue(State s)
	{
		HashMap<Card,Double> actionAndValue = new HashMap<Card,Double>();
		Card action = new Card();;
		List<Card> legalCards = getLegalActions(s);
		if(legalCards.size() == 0) // Not required?
		{
			actionAndValue.put(new Card(Rank.NONE,Suit.NONE),0.0);
			return actionAndValue;
		}
		Double max = Double.NEGATIVE_INFINITY;
		Iterator<Card> legalCardIterator = legalCards.iterator();
		while(legalCardIterator.hasNext())
		{
			 Card actionCard = legalCardIterator.next();
			Double qVal = getQValue(s,actionCard);
			if(qVal>max)
			{
				max = qVal;
				action.setRank(actionCard.getRank()); 
				action.setSuit(actionCard.getSuit());
				
			}
		}
		actionAndValue.put(action,max);
		return actionAndValue;
		
	}

	public void updateWeights(State prevState ,Card action,State  state, double reward)
	{
		HashMap<Card,Double> actionAndValue = getFutureQValue(state);
//		System.out.println("state "+state);
//		System.out.println("prevState "+prevState);
//		System.out.println("actionAndValue "+actionAndValue);
		Card maxAction =  actionAndValue.keySet().iterator().next();
		Double maxVal = actionAndValue.get(maxAction);
		Double diff = ((reward + this.discount * maxVal) - getQValue(state,action));
		HashMap<String,Double> features = stateFeatures.getFeatures(action);
		HashMap<String,Double> weights = stateFeatures.getWeights();
		Iterator<String> featureIterator = features.keySet().iterator();
		while(featureIterator.hasNext())
		{
			String feature = featureIterator.next();
			Double weight = weights.get(feature) + alpha * diff *features.get(feature);
			weights.put(feature,weight);
		}
		stateFeatures.setWeights(weights);
		}
	
	public String toString()
	{
		return Integer.toString(this.playerNumber);
	}
	 
}
