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
		 Features.weights.put("nextPlayerSuit1", 1.0);
		 //second person suit value'
		 Features.weights.put("nextPlayerSuit2", 1.0);
		 //second person suit value'
		 Features.weights.put("nextPlayerSuit3", 1.0);
	 }
	 Features(Features f) {
		 this.features = new HashMap<String,Double>(f.features);
		 //Round number
		 Features.weights.put("roundNumber", 1.0);
		 //Card Value
		 Features.weights.put("cardValue", 1.0);
		 //next person suit value'
		 Features.weights.put("nextPlayerSuit1", 1.0);
		 //second person suit value'
		 Features.weights.put("nextPlayerSuit2", 1.0);
		 //second person suit value'
		 Features.weights.put("nextPlayerSuit3", 1.0);
	 }
	public  HashMap<String,Double> getFeatures(State s,Card action)
	{
		//Round number
		 this.features.put("roundNumber", s.round/10.0);
		 
		 //Card Value
		 this.features.put("cardValue", action.getRank().getValue()/10.0);
		 
		 
		 //next person suit value'
		 int relativePno = 1,playerNumber = s.playerNumber;
		 while(relativePno<4)
		 {
			 double f1 = 0.0;
			 int nextPlayerNumber =  Util.getNextPlayerNumber(playerNumber,s.playerCount);
			 List<Suit> emptySuits = s.otherPlayerEmptySuits.get(nextPlayerNumber);
			 if(!emptySuits.isEmpty() && emptySuits.contains(action.getSuit()))
			 {
//				 System.out.println("CUTTTT if i play : "+s.cardPlayed);
				 f1 = 1.0 * (relativePno);
			 }
//			 else
//			 {
//				 boolean cardNotPresent = false;
//				 for(int i = 0;i<s.playerCount;i++)//3 times since precious 
//				 {
//					 int nextPlayerNumber2 =  Util.getNextPlayerNumber(playerNumber,s.playerCount);
//					 List<Suit> emptySuits2 = s.otherPlayerEmptySuits.get(nextPlayerNumber2);
//					 if(emptySuits2.contains(s.cardPlayed.getSuit()))
//					 {
//						 System.out.println("Someone doesnt have");
//						 cardNotPresent = true;
//					 }
//				 }
//				 if(cardNotPresent)
//				 {
//					 f1 = -0.5;
//				 }
//			 }
			 this.features.put("nextPlayerSuit"+relativePno, f1);
			 relativePno++;
			 playerNumber = nextPlayerNumber;
		 }
		 
		
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
		Features.weights = weights;
	}

}
