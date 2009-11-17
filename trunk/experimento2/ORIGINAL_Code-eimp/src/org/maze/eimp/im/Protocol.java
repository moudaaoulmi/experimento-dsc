package org.maze.eimp.im;

/**
 * Incarnation of a chat protocol.
 * 
 * @author Ringo De Smet
 */
public interface Protocol {

    /**
     * Get an identification of this protocol as a string.
     * 
     * @return a string identifying this protocol.
     */
    String getIdentifier();

    /**
     * Given the account, create a connection using this protocol.
     * 
     * @param account the login information of this user for this protocol.
     * @return a connection for this protocol
     */
    Connection createConnectionFor(Account account);
}
