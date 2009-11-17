package org.maze.eimp.aim;

import org.maze.eimp.EimpConsts;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.Protocol;

/**
 * @see Protocol
 */
public class AIMProtocol implements Protocol {

	public AIMProtocol() {
	}

    public String getIdentifier() {
        return EimpConsts.IMP_AIM;
    }

    public Connection createConnectionFor(Account account) {
        Connection connection = new AIMConnection();
        connection.setAccount(account);
        return connection;
    }
}
