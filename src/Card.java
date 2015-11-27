import java.applet.Applet;
import java.util.Iterator;
import java.util.List;

public class Card extends Applet{

	private Rank rank;
	private Suit suit;

	Card(Rank r, Suit s) {
		this.rank = r;
		this.suit = s;
	}

	public Rank getRank() {
		return this.rank;
	}

	public Suit getSuit() {
		return this.suit;
	}
	
	
	
	
}
