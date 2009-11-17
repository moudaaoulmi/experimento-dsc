package org.maze.eimp.rendezvous;

import org.maze.eimp.EimpConsts;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.Protocol;

/**
 * @see Protocol
 */
public class RendezVousProtocol implements Protocol {

	public RendezVousProtocol() {
	}

    public String getIdentifier() {
        return EimpConsts.IMP_RENDEZVOUS;
    }

    public Connection createConnectionFor(Account account) {
        Connection connection = new RendezVousConnection();
        connection.setAccount(account);
        return connection;
    }
}
