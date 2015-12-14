import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Util extends JPanel  implements ActionListener{
	boolean  nextStep;
	Player[] players;
	DrawingFrame frame;
	//New UI
	private Map<String, PlayerPanel> playerPanels;
	String actorName;
	Integer actorPosition;
	Player actor;
	 private  BoardPanel boardPanel ;
	//New UI end
	Util(Player[] players,DrawingFrame frame, Map<String, PlayerPanel> playerPanels,BoardPanel boardPanel)
	{
		this.nextStep = false;
		this.players = players;
		this.frame = frame;
		this.playerPanels = playerPanels;
		this.boardPanel = boardPanel;
	}
	
	public void playGame(Player players[])
	{
		playGame(players,false);
	}
	public void playGame(Player players[],Boolean training)
	{
		boolean gameEnd = false;
		int playerCount = players.length;
		int round = 1;
		
		List<Player> playerList = new ArrayList<Player>();
		for(int i =0;i<players.length;i++)// Until round ends
		{
			playerList.add(players[i]);
		}
		
		//New UI start
		if(!training)
			joinedTable(playerList);
		//New UI end
		while(!gameEnd)//Until game ends
		{
			
			LinkedHashMap<Player,Card> cardOnBoard = new LinkedHashMap<Player,Card>();
			Suit prevSuit = null;
			State[] state = new State[playerCount];
			Card[] actions = new Card[playerCount];
			Double[] reward = new Double[playerCount];
			Iterator<Player> playerIt = playerList.iterator();
//			for(int i =0;i<players.length;i++)// Until round ends
			int i = 0 ;
			while(playerIt.hasNext())
			{
				Player p = playerIt.next();
				i = p.getPlayerNumber();
				
//				
				//New UI Start
				actorName = p.getName();
				actorPosition = i;
				actor = p;
				//New UI End
				players[i].state.cardsOnBoard = new LinkedHashMap<Player,Card>(cardOnBoard);
				players[i].state.round = round;
				if(!training)
				{
					
					while(this.nextStep == false)
					{
						
						
						 try {
						       Thread.sleep(200);
						    } catch(InterruptedException e) {
						    }
					}
					this.nextStep = false;
					frame.repaint();
					frame.cardsOnBoard = players[i].state.cardsOnBoard;
					try {
					       Thread.sleep(200);
					    } catch(InterruptedException e) {
					    }
				}
				players[i].state.playerCount = playerCount;			
				players[i].state.playerNumber = i;			
				
				Card c1 = players[i].playTurn(training);
				Suit suit = c1.getSuit();
				actions[i] = new Card(c1.getRank(),suit);
				cardOnBoard.put(players[i],c1);
				players[i].state.cardsOnBoard = new LinkedHashMap<Player,Card>(cardOnBoard);
				players[i].state.cardPlayed = new Card(c1.getRank(),c1.getSuit());


				state[i] = new State(players[i].state);
				if(!training)
				{	
					//New UI start
					rotateActor();
					notifyBoardUpdated(p);
					playerUpdated(p);
					notifyPlayerActed(p);
					//New UI end
				}
				if(prevSuit != null)
				{
					if(suit!= prevSuit)
					{

//						System.out.println("Card cut  by player "+ i+ "Going tonext round.Played card "+c1.toString());
						break;
					}
				}
				prevSuit = suit;

				
			}
			
			if(!training)
			{
				
				while(this.nextStep == false)
				{
					
					
					 try {
					       Thread.sleep(200);
					    } catch(InterruptedException e) {
					    }
				}
				this.nextStep = false;
				frame.repaint();
				Player lastPlayer = null;
				Iterator<Player> cardIterator = cardOnBoard.keySet().iterator();
				while(cardIterator.hasNext())
				{
					lastPlayer = cardIterator.next();
				}
				frame.cardsOnBoard = lastPlayer.state.cardsOnBoard;
				try {
				       Thread.sleep(200);
				    } catch(InterruptedException e) {
				    }
			}
			if(!gameEnd)
			{
				reward = decideCards(cardOnBoard,players);
				Double min = 0.0;
				
				
				int index = 0;
				boolean cut = false;
				for (int i1 = 0;i1< players.length;i1++)
				{
					if(players[i1].state.hand.size() == 0)
					{
						gameEnd = true;
						System.out.println("Game won by player i "+i1);
						if(!training)
						JOptionPane.showMessageDialog(frame, "Game won by PlayerName"+i1);
					}
					if(reward[i1]<min)
					{
						min = reward[i1];
						index = i1;
						cut = true;
					}
				}
				if(!cut)
				{
					index = getHighCardPlayerIndex(cardOnBoard);
				}
				playerList.clear();
				playerList.add(players[index]);
				for(int count = 0;count<playerCount-1;count++)
				{
					int nextPlayer = getNextPlayerNumber(index,playerCount);
					playerList.add(players[nextPlayer]);
					index = nextPlayer;
				}
				Iterator<Player> cardIt = cardOnBoard.keySet().iterator();
				while(cardIt.hasNext())
				{
					Player p = cardIt.next();
					int playerNumber = p.getPlayerNumber();
					p.updateWeights(state[playerNumber],actions[playerNumber],players[playerNumber].state,reward[playerNumber]);
				}
			}
			round++;
		}

		
	}
	public int getHighCardPlayerIndex(LinkedHashMap<Player,Card> cardOnBoard)
	{
		Iterator<Player> it = cardOnBoard.keySet().iterator();
		Double max = Double.NEGATIVE_INFINITY;
		int pIndex = 0;
		while(it.hasNext())
		{
			Player p = it.next();
			Card c = cardOnBoard.get(p);
			if(c.getRank().getValue()>max)
			{
				max = Double.valueOf(c.getRank().getValue());
				pIndex = p.getPlayerNumber();
			}
		}
		return pIndex;
	}
	public Double[] decideCards(LinkedHashMap<Player,Card> cardOnBoard,Player[] players)
	{
		int playerCount  = players.length;
		Collection<Card> cardsPlayed = cardOnBoard.values();
		boolean sameSuit = PackOfCards.sameSuit(cardsPlayed);
		Double[] reward = new Double[playerCount];
		for (int i = 0;i< playerCount;i++)
		{
			reward[i] = 0.0;
			
		}
		if(sameSuit)
		{
			for (int i = 0;i< playerCount;i++)
			{
				players[i].state.passedCards.addAll(cardsPlayed);
				reward[i] = 10.0;
				
			}
		}
		else
		{
			//TODO reward[i] puts in rewards assuming  player 1 starts round always
			Suit s = cardsPlayed.iterator().next().getSuit();
			
			Integer playerNumber = PackOfCards.getPlayerWithHighCard(cardOnBoard,s);
			players[playerNumber].state.hand.addAll(cardsPlayed); 
			Player cutPlayer = null;
			
			Iterator<Player> playerIt = cardOnBoard.keySet().iterator();
			int i = 0;
			while(playerIt.hasNext())
			{
				Player p = playerIt.next();
				if(playerNumber.equals(p.getPlayerNumber()))//PLayer who received the cut 
				{
					reward[p.getPlayerNumber()] = -10.0 + (cardOnBoard.size());
				}
				else if(i == cardOnBoard.size() - 1)//PLayer who cut the card
				{
					reward[p.getPlayerNumber()] = 10.0;
					cutPlayer = p;
				}
				else
				{
					reward[p.getPlayerNumber()] = 10.0;
				}
				i++;
			}
			
			for (Integer j = 0;j< players.length;j++)
			{
			
				if(!playerNumber.equals(j))
				{
					HashMap<Integer,List<Card>> otherCards = players[j].state.otherPlayerCards;
					List<Card> cards = otherCards.get(playerNumber);
					cards.addAll( cardsPlayed);
				}
				if(j != cutPlayer.getPlayerNumber())//PLayer who cut the card
				{
					List<Suit> suits = players[j].state.otherPlayerEmptySuits.get(cutPlayer.getPlayerNumber());
					suits.add(s);
					players[j].state.otherPlayerEmptySuits.put(cutPlayer.getPlayerNumber(),suits);
				}
			}	
			
		}
		return reward;
	}
	public void trainPlayer()
	{
		int training = 1;
		int playerCount = 4;
		
		int perPlayer = 52 / playerCount;
		
		while(training<5000)
		{
			Player[] players = new Player[playerCount];
			Deck cards = new Deck();
			for (int i = 0; i < playerCount; i++) {
				players[i] = new Player(i,playerCount,true);
				players[i].state.hand = new ArrayList<Card>(cards.getDeck().subList(i * perPlayer, (i * perPlayer + perPlayer)));
				players[i].setPlayerNumber(i);
				players[i].playerName = "PlayerName"+i;
				
			}
			playGame(players,true);
//			System.out.println("Weights after training "+training+": "+players[0].stateFeatures.getWeights());
			training++;
		}
		
	}
	public static int getNextPlayerNumber(int n,int playerCount)
	{
		if(n==playerCount-1)
			return 0;
		else
			return n+1;
	}	
	
		public void actionPerformed(ActionEvent e){  
//			JOptionPane.showMessageDialog(this,"next hand"); 
			this.nextStep = true;
			}


		/////////New UI functions start
		    public void joinedTable(List<Player> players) {
		        for (Player player : players) {
//		        	System.out.println("playerPanels "+playerPanels);
		            PlayerPanel playerPanel = playerPanels.get(player.getName());
//		            System.out.println("player.getName() "+player);
		            if (playerPanel != null) {
		                playerPanel.update(player);
		            }
		        }
		    }


		   
		    private void rotateActor() {
		        for (Player player : players) {
		        	actorRotated(actor);
		        }
		    }
		    public void actorRotated(Player actor) {
		        setActorInTurn(false);
		        actorName = actor.getName();
		        setActorInTurn(true);
		    }
		    private void setActorInTurn(boolean isInTurn) {
		        if (actorName != null) {
		            PlayerPanel playerPanel = playerPanels.get(actorName);
		            if (playerPanel != null) {
		                playerPanel.setInTurn(isInTurn);
		            }
		        }
		    }
		    
		    
		    private void notifyBoardUpdated(Player player) {
		    	boardUpdated(player.state.cardsOnBoard.values());
		    }
		    
		    public void boardUpdated(Collection<Card> cardsOnBoard) {
		        boardPanel.update(cardsOnBoard);
		    }

		    public void playerUpdated(Player player) {
		        PlayerPanel playerPanel = playerPanels.get(player.getName());
		        if (playerPanel != null) {
		            playerPanel.update(player);
		        }
		    }
		    
		    private void notifyPlayerActed(Player p) {
//		        for (Player p : players) {
		            //Player playerInfo = p.equals(actor) ? actor : actor.publicClone();
//		           playerActed(p);
//		        }
		        playerActed(p);
		    }

		    public void playerActed(Player player) {
		        String name = player.getName();
		        PlayerPanel playerPanel = playerPanels.get(name);
		        if (playerPanel != null) {
		            playerPanel.update(player);
		            if (!player.bot) {
		                boardPanel.waitForUserInput();
		            }
		        } else {
		            throw new IllegalStateException(
		                    String.format("No PlayerPanel found for player '%s'", name));
		        }
		    }	
	/////////New UI functions end
		
		
		
}