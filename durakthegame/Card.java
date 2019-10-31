package durakthegame;

public class Card {

    private int value;
    private String suit;

    public Card(int value, String suit) {
        if (value > 5 && value < 15 && (suit.equals("Hearts") | suit.equals("Clubs") | suit.equals("Diamonds") | suit.equals("Spades"))) {
            this.value = value;
            this.suit = suit;
        } else {
            throw new IllegalArgumentException("");
        }

    }

    public int getVal() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    boolean canBeat(Card c) {
        return suit.equals(c.getSuit()) && value > c.getVal();
    }

    int compareValue(Card c) {
        if (this.value > c.getVal()) {
            return 1;
        }
        if (this.value < c.getVal()) {
            return -1;
        }
        return 0;
    }
            
    boolean equals(Card c) {
        return this.value == c.getVal() && this.suit.equals(c.getSuit());
    }

    boolean isTrump() {
        return suit.equals(Game.trump);
    }

    boolean fullCanBeat(Card c) {
        if(this.suit.equals(Game.trump)){
            if(c.getSuit().equals(Game.trump)){
                return this.canBeat(c);
            } else return true;
        } else if(this.suit.equals(c.getSuit())){
            return this.canBeat(c);
        } else return false;
    }

    String name() {
        String toPrint = "";
        switch (this.getVal()) {
            case 11:
                toPrint = "Jack";
                break;
            case 12:
                toPrint = "Queen";
                break;
            case 13:
                toPrint = "King";
                break;
            case 14:
                toPrint = "Ace";
                break;
            default:
                toPrint = "" + this.getVal();
        }
        return toPrint + " of " + this.getSuit();
    }
}
