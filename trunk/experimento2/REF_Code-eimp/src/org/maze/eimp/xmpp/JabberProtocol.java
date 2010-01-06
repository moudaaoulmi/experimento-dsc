package org.maze.eimp.xmpp;

import org.maze.eimp.EimpConsts;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.Protocol;

/**
 * @see Protocol
 */
public class JabberProtocol implements Protocol {

	public JabberProtocol() {
	}

    public String getIdentifier() {
        return EimpConsts.IMP_JABBER;
    }

    public Connection createConnectionFor(Account account) {
        Connection connection = new JabberConnection();
        connection.setAccount(account);
        return connection;
    }
}
