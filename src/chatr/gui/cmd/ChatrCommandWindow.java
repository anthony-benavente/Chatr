package chatr.gui.cmd;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * TODO: Describe me
 *
 * @author Anthony Benavente
 * @version 6/14/16
 */
public class ChatrCommandWindow extends JFrame {

    private JTextArea messages;

    public ChatrCommandWindow() {
        super("Chatr - Anthony Benavente");

        setContentPane(createGui());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel createGui() {
        JPanel result = new JPanel();
        messages = new JTextArea(25, 80);
        messages.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messages);
        result.add(scrollPane);
        return result;
    }


    public void appendMessage(String actionCommand) {
        messages.append(actionCommand + "\n");
    }
}
