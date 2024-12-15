package gamble;

import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

/**
 *
 * @author CKY
 */
public class Login {

    public static String host = "jdbc:derby:Game;create=true";
    private String user = "nbuser";
    private String pass = "nbuser";
    private Connection con;
    private ResultSet rs;
    private PreparedStatement stmt;
    private JTextField jtfUsername = new JTextField(50);
    private JPasswordField jpassword = new JPasswordField(50);
    private JButton jbtLogin = new JButton("Login");
    private JButton jbtRegister = new JButton("Register");
    private Frame loginFrame = new Frame();
    
    private JTextArea jtadata = new JTextArea();

    public Login() {

        loginFrame.createFrame("Login", 400, 130, 3);
        JPanel p1 = new JPanel(new GridLayout(2, 2));
        p1.add(new JLabel("Username:"));
        p1.add(jtfUsername);
        jtfUsername.addActionListener(new listener());
        p1.add(new JLabel("Password:"));
        p1.add(jpassword);
        jpassword.addActionListener(new listener());
        loginFrame.add(p1, BorderLayout.CENTER);
        JPanel p2 = new JPanel(new FlowLayout());
        p2.add(jbtLogin);
        jbtLogin.addActionListener(new listener());
        p2.add(jbtRegister);
        jbtRegister.addActionListener(new listener());
        loginFrame.add(p2, BorderLayout.SOUTH);

        connectDB();
    }

    private class listener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            String username = jtfUsername.getText();
            char[] passwordArr = jpassword.getPassword();
            String password = "";

            for (int i = 0; i < passwordArr.length; i++) {
                password += passwordArr[i];
            }

            if (e.getSource() == jbtLogin) {
                try {
                    rs = login(username, password);

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login Successful", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                        clearText();
                        double credit = rs.getDouble("credit");
                        MainMenu mm = new MainMenu(username, credit);
                        loginFrame.dispose();
                        disconnectDB();
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        clearText();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else if (e.getSource() == jbtRegister) {
                try {
                    rs = selectRecord(username);
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Username already existed!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        clearText();
                    } else if (username.equals("") && password.equals("")) {
                        JOptionPane.showMessageDialog(null, "Username and Password cannot be empty!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        clearText();
                    } else if (username.length() < 8 || password.length() < 8) {
                        JOptionPane.showMessageDialog(null, "Username and Password length must be at least 8!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        clearText();
                    } else {
                        register(username, password);
                        JOptionPane.showMessageDialog(null, "Register Successful", "Register Successful", JOptionPane.INFORMATION_MESSAGE);
                        clearText();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

        }
    }

    private void connectDB() {
        try {
            con = DriverManager.getConnection(host);
            createAndInitTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void disconnectDB() {
        try {
            stmt.close();
            rs.close();
            con.close();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void createAndInitTable() {
        try {
            String create = "CREATE TABLE ACCOUNT (USERNAME VARCHAR(20) NOT NULL, PASSWORD VARCHAR(20), CREDIT DOUBLE, PRIMARY KEY (USERNAME))";
            stmt = con.prepareStatement(create);
            stmt.executeUpdate();

            String insert = "INSERT INTO ACCOUNT VALUES (?,?,?)";
            stmt = con.prepareStatement(insert);
            stmt.setString(1, "admin");
            stmt.setString(2, "admin123");
            stmt.setDouble(3, 1000.0);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
    }

    private ResultSet selectRecord(String username) {
        String select = "SELECT * FROM Account WHERE username = ?";
        rs = null;
        try {
            stmt = con.prepareStatement(select);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        return rs;
    }

    private ResultSet login(String username, String password) {
        String select = "SELECT * FROM Account WHERE username = ? AND password = ?";
        rs = null;
        try {
            stmt = con.prepareStatement(select);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        return rs;
    }

    private void register(String username, String password) {
        String insert = "INSERT INTO ACCOUNT VALUES(?,?,?)";
        try {
            stmt = con.prepareStatement(insert);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setDouble(3, 0.0);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void clearText() {
        jtfUsername.setText("");
        jpassword.setText("");
    }
}
