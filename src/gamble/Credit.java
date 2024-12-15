package gamble;

import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

/**
 *
 * @author CKY
 */
public class Credit {

    private String host = "jdbc:derby:Game";
    private Connection con;
    private PreparedStatement stmt;

    private Frame creditFrame = new Frame();
    private JTextField jtfAmt = new JTextField();
    private JButton jbtWithdraw = new JButton("Withdraw");
    private JButton jbtDeposit = new JButton("Deposit");
    private JButton jbtBack = new JButton("Back");
    private double credit;
    private String username;

    public Credit(double credit, String username) {

        this.credit = credit;
        this.username = username;
        creditFrame.createFrame("Credit Manager", 400, 150, 3);

        JPanel p1 = new JPanel();
        p1.add(new JLabel("Your credit balance: " + this.credit));
        creditFrame.add(p1, BorderLayout.NORTH);

        JPanel p2 = new JPanel(new GridLayout(1, 2));
        p2.add(new JLabel("Enter an amount:"));
        p2.add(jtfAmt);
        creditFrame.add(p2, BorderLayout.CENTER);

        JPanel p3 = new JPanel();
        p3.add(jbtWithdraw);
        jbtWithdraw.addActionListener(new listeners());
        p3.add(jbtDeposit);
        jbtDeposit.addActionListener(new listeners());
        p3.add(jbtBack);
        jbtBack.addActionListener(new listeners());
        creditFrame.add(p3, BorderLayout.SOUTH);

        connectDB();
    }

    private class listeners implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == jbtWithdraw) {
                try {
                    if (jtfAmt.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter an amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        double amount = Double.parseDouble(jtfAmt.getText());

                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                            clearText();
                        } else {
                            if (amount > credit) {
                                JOptionPane.showMessageDialog(null, "Insufficient fund", "ERROR", JOptionPane.ERROR_MESSAGE);
                                clearText();
                            } else {

                                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to widthdraw?", "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    credit -= amount;
                                    updateCredit(credit, username);
                                    JOptionPane.showMessageDialog(null, "Successfully withdrew credit", "Successful", JOptionPane.INFORMATION_MESSAGE);
                                    clearText();
                                    creditFrame.dispose();
                                    MainMenu mm = new MainMenu(username, credit);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }

            } else if (e.getSource() == jbtDeposit) {
                try {
                    if (jtfAmt.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter an amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        double amount = Double.parseDouble(jtfAmt.getText());

                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid amount", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to deposit?", "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                credit += amount;
                                updateCredit(credit, username);
                                JOptionPane.showMessageDialog(null, "Successfully deposited credit", "Successful", JOptionPane.INFORMATION_MESSAGE);
                                clearText();
                                creditFrame.dispose();
                                MainMenu mm = new MainMenu(username, credit);
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }

            } else if (e.getSource() == jbtBack) {
                creditFrame.dispose();
                MainMenu mm = new MainMenu(username, credit);
            }
        }
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
