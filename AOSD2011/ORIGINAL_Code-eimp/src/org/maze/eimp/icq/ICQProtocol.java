package org.maze.eimp.icq;

import org.maze.eimp.EimpConsts;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.Protocol;

/**
 * @see Protocol
 */
public class ICQProtocol implements Protocol {

	public ICQProtocol() {
	}

    public String getIdentifier() {
        return EimpConsts.IMP_ICQ;
    }

    public Connection createConnectionFor(Account account) {
        Connection connection = new ICQConnection();
        connection.setAccount(account);
        return connection;
    }
}
