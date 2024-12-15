
package gamble;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author CKY
 */

public class MainMenu {
    
    private Frame mmFrame = new Frame();
    private JButton jbtCredit = new JButton("Manage Credit");
    private JButton jbtGame = new JButton("Play Games");
    private JButton jbtLogout = new JButton("Logout");
    private String username;
    private double credit;
    
    public MainMenu(String username, double credit) {
        
        this.username = username;
        this.credit = credit;
        
        mmFrame.createFrame("Main Menu", 320, 300, 3);
        JPanel p1 = new JPanel(new GridLayout(1,3));
        p1.add(new JLabel(this.username));
        p1.add(new JLabel("Your credit: " + this.credit));
        mmFrame.add(p1, BorderLayout.NORTH);
        
        JPanel p2 = new JPanel(new GridLayout(3,1));
        p2.add(jbtCredit);
        jbtCredit.addActionListener(new listeners());
        p2.add(jbtGame);
        jbtGame.addActionListener(new listeners());
        p2.add(jbtLogout);
        jbtLogout.addActionListener(new listeners());
        mmFrame.add(p2, BorderLayout.CENTER);
    }
    
    private class listeners implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            
            if(e.getSource() == jbtCredit) {
                mmFrame.dispose();
                Credit cred = new Credit(credit, username);
            }
            else if(e.getSource() == jbtGame) {
                mmFrame.dispose();
                GameMenu gm = new GameMenu(credit, username);
            }
            else if(e.getSource() == jbtLogout) {
                mmFrame.dispose();
                Login login = new Login();
            }
        }
    }
}
