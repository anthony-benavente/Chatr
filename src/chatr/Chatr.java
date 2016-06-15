package chatr;

import chatr.client.ChatrClient;
import chatr.client.ChatrClientException;
import chatr.client.ChatrUdpClient;
import chatr.gui.ChatrCommandWindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

/**
 * TODO: Describe me
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public class Chatr {

    private ChatrCommandWindow window;

    private Scanner in;

    private String username;

    private ChatrClient client;

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

    private void exit() {
        window.setVisible(false);
        window.dispose();
        client.stop();
    }

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

    private String prompt(String prompt) {
        System.out.print(prompt);
        return in.nextLine();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public static void main(String[] args) {
        Chatr app = new Chatr();
        try {
            app.start();
        } catch (ChatrClientException e) {
            e.printStackTrace();
        }
    }
}
