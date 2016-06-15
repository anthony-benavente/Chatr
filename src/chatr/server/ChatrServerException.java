package chatr.server;

/**
 * This class is an almost useless class that wraps every exception that occurs
 * within the logic of the ChatrServer so that we don't have to put many
 * Exceptions in the throws clause of each method in the interface. Without
 * this we would have methods like:
 *
 *     void doSomething() throws IOException, FileNotFoundException, ... etc.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public class ChatrServerException extends Exception {

    /**
     * The exception that this exception is wrapped around
     */
    private Exception wrapped;

    /**
     * Creates a ChatrServerException that wraps another exception that was
     * thrown
     *
     * @param wrapped the "real" exception that occurred
     */
    public ChatrServerException(Exception wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Gets the "real" exception that occurred.
     *
     * @return the "real" exception that occurred
     */
    public Exception getWrapped() {
        return wrapped;
    }

}
