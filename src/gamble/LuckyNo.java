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
public class LuckyNo {

    private String host = "jdbc:derby:Game";
    private Connection con;
    private PreparedStatement stmt;

    private Frame luckyFrame = new Frame();
    private JTextField jtfAmt = new JTextField();
    private JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
    private JButton jbtConfirm = new JButton("Confirm");
    private JButton jbtBack = new JButton("Back");
    private double credit;
    private String username;
    private boolean win = false;

    public LuckyNo(double credit, String username) {

        this.con = con;
        this.credit = credit;
        this.username = username;

        luckyFrame.createFrame("Lucky Number", 500, 200, 3);

        JPanel p1 = new JPanel(new GridLayout(2, 3));
        p1.add(new JLabel(this.username));
        p1.add(new JLabel());
        p1.add(new JLabel("Your credit: " + this.credit));
        p1.add(new JLabel());
        p1.add(new JLabel());
        p1.add(new JLabel());
        luckyFrame.add(p1, BorderLayout.NORTH);

        JPanel p2 = new JPanel(new GridLayout(2, 2));
        p2.add(new JLabel("Enter an amount you want to bet:"));
        p2.add(jtfAmt);
        p2.add(new JLabel("Pick your lucky number:"));
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        p2.add(slider);
        luckyFrame.add(p2);

        JPanel p3 = new JPanel();
        p3.add(jbtConfirm);
        jbtConfirm.addActionListener(new listeners());
        p3.add(jbtBack);
        jbtBack.addActionListener(new listeners());
        luckyFrame.add(p3, BorderLayout.SOUTH);
        
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
                        int selection = slider.getValue();

                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                            clearText();
                        } else if (amount > credit) {
                            JOptionPane.showMessageDialog(null, "Insufficient fund", "ERROR", JOptionPane.ERROR_MESSAGE);
                            clearText();
                        } else {
                            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to perform this action? "
                                    + "If you win you will get 5x the amount you gamble but if you lose you will lose all the amount you gamble.",
                                    "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                int result = luckyNo(amount, selection);
                                if(win){
                                    JOptionPane.showMessageDialog(null, "Congratulations! You lucky number is indeed " + result + 
                                            " and won " + amount * 5 + " credits.", "Congrats", JOptionPane.INFORMATION_MESSAGE);
                                    luckyFrame.dispose();
                                    LuckyNo ln = new LuckyNo(credit, username);
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "So sad! You lucky number is " + result + 
                                            " and you lost " + amount + " credits.", "Nice try", JOptionPane.INFORMATION_MESSAGE);
                                    luckyFrame.dispose();
                                    LuckyNo ln = new LuckyNo(credit, username);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                    clearText();
                }
            } else if (e.getSource() == jbtBack) {
                luckyFrame.dispose();
                GameMenu gm = new GameMenu(credit, username);
            }
        }
    }
    
    private int luckyNo(double amount, int selection) {
        
        Random rand = new Random(System.currentTimeMillis());
        double random = rand.nextDouble() * 10 + 1;
        
        if(selection == (int)random){
            amount *= 4;
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
