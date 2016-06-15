package chatr.server;

import chatr.server.users.ConnectedUser;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;

/**
 * The ChatrAbstractServer class is an abstract class that implements the
 * shared functionality between all the different Chatr clients such as the
 * port.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public abstract class ChatrAbstractServer implements ChatrServer {

    /**
     * The port that this server is opened on
     */
    private int port;

    /**
     * The collection of users connected to the server.
     */
    private HashMap<String, ConnectedUser> connectedUsers;

    /**
     * Creates a new ChatrAbstractServer with the specified port
     *
     * @param port the port to open the socket on.
     */
    public ChatrAbstractServer(int port) {
        this.port = port;
        connectedUsers = new HashMap<>();
    }

    /**
     * Gets the port that this server is listening for connections on.
     *
     * @return the port that this server is opened on
     */
    public int getPort() {
        return port;
    }

    /**
     * This method returns a collection of all users connected to the server
     *
     * @return the collection of users connected to the server
     */
    public Collection<ConnectedUser> getConnectedUsers() {
        return connectedUsers.values();
    }

    /**
     * This gets a user based on their username
     *
     * @param username the username of the user to get. If the user doesn't
     *                 exist, null should be returned.
     * @return the user corresponding to the specified username or null
     */
    public ConnectedUser getUser(String username) {
        return connectedUsers.get(username);
    }

    /**
     * Adds the specified user to the list of connected users if they are not
     * already connected (matched on IP address/port). Also, no two users can
     * have the same username.
     *
     * @param user the user to add
     */
    public void addUser(ConnectedUser user) {
        if (!connectedUsers.keySet().contains(user.getUsername())) {
            connectedUsers.put(user.getUsername(), user);
        }
    }

    /**
     * Remove the specified user. This should be used in conjunction with
     * getUser to ensure that the user we are removing exists.
     *
     * @param user the user to remove
     */
    public void removeUser(ConnectedUser user) {
        if (connectedUsers.containsKey(user.getUsername())) {
            connectedUsers.remove(user.getUsername());
        }
    }
}
