import java.applet.Applet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Player extends Applet {
	public State state = new State();
	double discount = 0.05;//TODO change discount alpha and value
	double alpha = 0.05;
	private int playerNumber;
	private int playerCount;
	public static Features  stateFeatures = new Features();
	
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
				this.state.otherPlayerCards.put(i, null);
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
		return null;
	}	
	
	public List<Card> getSuitCards(List<Card> cardsPlayed,Suit s)
	{
		List<Card> filteredCards = new ArrayList<Card>();
		Iterator<Card> cardIterator = cardsPlayed.iterator();
		while(cardIterator.hasNext())
		{
			Card c = cardIterator.next();
			if(c.getSuit() == s)
			{
				 filteredCards.add(c);
			}
			
		}
		return filteredCards;
	}
	public List<Card> getLegalCards(State s)
	{
		List<Card> cardsPlayed = (List<Card>) s.cardsOnBoard.values();
		List<Card> suitCards = getSuitCards(cardsPlayed,s.cardsOnBoard.get(0).getSuit());
		if(suitCards.size() == 0)
		{
			return this.state.hand;
		}
		else
		{
			return suitCards;
		}
	}


	public Double getQValue(State s,Card action)
	{
		Double sum = 0.0;
		HashMap<String,Double> features = stateFeatures.getFeatures(this,action);
		HashMap<String,Double> weights = stateFeatures.getWeights();
		Iterator<String> featureIterator = features.keySet().iterator();
		while(featureIterator.hasNext())
		{
			String feature = featureIterator.next();
			sum +=  features.get(feature) * weights.get(feature);
		}
		return sum;
	}	
	public Double getFutureQValue(State s)
	{
		List<Card> legalCards = getLegalCards(s);
		if(legalCards.size() == 0) // Not required?
			return 0.0;
		Double max = Double.NEGATIVE_INFINITY;
		Iterator<Card> legalCardIterator = legalCards.iterator();
		while(legalCardIterator.hasNext())
		{
			Card actionCard = legalCardIterator.next();
			Double qVal = getQValue(s,actionCard);
			if(qVal>max)
			{
				max = qVal;
			}
		}
		return max;
		
	}
	public void updateWeights(State prevState ,Card action,State  state, double reward)
	{
		Double diff = ((reward + this.discount * getFutureQValue(state)) - getQValue(state,action));
		HashMap<String,Double> features = stateFeatures.getFeatures(this,action);
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
	 
}
