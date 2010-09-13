package org.maze.eimp;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.Protocol;

/**
 * Access to all the available chat protocols. This class will look up the
 * registered chat protocols. Registering a chat protocol is done by defining a
 * chat extension in the plugin descriptor.
 * 
 * @author Ringo De Smet
 */
public class ProtocolManager {

	private static final String PROTOCOL_EXTENSION_POINT =
		"protocol";

	Map protocols;

	public ProtocolManager() throws CoreException {
		this.protocols = new HashMap();
		this.lookupProtocols();
	}

	public Protocol[] getAvailableProtocols() throws CoreException {
		return (Protocol[]) this.protocols.values().toArray(
			new Protocol[this.protocols.size()]);
	}

	void lookupProtocols() throws CoreException {
		IExtensionPoint protocolExtensionPoint =
			eimpPlugin.getDefault().getDescriptor().getExtensionPoint(
				PROTOCOL_EXTENSION_POINT);
		IExtension[] registeredProtocols =
			protocolExtensionPoint.getExtensions();
		for (int protocolIndex = 0;
			protocolIndex < registeredProtocols.length;
			protocolIndex++) {
			IConfigurationElement[] configElements =
				registeredProtocols[protocolIndex].getConfigurationElements();
			for(int configIndex = 0; configIndex < configElements.length; configIndex++) {
				Protocol protocol = (Protocol) configElements[configIndex].createExecutableExtension("class");
                this.addProtocol(protocol);
			}
		}
	}

    void addProtocol(Protocol protocol) {
        this.protocols.put(protocol.getIdentifier(), protocol);
    }

    public Connection createConnectionFor(Account account) {
        Protocol correspondingProtocol = this.resolveProtocol(account.getType());
        return correspondingProtocol.createConnectionFor(account);
    }

    Protocol resolveProtocol(String protocolIdentifier) {
        return (Protocol) this.protocols.get(protocolIdentifier);
    }
}
