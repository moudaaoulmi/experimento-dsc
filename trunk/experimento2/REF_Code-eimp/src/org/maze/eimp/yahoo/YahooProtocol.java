package org.maze.eimp.yahoo;

import org.maze.eimp.EimpConsts;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.Protocol;

/**
 * @see Protocol
 */
public class YahooProtocol implements Protocol {

	public YahooProtocol() {
	}

    public String getIdentifier() {
        return EimpConsts.IMP_YAHOO;
    }

    public Connection createConnectionFor(Account account) {
        Connection connection = new YahooConnection();
        connection.setAccount(account);
        return connection;
    }
}
