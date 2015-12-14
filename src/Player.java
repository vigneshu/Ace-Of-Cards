import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Player{
	public State state;
	double discount = 0.05;//TODO change discount alpha and value
	double alpha = 0.05;
	private int playerNumber;
	static private int playerCount;
	public static Features  stateFeatures = new Features();
	Boolean bot;
	public String playerName;

	
	Player(Player p,Boolean bot) {
		this.state = new State(p.state);
		this.playerNumber = p.playerNumber;
		this.discount = p.discount;
		this.alpha = p.alpha;
		Player.stateFeatures = new Features(stateFeatures);
		this.bot = bot;
		
	}
	
	Player(int pno,int playerCount,Boolean bot) {
		this.state = new State();
		this.state.playerNumber = pno;
		this.playerNumber = pno;
		Player.playerCount = playerCount;
		this.bot = bot;
		for(int i=0;i<playerCount;i++)
		{
			if(i != playerNumber)
			{
				this.state.otherPlayerCards.put(i, new ArrayList<Card>());
				this.state.otherPlayerEmptySuits.put(i, new ArrayList<Suit>());
			}
		}
	}
	public String getName()
	{
		return this.playerName;
	}
	public int getPlayerNumber()
	{
		return this.playerNumber;
	}
	public int setPlayerNumber(int playernumber)
	{
		return this.playerNumber = playernumber;
	}
	public Card playTurn(Boolean training)
	{
		HashMap<Card,Double> actionAndValue = getFutureQValue(state,training);
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
		HashMap<String,Double> features = stateFeatures.getFeatures(state,action);
		HashMap<String,Double> weights = stateFeatures.getWeights();
		if(weights.isEmpty())
			System.out.println("weights Erorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+weights);
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
		return getFutureQValue( s, false);
	}
	public HashMap<Card,Double> getFutureQValue(State s,Boolean training)
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
		Random randomGenerator = new Random();;
		
		if(training && Math.random()<0.5)
		{
			 int index = randomGenerator.nextInt(legalCards.size());
			 action = legalCards.get(index);
		}
		else
		{
			Iterator<Card> legalCardIterator = legalCards.iterator();
			boolean temp = false;
//			int nextPlayerNumber =  Util.getNextPlayerNumber(playerNumber,s.playerCount);
			while(legalCardIterator.hasNext())
			{
				 Card actionCard = legalCardIterator.next();
				Double qVal = getQValue(s,actionCard);
				////Temp test

				
//				List<Suit> emptySuits = s.otherPlayerEmptySuits.get(nextPlayerNumber);
//				 if(s.cardsOnBoard.size()==0 && !temp &&!training && !emptySuits.isEmpty() && emptySuits.contains(actionCard.getSuit()))
//				 {
//					System.out.println("qVal for emoty suit "+qVal+" "+s.cardPlayed.getSuit());
//					System.out.println("legalCards "+legalCards);
//					temp = true;
//				 }
//				 if(s.cardsOnBoard.size()==0)
//				 {
//					 System.out.println("actionCard "+actionCard);
//					 System.out.println("actionCard qVal "+qVal);;
//				 }
//				 if(temp)
//				 {

//				 }
				////Temp test
				if(qVal>max)
				{
					max = qVal;
					action.setRank(actionCard.getRank()); 
					action.setSuit(actionCard.getSuit());
					
				}
			}
		}
		actionAndValue.put(action,max);
		return actionAndValue;
		
	}

	public void updateWeights(State prevState ,Card action,State  state, double reward)
	{
		HashMap<Card,Double> actionAndValue = getFutureQValue(state,false);//should be false for update weights even during training
		Card maxAction =  actionAndValue.keySet().iterator().next();
		Double maxVal = actionAndValue.get(maxAction);
		Double diff = ((reward + this.discount * maxVal) - getQValue(state,action));
		HashMap<String,Double> features = stateFeatures.getFeatures(state,action);
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
