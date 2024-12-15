
package gamble;

import javax.swing.*;

/**
 *
 * @author CKY
 */

public class Frame extends JFrame {
    
    public void createFrame(String title, int width, int height, int closeOperation) {
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(closeOperation);
        setVisible(true);
    }
    
    public void createFrame(String title) {
        setTitle(title);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
