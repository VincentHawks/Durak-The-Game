package durakthegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

final class LogoPanel extends JPanel {
    public LogoPanel() {
        Dimension size = getPreferredSize();
        size.height = 70;
        setPreferredSize(size);
        
        //setBorder(BorderFactory.createTitledBorder("Logo placeholder"));
        
        JLabel logo = new JLabel(" Durak The Game");
        logo.setFont(new Font("Tahoma", 0, 35));
        setLayout(new BorderLayout());
        
        add(logo, BorderLayout.CENTER);
    }
}

final class RegistrationPanel extends JPanel{
    
    public static JTextField player1name, player2name;
    public static JLabel errorlabel;
    
    public RegistrationPanel() { 
        //setBorder(BorderFactory.createTitledBorder("Login placeholder"));
        
        player1name = new JTextField();
        player2name = new JTextField();
        player1name.setColumns(20);
        player2name.setColumns(20);
        
        JLabel player1label = new JLabel("Имя 1 игрока:");
        JLabel player2label = new JLabel("Имя 2 игрока:");
        errorlabel = new JLabel("");
        errorlabel.setForeground(Color.red);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 0.05;
        gc.weighty = 0.05;
        
        gc.gridx = 0;
        gc.gridy = 0;
        add(player1label, gc);
        
        gc.gridy = 1;
        add(player1name, gc);
        
        gc.gridy = 2;
        add(player2label, gc);
        
        gc.gridy = 3;
        add(player2name, gc);
        
        gc.gridy = 4;
        gc.weighty = 2;
        add(errorlabel, gc);
        
        player1name.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    ButtonsPanel.start.doClick();
                }
            }
            
        });
        
        player2name.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    ButtonsPanel.start.doClick();
                }
            }
            
        });
        
    }
}

final class ButtonsPanel extends JPanel{
    
    static JButton start = new JButton("Старт");
    static JButton exit = new JButton("Выход");
    
    public ButtonsPanel(){
        Dimension size = getPreferredSize();
        size.height = 40;
        setPreferredSize(size);
        
        setLayout(new BorderLayout());
        add(start, BorderLayout.WEST);
        add(exit, BorderLayout.EAST);
        
        
        exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println(e.getActionCommand());
                System.exit(0);
            }
        });
        
        start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String[] args = new String[2];
                
                args[0] = RegistrationPanel.player1name.getText();
                args[1] = RegistrationPanel.player2name.getText();
                
                if(args[0].isEmpty() || args[1].isEmpty()){ 
                    RegistrationPanel.errorlabel.setText("Сначала укажите имена игроков!");
                    return;
                }
                try{
                    Game.start(args);
                    RegistrationPanel.errorlabel.setText("");
                } catch(IOException x){
                    RegistrationPanel.errorlabel.setText("ФАТАЛЬНАЯ ОШИБКА: Ресурсы недоступны");
                }
                
            }
        });
    }
}

public final class StartWindow extends JFrame{
    public StartWindow(String title){
        super(title);
        System.out.println("Start Window Initialized");
        
        // Layout manager
        setLayout(new BorderLayout());
        
        // Create components
        LogoPanel logo = new LogoPanel();
        RegistrationPanel login = new RegistrationPanel();
        ButtonsPanel buttons = new ButtonsPanel();
        
        // Apply components
        Container c = getContentPane();
        c.add(logo, BorderLayout.NORTH);
        c.add(login, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);
        
    }
}
