package gamble;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.Random;

/**
 *
 * @author CKY
 */
public class CoinFlip {

    private String host = "jdbc:derby:Game";
    private Connection con;
    private PreparedStatement stmt;

    private Frame cfFrame = new Frame();
    private JTextField jtfAmt = new JTextField();
    private JComboBox jcbHeadTail = new JComboBox(new Object[]{"Head", "Tail"});
    private JButton jbtConfirm = new JButton("Confirm");
    private JButton jbtBack = new JButton("Back");
    private double credit;
    private String username;
    private boolean win = false;

    public CoinFlip(double credit, String username) {

        this.credit = credit;
        this.username = username;
        
        cfFrame.createFrame("Coin Flip", 400, 200, 3);

        JPanel p1 = new JPanel(new GridLayout(2, 3));
        p1.add(new JLabel(this.username));
        p1.add(new JLabel());
        p1.add(new JLabel("Your credit: " + this.credit));
        p1.add(new JLabel());
        p1.add(new JLabel());
        p1.add(new JLabel());
        cfFrame.add(p1, BorderLayout.NORTH);

        JPanel p2 = new JPanel(new GridLayout(2, 2));
        p2.add(new JLabel("Enter an amount you want to bet:"));
        p2.add(jtfAmt);
        p2.add(new JLabel("Select Head or Tail:"));
        p2.add(jcbHeadTail);
        jcbHeadTail.setRequestFocusEnabled(false);
        cfFrame.add(p2);

        JPanel p3 = new JPanel();
        p3.add(jbtConfirm);
        jbtConfirm.addActionListener(new listeners());
        p3.add(jbtBack);
        jbtBack.addActionListener(new listeners());
        cfFrame.add(p3, BorderLayout.SOUTH);
        
        connectDB();
    }

    private class listeners implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == jbtConfirm) {
                try {
                    if (jtfAmt.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter an amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        double amount = Double.parseDouble(jtfAmt.getText());
                        int selection = jcbHeadTail.getSelectedIndex();

                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                            clearText();
                        } else if (amount > credit) {
                            JOptionPane.showMessageDialog(null, "Insufficient fund", "ERROR", JOptionPane.ERROR_MESSAGE);
                            clearText();
                        } else {
                            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to perform this action? "
                                    + "If you win you will get 2x the amount you gamble but if you lose you will lose all the amount you gamble.",
                                    "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                int result = coinFlip(amount, selection);
                                if(win){
                                    JOptionPane.showMessageDialog(null, "Congratulations! You have flipped " + jcbHeadTail.getItemAt(result) + 
                                            " and won " + amount * 2 + " credits.", "Congrats", JOptionPane.INFORMATION_MESSAGE);
                                    cfFrame.dispose();
                                    CoinFlip cf = new CoinFlip(credit, username);
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "So sad! You have flipped " + jcbHeadTail.getItemAt(result) + 
                                            " and lost " + amount + " credits.", "Nice try", JOptionPane.INFORMATION_MESSAGE);
                                    cfFrame.dispose();
                                    CoinFlip cf = new CoinFlip(credit, username);
                                }
                            }
                        }
                    }
                
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                    clearText();
                }
            }
            else if(e.getSource() == jbtBack) {
                cfFrame.dispose();
                GameMenu gm = new GameMenu(credit, username);
            }
        }
    }

    private int coinFlip(double amount, int selection) {

        Random rand = new Random(System.currentTimeMillis());
        double random = (rand.nextDouble() * 100) / 50;
        
        if(selection == (int)random){
            credit += amount;
            updateCredit(credit, username);
            win = true;
        }
        else {
            credit -= amount;
            updateCredit(credit, username);
            win = false;
        }
        
        return (int)random;
    }
    
    private void connectDB() {
        try {
            con = DriverManager.getConnection(host);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void disconnectDB() {
        try {
            stmt.close();
            con.close();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void updateCredit(double credit, String username) {
        String update = "UPDATE ACCOUNT SET CREDIT = ? WHERE USERNAME = ?";

        try {
            stmt = con.prepareStatement(update);
            stmt.setDouble(1, credit);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException ex) {;
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void clearText() {
        jtfAmt.setText("");
    }
}
