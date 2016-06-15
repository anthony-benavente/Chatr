package chatr.server.users;

import java.io.IOException;
import java.net.InetAddress;

/**
 * The ConnectedUser class is an abstract class that holds information
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public abstract class ConnectedUser {

    /**
     * The username of the connected user.
     */
    private String username;

    /**
     * The IP address of the connected user.
     */
    private InetAddress address;

    /**
     * The port that the connected user is connected through.
     */
    private int port;

    /**
     * Creates a new ConnectedUser with the specified parameters.
     *
     * @param username the username of the user
     * @param address  the address of the user
     * @param port     the port of the user
     */
    public ConnectedUser(String username, InetAddress address, int port) {
        this.username = username;
        this.address = address;
        this.port = port;
    }

    /**
     * This is an abstract method that should be implemented in the different
     * ConnectedUser implementations. This method should send a message to the
     * specified user.
     *
     * @param message the message to send to the user.
     * @throws IOException if something goes wrong while sending the message
     */
    public abstract void send(String message) throws IOException;


    /**
     * Gets the username of the user
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the address of the user.
     *
     * @return the address of the user.
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Gets the port of the user.
     *
     * @return the port of the user.
     */
    public int getPort() {
        return port;
    }

    /**
     * Prints out a more reading-friendly string representing this user.
     *
     * @return the string representing this user [username(address:port)]
     */
    @Override
    public String toString() {
        return String.format("%s(%s:%d)", getUsername(),
                getAddress().getHostName(), getPort());
    }
}
