package chatr.server;

import chatr.server.users.ConnectedUdpUser;
import chatr.server.users.ConnectedUser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

/**
 * The ChatrUdpServer is the UDP implementation of the ChatrServer.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public class ChatrUdpServer extends ChatrAbstractServer {

    /**
     * The most number of bytes that a client can send us.
     */
    private static final int LARGEST_MSG = 2048;

    /**
     * This is the socket object we use to wait for people to send us data.
     */
    private DatagramSocket socket;

    /**
     * Creates a new ChatrUdpServer open on the default port specified in the
     * ChatrServer interface.
     *
     * @throws SocketException if the socket can't open on the default port
     */
    public ChatrUdpServer() throws SocketException {
        this(DEFAULT_PORT);
    }

    /**
     * Creates and opens a UDP server at the specified port. If the server
     * can't be opened at the port, a SocketException is thrown.
     *
     * @param port the port to open the server at
     * @throws SocketException if the server cannot be opened on the specified
     * port
     */
    public ChatrUdpServer(int port) throws SocketException {
        super(port);
        socket = new DatagramSocket(port);
    }

    /**
     * This method starts the server loop.
     *
     * @throws ChatrServerException if anything goes wrong while trying to
     * listen for data
     */
    @Override
    public void listen() throws ChatrServerException {
        // The largest message we can is dictated by the constant LARGEST_MSG
        byte[] data;

        // We use this packet to "catch" what data comes through the socket
        DatagramPacket packet;

        while (true) {
            try {
                packet = new DatagramPacket(new byte[LARGEST_MSG], LARGEST_MSG);
                socket.receive(packet);
                handlePacket(packet);
            } catch (IOException e) {
                // We had trouble receiving this packet for some reason
                System.err.println("Failed to retrieve packet: " +
                        e.getMessage());
                throw new ChatrServerException(e);
            }
        }
    }

    /**
     * This method handles what happens when the server recieves a packet.
     *
     * @param packet the packet that was received
     */
    private void handlePacket(DatagramPacket packet) {
        byte[] data = packet.getData();

        /*
        Parse the data out. The first byte is the opcode, i.e., this tells us
        what we need to do to parse the data.
        */
        int opCode = data[0];

        InetAddress sender = packet.getAddress();
        int arrivingPort = packet.getPort();
        String username = getUsername(data);

        // If the opcode is 0, we have to create a new user.
        ConnectedUser user = getConnectedUser(opCode, sender,
                arrivingPort, username);

        if (opCode == 0) {
            // This is a login message
            addUser(user);
            System.out.println(user + " connected!");
        } else if (opCode == 1) {
            // This is a logout message
            removeUser(user);
            System.out.println(user.toString() + " left the server.");
        } else {
            /*
            Convert the data into a string. Trim to get rid of trailing white
            space. Then send what we received to every client.
            */
            String out = user + ": " +
                    new String(packet.getData()).trim().split(";")[1];
            broadcast(out);
        }
    }

    /**
     * Gets the connected user based on the op code.
     *
     * @param opCode        the op code that determines how we obtain this
     *                      user. If the op code is 0, we create a new one.
     * @param sender        the address of the connected user
     * @param arrivingPort  the port that the connected user arrived through
     * @param username      the desired username for the user
     * @return the created/retrieved user
     */
    private ConnectedUser getConnectedUser(int opCode,
                                           InetAddress sender,
                                           int arrivingPort,
                                           String username) {
        ConnectedUser user;
        if (opCode == 0) {
            user = new ConnectedUdpUser(username, sender, arrivingPort, socket);
        } else {
            user = getUser(username);
        }
        return user;
    }

    /**
     * Sends a string message to all connected users.
     *
     * @param out the message to send out to all connected users.
     */
    private void broadcast(String out) {
        for (ConnectedUser user : getConnectedUsers()) {
            try {
                user.send(out);
                System.out.println("Sent message to " + user);
            } catch (IOException e) {
                System.err.println("Couldn't contact user " + user + ": " +
                        e.getMessage());
            }
        }
    }

    /**
     * Gets the username out of data received by a client.
     *
     * @param data the data we received from the client
     * @return the username pulled from the data
     */
    private String getUsername(byte[] data) {
        String dataStr = new String(Arrays.copyOfRange(data, 1, data.length));
        dataStr = dataStr.trim();
        return dataStr.split(";")[0];
    }
}
