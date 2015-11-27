
public enum Suit {

		CLUBS("clubs"), DIAMONDS("diamonds"), HEARTS("hearts"), SPADES("spades");
		private String suitString;

		Suit(String suitString) {
			this.suitString = suitString;
		}

		public String getValue() {
			return this.suitString;
		}
	}
