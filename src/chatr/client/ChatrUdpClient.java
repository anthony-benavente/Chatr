package chatr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This class is the UDP implementation of the ChatrClient.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public class ChatrUdpClient extends ChatrAbstractClient{

    /**
     * The socket we use to communicate with the server
     */
    private DatagramSocket socket;

    /**
     * Creates a new ChatrUdpClient with the specified information.
     *
     * @param hostname the address of the machine we are connecting to
     * @param port     the port of the machine we are connecting to
     * @param username the username to use when connecting to the server
     * @throws ChatrClientException see the wrapped exception to find what went
     * wrong
     */
    public ChatrUdpClient(String hostname, int port, String username)
            throws ChatrClientException {
        super(hostname, port, username);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new ChatrClientException(e);
        }
    }

    /**
     * Starts the ChatrClient. This entails:
     *
     *   1. Starting a thread to listen for messages from the server we are
     *      connected to
     *
     *   2. Sending a login message to the server and verifying that we are
     *      logged in
     *
     * @throws ChatrClientException if something goes wrong
     */
    @Override
    public void go() throws ChatrClientException {
        // Start the client listener to listen if the server tells us stuff
        new Thread(new ClientListener()).start();

        // Send a login message to the server
        login();
    }

    /**
     * Sends a login message to the server ("\0" + username)
     *
     * @throws ChatrClientException if something goes wrong when sending the
     * packet
     */
    private void login() throws ChatrClientException {
        // TODO: Verify that the login was sent and acknowledged
        try {
            socket.send(buildPacket(("\0" + getUsername()).getBytes()));
        } catch (IOException e) {
            throw new ChatrClientException(e);
        }
    }

    /**
     * Sends a logout message to the server ("\1" + username)
     *
     * @throws ChatrClientException if something goes wrong when sending the
     * packet
     */
    public void logout() throws ChatrClientException {
        try {
            socket.send(buildPacket(("\1" + getUsername()).getBytes()));
        } catch (IOException e) {
            throw new ChatrClientException(e);
        }
    }

    /**
     * Sends a message to the server ("\2" + username + ";" + message)
     *
     * @param msg The message (as a string) to send to the server
     * @throws ChatrClientException if something goes wrong when sending the
     * packet
     */
    @Override
    public void sendMessage(String msg) throws ChatrClientException {
        try {
            String toSend = "\2" + getUsername() + ";" + msg;
            socket.send(buildPacket((toSend).getBytes()));
        } catch (IOException e) {
            throw new ChatrClientException(e);
        }
    }

    /**
     * Builds an outgoing packet with the specified data
     *
     * @param data this array of bytes is the data we are sending to the
     *             connected server (getHost():getPort())
     * @return the built DatagramPacket that is ready to get sent.
     */
    private DatagramPacket buildPacket(byte[] data) {
        return new DatagramPacket(data, data.length, getHost(), getPort());
    }

    /**
     * This mini-class is used to start a thread that notifies listeners
     * whenever this client recieves data.
     */
    private class ClientListener implements Runnable {
        @Override
        public void run() {
            try {
                // This buffer gets filled with the information received
                byte[] buf = new byte[2048];

                // This is the packet we will use to catch data from the server
                DatagramPacket recv = new DatagramPacket(buf, buf.length);

                // Keep going while we aren't interrupted
                while (!Thread.currentThread().isInterrupted()) {
                    socket.receive(recv);

                    /*
                    Let every listener know that we got data and send the
                    data we received as the "command" of the action. Yes
                    this is kind of hacky...
                    */
                    for (ActionListener listener : getActionListeners()) {
                        listener.actionPerformed(new ActionEvent(this, 0,
                                new String(buf).trim()));
                    }

                    /*
                    Fill the buffer with 0's so we don't accidentally garble
                    the information we received.
                    */
                    for (int i = 0; i < buf.length && buf[i] != 0; i++) {
                        buf[i] = 0;
                    }

                    Thread.sleep(1); // Sleep to see if we are interrupted
                }
            } catch (InterruptedException ignored) {
            } catch (IOException e) {
                System.err.println("Something went wrong while listening..."
                        + e.getMessage());
            }
        }
    }
}
