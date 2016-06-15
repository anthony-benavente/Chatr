package chatr.server.users;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * The ConnectedUdpUser class
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public class ConnectedUdpUser extends ConnectedUser {

    /**
     * The socket we can use to communicate with the user
     */
    private DatagramSocket sendingSocket;

    /**
     * Creates a new ConnectedUdpUser with the specified parameters.
     *
     * @param username the username of the user
     * @param address  the address of the user
     * @param port     the port of the user
     * @param sendingSocket the socket we can use to send the user data
     */
    public ConnectedUdpUser(String username,
                            InetAddress address,
                            int port,
                            DatagramSocket sendingSocket) {
        super(username, address, port);
        this.sendingSocket = sendingSocket;
    }

    /**
     * Sends a message to the connected user through a DatagramPacket using the
     * socket above.
     *
     * @param message the message to send to the user.
     * @throws IOException if something goes wrong while sending.
     */
    @Override
    public void send(String message) throws IOException {
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(),
                message.length(), getAddress(), getPort());
        sendingSocket.send(outPacket);
    }
}
