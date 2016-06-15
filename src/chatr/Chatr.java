package chatr;

import chatr.client.ChatrClient;
import chatr.client.ChatrClientException;
import chatr.client.ChatrUdpClient;
import chatr.gui.ChatrCommandWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

/**
 * Chatr is a networked chat application written in Java. This class is the
 * entry point to the program and takes no parameters on execution.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public class Chatr {

    /**
     * This is the window we use to display the output from the server.
     */
    private ChatrCommandWindow window;

    /**
     * This is used for gathering user input.
     */
    private Scanner in;

    /**
     * This is the user's username that is displayed when the user sends out
     * messages.
     */
    private String username;

    /**
     * This is the client used to talk to the server
     */
    private ChatrClient client;

    /**
     * Creates a new Chatr application. Creating a Chatr object immediately
     * pops up the output window and asks the user for information on
     * connecting to the server.
     */
    public Chatr() {
        window = new ChatrCommandWindow();
        window.setVisible(true);
        in = new Scanner(System.in);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                client.stop();
            }
        });
    }

    /**
     * Starts the application. This creates the network client, starts it, and
     * starts the input loop.
     *
     * @throws ChatrClientException if something goes wrong
     */
    public void start() throws ChatrClientException {
        createClient();
        client.go();

        String input;
        do {
            System.out.print(username + ": ");
            input = in.nextLine();
            if (!input.equals("\\quit")) {
                try {
                    client.sendMessage(input);
                } catch (ChatrClientException e) {
                    e.printStackTrace();
                }
            }
        } while (!input.equals("\\quit"));
        exit();
    }

    /**
     * This is called to quit the application without pressing the X button.
     */
    private void exit() {
        window.setVisible(false);
        window.dispose();
        client.stop();
    }

    /**
     * This creates our client.
     */
    private void createClient() {
        boolean validHostAndPort;
        do {
            try {
                String address = prompt("Enter the host address: ");
                System.out.print("Enter the port: ");
                int port = in.nextInt();
                in.nextLine(); // Flush it out.
                username = prompt("Enter your username: ");
                client = new ChatrUdpClient(address, port, username);
                validHostAndPort = true;
            } catch (ChatrClientException e) {
                validHostAndPort = false;
            }
        } while (!validHostAndPort);

        client.addActionListener(e -> {
            window.appendMessage(e.getActionCommand());
        });
    }

    /**
     * Prompts the user to type in a line of input with the given prompt.
     *
     * @param prompt the message to show the user upon asking for input
     * @return the user's line of input
     */
    private String prompt(String prompt) {
        System.out.print(prompt);
        return in.nextLine();
    }


    /**
     * This is the entry point to the program. No arguments are used.
     *
     * @param args Unused
     */
    public static void main(String[] args) {
        Chatr app = new Chatr();
        try {
            app.start();
        } catch (ChatrClientException e) {
            e.printStackTrace();
        }
    }
}
