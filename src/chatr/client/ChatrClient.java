package chatr.client;

import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * This interface describes all Chatr clients. The reason this is an interface
 * is so that we can easily interchange between ChatrClients that follow
 * different protocols, such as UDP vs. TCP.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public interface ChatrClient {

    /**
     * This should start the listening process of the client so that it can
     * notify listeners when it receives data from the server it connected
     * to.
     *
     * @throws ChatrClientException if something goes wrong while listening
     */
    void go() throws ChatrClientException;

    /**
     * Adds a listener to the group of listeners waiting for data to be
     * received. This list of listeners should be notified whenever this client
     * receives any sort of message from the server it is connected to
     *
     * @param listener the listener to add. This can be a lambda expression
     */
    void addActionListener(ActionListener listener);

    /**
     * Gets the list of listeners for the client.
     *
     * @return the list of listeners
     */
    Collection<ActionListener> getActionListeners();

    /**
     * Sends a message to the server that this client is connected to.
     *
     * @param message the message to send
     * @throws ChatrClientException if something goes wrong while sending the
     * message to the server. This can happen if the server goes down or the
     * connection is severed.
     */
    void sendMessage(String message) throws ChatrClientException;

}
