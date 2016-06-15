package chatr.client;

import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

/**
 * The ChatrAbstractClient class is an abstract class that implements the
 * shared functionality between all the different Chatr clients such as
 * event listeners, host address, and port.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public abstract class ChatrAbstractClient implements ChatrClient {

    /**
     * The username to use when connecting to the server.
     */
    private String username;

    /**
     * This address of the machine we are connecting to.
     */
    private InetAddress host;

    /**
     * The port of the machine we are connecting to.
     */
    private int port;

    /**
     * The list of listeners that get notified whenever an event occurs
     * within this client.
     */
    private List<ActionListener> actionListeners;

    /**
     * Creates a new ChatrAbstractClient with the specified hostname and port.
     *
     * @param hostname the address of the machine we are connecting to
     * @param port     the port of the machine we are connecting to
     * @param username the username to use when connecting to the server
     * @throws ChatrClientException if the hostname:port could not be found
     */
    public ChatrAbstractClient(String hostname, int port, String username)
            throws ChatrClientException {
        this.port = port;
        this.username = username;
        actionListeners = new LinkedList<>();
        try {
            host = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            throw new ChatrClientException(e);
        }
    }

    /**
     * Gets the address of the machine we are connecting to.
     *
     * @return the address of the machine we are connecting to.
     */
    public InetAddress getHost() {
        return host;
    }

    /**
     * Gets the port of the machine we are connecting to.
     *
     * @return the port of the machine we are connecting to.
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the username we are using while connected to the server.
     *
     * @return the username we are using while connected to the server.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Adds the listener to the list of listeners that are notified whenever
     * an event occurs.
     *
     * @param listener the listener to add. This can be a lambda expression
     */
    @Override
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    /**
     * Gets the list of listeners that can be traversed to notify them of
     * events
     *
     * @return the list of listeners.
     */
    @Override
    public List<ActionListener> getActionListeners() {
        return actionListeners;
    }
}
