package chatr.gui.swing;

import chatr.client.ChatrClient;
import chatr.client.ChatrClientException;
import chatr.client.ChatrUdpClient;
import chatr.server.ChatrServer;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.net.UnknownHostException;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * TODO: Describe me
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public class ChatrWindow extends JFrame {

    private ChatrServer dedicatedServer;

    private ChatrClient client;

    private JTextArea messageArea;

    private JTextField sendMessageBox;

    public ChatrWindow() {
        super("Chatr - Anthony Benavente");
        setContentPane(createGui());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setJMenuBar(createMenu());
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createServerMenu());
        menuBar.add(createAboutMenu());

        return menuBar;
    }

    private JMenu createServerMenu() {
        JMenu serverMenu = new JMenu("Server");
        serverMenu.add(createJMenuItem("Connect", e -> {
            String username = showInputDialog(ChatrWindow.this,
                    "Enter your username:");
            String hostname = showInputDialog(ChatrWindow.this,
                    "Enter the hostname for the server:");
            int port = -1;
            try {
                port = Integer.parseInt(showInputDialog(
                        ChatrWindow.this,
                        "Enter the port number to connect to:"));
            } catch (NumberFormatException nfe) {
                showMessageDialog(ChatrWindow.this,
                        "The port number must be an integer!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            if (port > 0) {
                try {
                    client = new ChatrUdpClient(hostname, port, username);
                    client.addActionListener(e2 ->
                            messageArea.append(e2.getActionCommand() + "\n"));
                    client.go();
                } catch (ChatrClientException e1) {
                    e1.printStackTrace();
                }
            }
        }));
        serverMenu.add(createJMenuItem("Start Server", e -> {
            // Ask for the port number to open on
        }));
        serverMenu.addSeparator();
        serverMenu.add(createJMenuItem("Quit", e -> {
            setVisible(false);
            ChatrWindow.this.dispose();
        }));
        return serverMenu;
    }

    private JMenu createAboutMenu() {
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.add(createJMenuItem("About Chatr", e -> {
            // Open dialog box for information about chatr
        }));
        return aboutMenu;
    }

    private JMenuItem createJMenuItem(String title,
                                      ActionListener action) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(action);
        item.setMnemonic(title.charAt(0));
        return item;
    }

    private JPanel createGui() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(createMessageArea(), BorderLayout.CENTER);
        mainPanel.add(createTextArea(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JComponent createTextArea() {
        JPanel messageArea = new JPanel(new BorderLayout());
        JButton sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.addActionListener(e -> {
            try {
                client.sendMessage(sendMessageBox.getText());
                sendMessageBox.setText("");
            } catch (ChatrClientException e1) {
                showMessageDialog(this, "Failed to send message to server!");
                e1.printStackTrace();
            }
        });
        messageArea.add(sendButton, BorderLayout.WEST);

        sendMessageBox = new JTextField();
        messageArea.add(sendMessageBox, BorderLayout.CENTER);
        return messageArea;
    }

    private JComponent createMessageArea() {
        messageArea = new JTextArea(40, 30);
        messageArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setPreferredSize(new Dimension(480, 640));
        return scrollPane;
    }

    public void start() {
        setVisible(true);
    }
}
