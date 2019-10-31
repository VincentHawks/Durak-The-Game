package durakthegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.NoSuchElementException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


class CanNotMoveException extends Exception {

        public CanNotMoveException() {
        }
    }

final class Table extends JPanel {
    
    GridBagConstraints gc;
    JButton credits;

    public Table() {
        setLayout(new GridBagLayout());
        setBackground(Color.decode("0x0B730B"));

        gc = new GridBagConstraints();
        gc.gridx = 0;
    }
    
    public void addCard(Card card) throws CanNotMoveException{
        try{
            Game.table.lastElement();
            if(Game.table.size() % 2 == 1){
                if(!(card.fullCanBeat(Game.table.lastElement()))){
                    throw new CanNotMoveException();
                }
            } else {
                boolean canThrow = false;
                for(Card elem : Game.table){
                    if(card.compareValue(elem) == 0){
                        canThrow = true;
                    }
                }
                if(!(canThrow)){
                    throw new CanNotMoveException();
                }
            }
        } catch(NoSuchElementException e){
            
        }
        
        add(new CardElement(card, Game.getAsset(card.getVal(), card.getSuit()), true), gc);
        Game.table.add(card);
        gc.gridx++;
        gc.gridy = 0;
        GameWindow.hand.updateCards();
        this.validate();
        GameWindow.hand.validate();
    }
    
    public void clear(){
        gc.gridx = 0;
        this.removeAll();
        this.repaint();
    }
    
    public void gameOver(){
        System.out.println("Game Over");
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.2;
        add(new JLabel("Игра окончена! " + Game.durak() + " дурак!"), gc);
        
        credits = new JButton("Благодарности");
        gc.gridy = 1;
        add(credits, gc);
        
        this.validate();
        this.repaint();
        credits.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                GameWindow.table.removeAll();
                GameWindow.table.validate();
                GameWindow.table.gc.gridx = 0;
                GameWindow.table.gc.gridy = 0;
                GameWindow.table.gc.weighty = 0.2;
                GameWindow.table.add(new JLabel("Над игрой работали: "));
                GameWindow.table.gc.gridy = 1;
                GameWindow.table.add(new JLabel("Логика: команда 18БИ2, "));
                GameWindow.table.gc.gridy = 2;
                GameWindow.table.add(new JLabel("Интерфейс: Команда Game, "));
                GameWindow.table.gc.gridy = 3;
                GameWindow.table.add(new JLabel("Ассеты: Алиса \"SadNicole\" Косолапова"));
                GameWindow.table.validate();
                GameWindow.table.repaint();
            }
        });
    }
}

final class CardElement extends JPanel {

    private Card card;
    private Image image;
    boolean passive;

    public CardElement(Card card, Image image, boolean passive) {
        this.card = card;
        this.image = image;
        this.passive = passive;
        
        Dimension size = getPreferredSize();
        size.height = 200;
        size.width = 120;
        setPreferredSize(size);
        
        setLayout(new BorderLayout());
        
        JButton cardButton = new JButton(new ImageIcon(image.getScaledInstance(120, 200, Image.SCALE_SMOOTH)));
        add(cardButton, BorderLayout.CENTER);
        
        if(!passive){
            cardButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    try{
                        GameWindow.table.addCard(card);
                    } catch(CanNotMoveException x){
                        return;
                    }
                    GameWindow.hand.removeCard(card);
                    GameWindow.table.validate();
                    GameWindow.hand.validate();
                    GameWindow.rotate();
                }
            });
        }
    }
    
    public CardElement(Card card){
        this(card, Game.getAsset(card.getVal(), card.getSuit()), false);
    }
    
    public CardElement(){
        this(new Card(14, "Spades"), Game.back, true);
    }

    public Card getCard() {
        return this.card;
    }

    public Image getImage() {
        return this.image;
    }
}

final class Hand extends JPanel {
    
    private Player player;

    public Hand(Player player) throws IOException {
        this.player = player;

        Dimension size = getPreferredSize();
        size.height = 210;
        setPreferredSize(size);
        setBackground(Color.darkGray);
        
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        setLayout(layout);
        updateCards();
    }

    public void addCard(Card card) {
        if(card != null){
            add(new CardElement(card, Game.placeholder, false)); 
            this.update(this.getGraphics());
        }
    }
    
    public void removeCard(Card card){
        for(Card elem : player.hand){
            if(elem.equals(card)){
                
                player.giveCard(card);
                updateCards();
                
                return;
            }
        }
    }
    
    public Player getPlayer(){
        return this.player;
    }
    
    public void setPlayer(Player player){
        this.player = player;
    }
    
    public void updateCards() {
        
        this.removeAll();
        this.validate();
        this.repaint();
        for(Card card : player.hand){
            add(new CardElement(card));
        }
        this.repaint();
    }

    
}

final class Opponent extends JPanel {
    private Player player;
    
    public void updateCards(){
        this.removeAll();
        for(Card card : player.hand){
            add(new CardElement());
        }
        this.validate();
        this.repaint();
    }
    
    public void setPlayer(Player player){
        this.player = player;
    }
    
    public Player getPlayer(){
        return this.player;
    }
    
    public Opponent(Player player){
        this.player = player;
        
        Dimension size = getPreferredSize();
        size.height = 210;
        setPreferredSize(size);
        setBackground(Color.darkGray);
        
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        setLayout(layout);
        
        updateCards();
    }
}

final class Indicator extends JPanel {
    
    JLabel attacker, cardsInDeck, trump;
    
    private String translate(String trump){
        switch(trump){
            case "Hearts":
                return "Червы";
            case "Spades":
                return "Пики";
            case "Clubs":
                return "Трефы";
            case "Diamonds":
                return "Бубны";
            default:
                return "Не козырь";
        }
    }
    
    public void updateCount(){
        cardsInDeck.setText("Карт в колоде: " + Game.deck.amountCard());
    }

    public Indicator() {
        setBackground(Color.decode("0x0B730B"));
        
        attacker = new JLabel("Ходит " + (Game.players[Game.currentAttackerID].name()));
        cardsInDeck = new JLabel("Карт в колоде: " + Game.deck.amountCard());
        trump = new JLabel("Козырь :" + translate(Game.trump));
        
        setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        
        Dimension size = getPreferredSize();
        size.width = 150;
        setPreferredSize(size);

        setLayout(new GridBagLayout());
        
        GridBagConstraints gc = new GridBagConstraints();
        
        gc.weighty = 0.2;
        gc.gridx = 0;
        gc.gridy = 0;
        add(attacker, gc);
        
        gc.gridy = 1;
        add(cardsInDeck, gc);
        
        gc.gridy = 2;
        add(trump, gc);
        
    }
}

final class ControlArea extends JPanel {

    public ControlArea() {
        setBackground(Color.decode("0x0B730B"));
        
        this.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        
        JButton end = new JButton("Завершить ход"), turn = new JButton();
        end.setBounds(end.getX(), end.getY(), end.getWidth(), end.getHeight() * 2);
        
        setLayout(new GridBagLayout());
        
        GridBagConstraints gc = new GridBagConstraints();
        
        Dimension size = getPreferredSize();
        size.width = 150;
        setPreferredSize(size);
        
        gc.gridx = 0;
        gc.gridy = 0;
        add(end, gc);
        
        end.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean rotates = true;
                if(Game.table.size() % 2 == 1){
                    GameWindow.hand.getPlayer().takeFromDesk();
                    Game.nextTurn();
                    //rotates = false;
                }
                if(Game.gameOver()){
                    GameWindow.table.removeAll();
                    GameWindow.hand.removeAll();
                    GameWindow.opponentHand.removeAll();
                    GameWindow.indicator.removeAll();
                    GameWindow.drop.removeAll();
                    GameWindow.table.validate();
                    GameWindow.hand.validate();
                    GameWindow.opponentHand.validate();
                    GameWindow.indicator.validate();
                    GameWindow.drop.validate();
                    GameWindow.table.repaint();
                    GameWindow.hand.repaint();
                    GameWindow.opponentHand.repaint();
                    GameWindow.indicator.repaint();
                    GameWindow.drop.repaint();
                    GameWindow.table.gameOver();
                    return;
                }
                Game.nextTurn();
                GameWindow.table.clear();
                GameWindow.hand.updateCards();
                GameWindow.opponentHand.updateCards();
                GameWindow.indicator.updateCount();
                if(rotates) GameWindow.rotate();
                GameWindow.indicator.attacker.setText("Ходит " + (Game.players[Game.currentAttackerID].name()));
                
            }
            
        });
    }
}

public class GameWindow extends JFrame {
    
    static Table table = new Table();
    static Hand hand;
    static Opponent opponentHand;
    static Indicator indicator;
    static ControlArea drop;

    public GameWindow(String title) {
        super(title);

        setLayout(new BorderLayout());    

        try {
            hand = new Hand(Game.players[Game.currentAttackerID]);
            opponentHand = new Opponent(Game.players[Game.nextPlayer()]);
        } catch (IOException e) {
            throw new IllegalStateException("Missing files");
        }
        indicator = new Indicator();
        drop = new ControlArea();

        Container c = getContentPane();
        c.add(opponentHand, BorderLayout.PAGE_START);
        c.add(table, BorderLayout.CENTER);
        c.add(hand, BorderLayout.PAGE_END);
        c.add(indicator, BorderLayout.WEST);
        c.add(drop, BorderLayout.EAST);
        
    }
    
    public static void rotate(){
        table.gc.gridy = 1 - table.gc.gridy;
        Player buffer = hand.getPlayer();
        hand.setPlayer(opponentHand.getPlayer());
        opponentHand.setPlayer(buffer);
        hand.updateCards();
        opponentHand.updateCards();
        hand.validate();
        opponentHand.validate();
        
        char defenderKey = Game.players[Game.nextPlayer()].name().charAt(0),
            attackerKey = Game.players[Game.currentAttackerID].name().charAt(0), 
            moveKey = 'Х',
            currentKey = indicator.attacker.getText().charAt(0);
        
        if(currentKey == moveKey){
            indicator.attacker.setText(Game.players[Game.nextPlayer()].name() + " кроет");
        } else if(currentKey == defenderKey){
            indicator.attacker.setText(Game.players[Game.currentAttackerID].name() + " подкидывает");
        } else if(currentKey == attackerKey){
            indicator.attacker.setText(Game.players[Game.nextPlayer()].name() + " кроет");
        } else {
            throw new IllegalStateException("Incorrect label");
        }
    }

}
