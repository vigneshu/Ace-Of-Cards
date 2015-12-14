public class Card {

	private Rank rank;
	private Suit suit;

	Card(Rank r, Suit s) {
		this.rank = r;
		this.suit = s;
	}
	Card() {
		
	}

	public Rank getRank() {
		return this.rank;
	}

	public Suit getSuit() {
		return this.suit;
	}
	
	public Integer getSuitNumber() {
		return this.suit.getNumber();
	}

	public void setRank(Rank r) {
		this.rank = r;
	}

	public void setSuit(Suit s) {
		this.suit = s;
	}
	
	public String toString(){
		return (Integer.toString(rank.getValue()) + this.suit.getValue()  );
		
	}
	public Boolean equals(Card c2){
		return (c2.getRank() == this.rank && c2.getSuit() == this.suit );
		
	}

	
	
	

}
