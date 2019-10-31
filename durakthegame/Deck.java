package durakthegame;

import java.util.Random;
import java.util.Vector;

public class Deck {

    Card[] deck;
    static String[] suits = {"Hearts", "Clubs", "Diamonds", "Spades"};
    
    public Deck() {
        Random random = new Random();
        this.deck = new Card[36];
        Vector avalaiblePositions = new Vector();
        for(int i = 0; i < 36; i++){
            avalaiblePositions.add(i);
        }
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 4; j++){ 
                int pos = random.nextInt(avalaiblePositions.size());
                deck[(int)avalaiblePositions.elementAt(pos)] = new Card(i + 6, suits[j]);
                //System.out.println(deck[(int)avalaiblePositions.elementAt(pos)].name());
                avalaiblePositions.removeElementAt(pos);
            }
        }
    }

    public Card getACard() {
        Card lastOne = new Card(this.deck[this.deck.length - 1].getVal(), this.deck[this.deck.length - 1].getSuit());
        getOne();
        return lastOne;
    }

    private void getOne() {
        Card[] deckSupport = new Card[this.deck.length - 1];
        System.arraycopy(this.deck, 0, deckSupport, 0, this.deck.length - 1);
        this.deck = deckSupport;
    }

    public String getAndMoveTrump() {    
        Card trump = getACard();
        Card[] deckSupport2 = new Card[deck.length + 1];
        for (int i = 1; i < (deck.length + 1); i++) {
            deckSupport2[i] = deck[i - 1];
        }
        deckSupport2[0] = trump;
        deck = deckSupport2;
        return trump.getSuit();
    }

    public int amountCard() {
        return this.deck.length;
    }
}