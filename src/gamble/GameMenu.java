
package gamble;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author CKY
 */

public class GameMenu {
    
    private Frame gmFrame = new Frame();
    private JButton jbtCoin = new JButton("Coin Flip");
    private JButton jbtLuckyNo = new JButton("Lucky Number");
    private JButton jbtBack = new JButton("Back");
    private double credit;
    private String username;
    
    public GameMenu(double credit, String username){
        
        this.credit = credit;
        this.username = username;
        
        gmFrame.createFrame("Game Menu", 400, 300, 3);
        JPanel p1 = new JPanel();
        p1.add(new JLabel("Game"));
        gmFrame.add(p1, BorderLayout.NORTH);
        JPanel p2 = new JPanel(new GridLayout(3,1));
        p2.add(jbtCoin);
        jbtCoin.addActionListener(new listeners());
        p2.add(jbtLuckyNo);
        jbtLuckyNo.addActionListener(new listeners());
        p2.add(jbtBack);
        jbtBack.addActionListener(new listeners());
        gmFrame.add(p2, BorderLayout.CENTER);
    }
    
    private class listeners implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == jbtCoin) {
                gmFrame.dispose();
                CoinFlip cf = new CoinFlip(credit, username);
            }
            else if(e.getSource() == jbtLuckyNo) {
                gmFrame.dispose();
                LuckyNo ln = new LuckyNo(credit, username);
            }
            else if (e.getSource() == jbtBack) {
                gmFrame.dispose();
                MainMenu mm = new MainMenu(username, credit);
            }
        }
    }
}
