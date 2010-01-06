package org.maze.eimp.msn;

import org.maze.eimp.EimpConsts;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.Protocol;

/**
 * @see Protocol
 */
public class MSNProtocol implements Protocol {

	public MSNProtocol() {
	}

    public String getIdentifier() {
        return EimpConsts.IMP_MSN;
    }

    public Connection createConnectionFor(Account account) {
        Connection connection = new MSNConnection();
        connection.setAccount(account);
        return connection;
    }
}
