package chatr.server;

import java.net.SocketException;

/**
 * This class is a driver that is used for starting a ChatrServer.
 *
 * @author Anthony Benavente
 * @version 6/12/16
 */
public class ChatrServerDriver {

    /**
     * This is the index in the args array where the udp/tcp option will be
     */
    private static final int ARG_UDP_TCP = 0;

    /**
     * This is the index in the args array where the port number may be
     */
    private static final int ARG_PORT = 1;

    /**
     * This indicates that the program exited in a failure
     */
    private static final int EXIT_FAILURE = 66;

    /**
     * Starts up the ChatrServer with specified parameters.
     *
     * @param args [0] - Required argument specifying whether this is a UDP or
     *                   TCP server
     *             [1] - Optional argument specfying the port to open the
     *                   server on
     */
    public static void main(String[] args) {
        // There is 1 required argument
        if (args.length < 1) {
            usageExit();
        }

        String tcpUdp = args[ARG_UDP_TCP].toLowerCase();
        if (!(tcpUdp.equals("tcp") || tcpUdp.equals("udp"))) {
            usageExit();
        }

        try {
            int port = args.length > 1 ? Integer.parseInt(args[ARG_PORT])
                                       : ChatrServer.DEFAULT_PORT;
            ChatrServer server = new ChatrUdpServer(port);

            // Program loops in this method forever :O
            server.listen();
        } catch (SocketException | ChatrServerException e) {
            e.printStackTrace();
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid port number: " + args[ARG_PORT]);
            usageExit();
        }
    }

    /**
     * This method prints out the usage for the program and then exits.
     */
    private static void usageExit() {
        System.out.println("usage: java chatr.server.ChatrServerDriver " +
                "<udp|tcp> [<port>]");
        System.exit(EXIT_FAILURE);
    }
}
