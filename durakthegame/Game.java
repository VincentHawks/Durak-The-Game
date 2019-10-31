package durakthegame;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game {
    
    // Working relative paths for assets didn't happen after all
    // Would it really be so bold to ask you for a little help? :3

    public static Player[] players;
    public static Deck deck;
    public static Vector<Card> table = new Vector<Card>(); // One-dimensional, because of an agreement with Player team
    public static String trump;
    public static int currentAttackerID;
    public static StartWindow startWindow;
    public static GameWindow gameWindow;

    public static Image placeholder;
    public static Image back;

    public static Image[][] assetTable = new Image[4][9];

    public static int nextPlayer() {
        if (currentAttackerID < players.length - 1) {
            return currentAttackerID + 1;
        } else {
            return 0;
        }
    }

    public static void nextTurn() {
        table.clear();

        currentAttackerID = nextPlayer();
        
        for(Player player : players){
            player.takeFromDeck();
        }
    }

    public static boolean isSomePlayerHandEmpty() {
        for (Player player : players) {
            if (player.numberOfCardsInHand() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean gameOver() {
        return (deck.amountCard() == 0) && isSomePlayerHandEmpty();
    }

    public static String durak() {
        for (Player player : players) {
            if (player.numberOfCardsInHand() > 0) {
                return player.name();
            }
        }
        return "Somehow no one";
    }
    
    public static Image getAsset(int val, String suit){
        int suitID;
        
        switch(suit){
            case "Hearts":
                suitID = 0;
                break;
            case "Clubs":
                suitID = 1;
                break;
            case "Diamonds":
                suitID = 2;
                break;
            case "Spades":
                suitID = 3;
                break;
            default:
                throw new IllegalArgumentException("Undefined suit");
        }
        return assetTable[suitID][val - 6];
    }

    public static void start(String[] args) throws IOException{
        System.out.println("Start entered");

        placeholder = ImageIO.read(new File("D:\\JavaProjects\\DurakTheGame\\src\\durakthegame\\placeholder.png"));    
        
        for (int i = 0; i < 4; i++) {
            for (int j = 6; j < 15; j++) {

                try {
                    assetTable[i][j-6] = ImageIO.read(new File("D:\\JavaProjects\\DurakTheGame\\src\\durakthegame\\" + ((j<10)?"0":"") + j + Deck.suits[i].substring(0, 1).toLowerCase() + ".png"));
                    System.out.println("Found asset" + ((j<10)?"0":"") + j + Deck.suits[i].substring(0, 1).toLowerCase() + ".png");
                } catch (IOException e) {
                    System.out.println("Missing asset" + ((j<10)?"0":"") + j + Deck.suits[i].substring(0, 1).toLowerCase() + ".png");
                    assetTable[i][j-6] = placeholder;
                }
            }
        }
        try{
            back = ImageIO.read(new File("D:\\JavaProjects\\DurakTheGame\\src\\durakthegame\\back.png"));
        } catch(IOException e){
            back = placeholder;
        }
        
        players = new Player[2];
        System.out.println("Players initialized");
        deck = new Deck();

        // Init players
        for (int i = 0; i < players.length; i++) {
            System.out.println("Player " + i + "\'s name: " + args[i]);
            players[i] = new Player(args[i]);
        }

        trump = deck.getAndMoveTrump();
        System.out.println("The trump is " + trump);
        //Define the first player:
        boolean trumpFound = false;
        for (int i = 0; i < players.length; i++) {
            if (players[i].lowestTrump() == null) {
                continue;
            }
            for (int minTrump = 6; !trumpFound; minTrump++) {
                if (players[i].lowestTrump().getVal() == minTrump) {
                    trumpFound = true;
                    currentAttackerID = i;
                    break;
                }
            }
        }

        System.out.println("Player " + currentAttackerID + " moves first");

        SwingUtilities.invokeLater(new Runnable() {
            @Override

            public void run() {
                startWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                gameWindow = new GameWindow(args[0] + " vs " + args[1]);
                gameWindow.setBounds(0, 0, 1600, 900);
                gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameWindow.setVisible(true);
                startWindow.dispose();
            }
        });

    }

    public static void main(String[] args) {
        System.out.println("Program started");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("\"run()\" reached");
                startWindow = new StartWindow("Game start");
                startWindow.setBounds(700, 300, 300, 300);
                startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                startWindow.setResizable(false);
                startWindow.setVisible(true);
                /*
                debug = new DebugWindow("");
                debug.setBounds(100, 100, 200, 200);
                debug.setVisible(true);
                debug.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                 */
            }

        });
    }

}
