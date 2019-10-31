package durakthegame;

import java.util.ArrayList;
import java.util.Vector;

class Player {

    Card[] hand;
    String ourname;

    Card lowestTrump() {
        ArrayList<Card> trumpArray = new ArrayList<Card>(); 
        for (int i = 0; i < hand.length; i++) {
            if (hand[i].isTrump()) {
                trumpArray.add(hand[i]);
            }
        }
        
        if(trumpArray.isEmpty()){
            return null;
        }
        Card min = trumpArray.get(0); //��� ������
        for (int i = 0; i < trumpArray.size(); i++) {
            if (min.compareValue(trumpArray.get(i)) == 1) {
                min = trumpArray.get(i);
            }

        }
        return min;
    }

    void takeFromDesk() {
        for (int i = 0; i < Game.table.size(); i++) {
            enlargeArray();
            hand[hand.length - 1] = Game.table.elementAt(i);
            
        }
        Game.table.clear();
    }

    final void takeFromDeck() {
        int lackofcards = 6 - hand.length;
        if (Game.deck.amountCard() >= lackofcards) {
            while (hand.length < 6) {
                enlargeArray();
                hand[hand.length - 1] = Game.deck.getACard();

            }
        } else {
            while (Game.deck.amountCard() != 0) {
                enlargeArray();
                hand[hand.length - 1] = Game.deck.getACard();

            }
        }

    }
    
    Card move() {
        Card min = hand[0];
        for (int i = 0; i < hand.length; i++) {
            if (min.compareValue(hand[i]) == 1) {
                min = hand[i];
            }
        }

        return (giveCard(min));
    }

    Card throwcard() {
        for (int a = 0; a < hand.length; a++) {
            for (int i = 0; i < Game.table.size(); i++) {
                if (hand[a].compareValue(Game.table.elementAt(i)) == 0) {
                    return giveCard(hand[a]);
                }
            }
        }
        return null;
    }

    public boolean canThrowCard() {
    	for (int a = 0; a < hand.length; a++) {
            for (int i = 0; i < Game.table.size(); i++) {
                if (hand[a].compareValue(Game.table.elementAt(i)) == 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean canCover(Card opponent_card) {
    	for (int i = 0; i < hand.length; i++)
            if (hand[i].canBeat(opponent_card))
                return true;
        return false;
    }
    
    Card cover(Card opponent_card) {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i].canBeat(opponent_card)) {
                return giveCard(hand[i]);
            }
        }
        return null;
    }

    public Card giveCard(Card card) {
        Card add[] = new Card[hand.length - 1];
        for (int i = 0; i < cardIndex(card); i++) {
            add[i] = hand[i];
        }
        for (int i = cardIndex(card); i < hand.length - 1; i++) {
            add[i] = hand[i + 1];
        }
        hand = add;
        return card;
    }

    private int cardIndex(Card card) {
        int a = -1; 
        for (int i = 0; i < hand.length; i++) {
            if (card.equals(hand[i])) {
                a = i;
            }
        }
        return a;
    }

    private void enlargeArray() {
        Card[] add = new Card[hand.length + 1];
        for (int i = 0; i < hand.length; i++) {
            add[i] = hand[i];
        }
        hand = add;
    }

    public int numberOfCardsInHand() {
    	return hand.length;
    }
    
    public String name() {
    	return ourname;
    }
    
    public Card[] getCardsInHand() { 
        return hand; 
    } 
    
    public Card getByIndex(int index){
        if(index >= numberOfCardsInHand()){
            return null;
        }
        
        return hand[index];
    }
    
    Player(String name) {
        ourname = name;
        hand = new Card[0];
        takeFromDeck();

    }
}
