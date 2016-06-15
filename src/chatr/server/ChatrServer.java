package chatr.server;

import chatr.server.users.ConnectedUser;

import java.util.Collection;

/**
 * The ChatrServer interface describes what different ChatrServers can do
 * (such as listen). The reason this is an interface is because TCP and UDP
 * servers listen in very different ways.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public interface ChatrServer {

    /**
     * The default port that the server opens up on if not given a port.
     */
    int DEFAULT_PORT = 8002;

    /**
     * This method should be listening for clients to connect and then handle
     * those connections.
     *
     * @throws ChatrServerException if something goes wrong in the process
     */
    void listen() throws ChatrServerException;

    /**
     * This method should return a collection of all users connected to the
     * server
     *
     * @return the collection of users connected to the server
     */
    Collection<ConnectedUser> getConnectedUsers();

    /**
     * This should get a user based on their username
     *
     * @param username the username of the user to get. If the user doesn't
     *                 exist, null should be returned.
     * @return the user corresponding to the specified username or null
     */
    ConnectedUser getUser(String username);

    /**
     * Adds the specified user to the list of connected users if they are not
     * already connected (matched on IP address/port). Also, no two users can
     * have the same username.
     *
     * @param user the user to add
     */
    void addUser(ConnectedUser user);

    /**
     * Remove the specified user. This should be used in conjunction with
     * getUser to ensure that the user we are removing exists.
     *
     * @param user the user to remove
     */
    void removeUser(ConnectedUser user);
}
