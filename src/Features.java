import java.util.HashMap;
import java.util.List;

public class Features {
	
	 HashMap<String,Double> features = new HashMap<String,Double>();
	 static HashMap<String,Double>  weights = new HashMap<String,Double>();
	 
	 Features() {
		
		 //Round number
		 Features.weights.put("roundNumber", 1.0);
		 //Card Value
		 Features.weights.put("cardValue", 1.0);
		 //next person suit value'
		 Features.weights.put("nextPlayerSuit", 1.0);
	 }
	 Features(Features f) {
		 this.features = new HashMap<String,Double>(f.features);;
	 }
	public  HashMap<String,Double> getFeatures(State s,Card action)
	{
		//Round number
//		 this.features.put("roundNumber", s.round/10.0);
		 
		 //Card Value
//		 this.features.put("cardValue", s.getHighCard()/10.0);
		 
		 
		 //next person suit value'
		 double f1 = 1.0;
		 int nextPlayerNumber =  Util.getNextPlayerNumber(s.playerNumber,s.playerCount);
		 List<Suit> emptySuits = s.otherPlayerEmptySuits.get(nextPlayerNumber);
		 if(!emptySuits.isEmpty() && emptySuits.contains(s.cardPlayed.getSuit()))
		 {
//			 System.out.println("CUTTTT if i play : "+s.cardPlayed);
			 f1 = -1.0;
		 }
		 else
		 {
			 boolean cardNotPresent = false;
			 for(int i = 0;i<s.playerCount;i++)//3 times since precious 
			 {
				 int nextPlayerNumber2 =  Util.getNextPlayerNumber(s.playerNumber,s.playerCount);
				 List<Suit> emptySuits2 = s.otherPlayerEmptySuits.get(nextPlayerNumber2);
				 if(emptySuits2.contains(s.cardPlayed.getSuit()))
				 {
					 System.out.println("Someone doesnt have");
					 cardNotPresent = true;
				 }
			 }
			 if(cardNotPresent)
			 {
				 f1 = -0.5;
			 }
		 }
		 
		 this.features.put("nextPlayerSuit", f1);
		
		return this.features;
	}
	public HashMap<String,Double> getWeights()
	{
		
		return weights;
	}
	
	public void setFeatures(HashMap<String,Double> features)
	{
		 this.features = features;
	}
	public void setWeights(HashMap<String,Double>  weights)
	{
		this.weights = weights;
	}

}
