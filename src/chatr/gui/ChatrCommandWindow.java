package chatr.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * The ChatrCommandWindow class is the command window we use to output the
 * server's messages.
 *
 * @author Anthony Benavente
 * @version 6/14/16
 */
public class ChatrCommandWindow extends JFrame {

    /**
     * This text area is where the messages are displayed.
     */
    private JTextArea messages;

    /**
     * Creates a new command window.
     */
    public ChatrCommandWindow() {
        super("Chatr - Anthony Benavente");

        setContentPane(createGui());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Creates the GUI for the window, i.e., just the message area.
     *
     * @return a JPanel used for the GUI of the application.
     */
    private JPanel createGui() {
        JPanel result = new JPanel();
        messages = new JTextArea(25, 80);
        messages.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messages);
        result.add(scrollPane);
        return result;
    }


    /**
     * Appends a string to the end of the text area followed by a new line.
     *
     * @param actionCommand the message to append to the text area.
     */
    public void appendMessage(String actionCommand) {
        messages.append(actionCommand + "\n");
    }
}
