
public enum Suit {

		CLUBS("clubs"), DIAMONDS("diamonds"), HEARTS("hearts"), SPADES("spades"), NONE("None");
		private String suitString;

		Suit(String suitString) {
			this.suitString = suitString;
		}

		public String getValue() {
			return this.suitString;
		}
		public Integer getNumber() {
			if(suitString.equals("clubs"))
			{
				return 1;
			}
			else if(suitString.equals("hearts"))
			{
				return 2;
			}
			else if(suitString.equals("spades"))
			{
				return 3;
			}
			else if(suitString.equals("diamonds"))
			{
				return 0;
			}
			else
			{
				return -100;
			}
		}
	}
