import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;


/**
 * @author Vignesh
 *
 */
public class main{
	static int playerCount = 4;
	private static Player[] players = new Player[playerCount];
	static Deck cards = new Deck();
	static Boolean gameEnd = false;
	static DrawingFrame frame = new DrawingFrame(players);
	
	static Util u = new Util(players,frame);

	
	public static void main (String[] args) {
		int perPlayer = 52 / playerCount;
		System.out.println("Weights before  training " +players[0].stateFeatures.getWeights());
		u.trainPlayer();
		System.out.println("Weights after  training " +players[0].stateFeatures.getWeights());
		
		for (int i = 0; i < playerCount; i++) {
			Player player = new Player(i,playerCount);
			player.state.hand = new ArrayList<Card>(cards.getDeck().subList(i * perPlayer, (i * perPlayer + perPlayer)));
			player.state.playerNumber = i;
			player.setPlayerNumber(i);
			players[i] = player;
			
		}
		
		
		
		
		System.out.println("Weights before playGame" +players[0].stateFeatures.getWeights());
		System.out.println("Weights before playGame" +players[1].stateFeatures.getWeights());
		frame.setSize(new Dimension(1500, 700));
		JButton b1=new JButton("clickssss");//create button 
		b1.addActionListener(u);
		frame.getContentPane().add(b1);
		Container c = frame.getContentPane();
		c.setLayout(new FlowLayout());
		frame.setVisible(true);	
		u.playGame(players);
		System.out.println("Weights after playGame" +players[1].stateFeatures.getWeights());
		
	}

	
	public void printPlayers(Player[] p)
	{
		for(int i = 0;i<p.length;i++)
		{
			System.out.println(p.toString()+ " ");
		}
	}
	public void printCards(Card[] c)
	{
		for(int i = 0;i<c.length;i++)
		{
			System.out.println(c.toString()+ " ");
		}
	}


	

	 
}
