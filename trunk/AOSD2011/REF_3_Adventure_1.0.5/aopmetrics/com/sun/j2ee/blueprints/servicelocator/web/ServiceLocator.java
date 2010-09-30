/* Copyright 2004 Sun Microsystems, Inc.  All rights reserved.  You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: 
 http://adventurebuilder.dev.java.net/LICENSE.txt
 $Id: ServiceLocator.java,v 1.4 2004/07/30 23:59:21 inder Exp $ */

package com.sun.j2ee.blueprints.servicelocator.web;

import java.util.*;
import java.net.*;
import javax.ejb.*;
import javax.jms.*;
import javax.naming.*;
import javax.rmi.*;
import javax.sql.*;
import javax.transaction.*;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

/**
 * This class is an implementation of the Service Locator pattern. It is used to
 * looukup resources such as EJBHomes, JMS Destinations, etc. This
 * implementation uses the "singleton" strategy and also the "caching" strategy.
 * This implementation is intended to be used on the web tier and not on the ejb
 * tier.
 */
public class ServiceLocator {

	private InitialContext ic;
	// used to hold references to EJBHomes/JMS Resources for re-use
	private Map cache = Collections.synchronizedMap(new HashMap());

	private static ServiceLocator instance = new ServiceLocator();

	public static ServiceLocator getInstance() {
		return instance;
	}

	private ServiceLocator() throws ServiceLocatorException {
		ic = new InitialContext();
	}

	/**
	 * will get the ejb Local home factory. If this ejb home factory has already
	 * been clients need to cast to the type of EJBHome they desire
	 * 
	 * @return the EJB Home corresponding to the homeName
	 */
	public EJBLocalHome getLocalHome(String jndiHomeName)
			throws ServiceLocatorException {
		EJBLocalHome home = (EJBLocalHome) cache.get(jndiHomeName);
		if (home == null) {
			home = internalGetLocalHome(jndiHomeName, home);
		}
		return home;
	}

	private EJBLocalHome internalGetLocalHome(String jndiHomeName,
			EJBLocalHome home) throws ServiceLocatorException {
		home = (EJBLocalHome) ic.lookup(jndiHomeName);
		cache.put(jndiHomeName, home);
		return home;
	}

	/**
	 * will get the ejb Remote home factory. If this ejb home factory has
	 * already been clients need to cast to the type of EJBHome they desire
	 * 
	 * @return the EJB Home corresponding to the homeName
	 */
	public EJBHome getRemoteHome(String jndiHomeName, Class className)
			throws ServiceLocatorException {
		EJBHome home = (EJBHome) cache.get(jndiHomeName);
		if (home == null) {
			home = internalGetRemoteHome(jndiHomeName, className, home);
		}
		return home;
	}

	private EJBHome internalGetRemoteHome(String jndiHomeName, Class className,
			EJBHome home) throws ServiceLocatorException {
		Object objref = ic.lookup(jndiHomeName);
		Object obj = PortableRemoteObject.narrow(objref, className);
		home = (EJBHome) obj;
		cache.put(jndiHomeName, home);
		return home;
	}

	/**
	 * @return the factory for the factory to get queue connections from
	 */
	public ConnectionFactory getJMSConnectionFactory(String jmsConnFactoryName)
			throws ServiceLocatorException {
		ConnectionFactory factory = (ConnectionFactory) cache
				.get(jmsConnFactoryName);
		if (factory == null) {
			factory = internalGetJMSConnectionFactory(jmsConnFactoryName,
					factory);
		}
		return factory;
	}

	private ConnectionFactory internalGetJMSConnectionFactory(
			String jmsConnFactoryName, ConnectionFactory factory)
			throws ServiceLocatorException {
		factory = (ConnectionFactory) ic.lookup(jmsConnFactoryName);
		cache.put(jmsConnFactoryName, factory);
		return factory;
	}

	/**
	 * @return the Queue Destination to send messages to
	 */
	public javax.jms.Destination getJMSDestination(String destName)
			throws ServiceLocatorException {
		javax.jms.Destination dest = (javax.jms.Destination) cache
				.get(destName);
		if (dest == null) {
			dest = internalGetJMSDestination(destName, dest);
		}
		return dest;
	}

	private javax.jms.Destination internalGetJMSDestination(String destName,
			javax.jms.Destination dest) throws ServiceLocatorException {
		dest = (javax.jms.Destination) ic.lookup(destName);
		cache.put(destName, dest);
		return dest;
	}

	/**
	 * This method obtains the datasource itself for a caller
	 * 
	 * @return the DataSource corresponding to the name parameter
	 */
	public DataSource getDataSource(String dataSourceName)
			throws ServiceLocatorException {
		DataSource dataSource = (DataSource) cache.get(dataSourceName);
		if (dataSource == null) {
			dataSource = internalGetDataSource(dataSourceName, dataSource);
		}
		return dataSource;
	}

	private DataSource internalGetDataSource(String dataSourceName,
			DataSource dataSource) throws ServiceLocatorException {
		dataSource = (DataSource) ic.lookup(dataSourceName);
		cache.put(dataSourceName, dataSource);
		return dataSource;
	}

	/**
	 * This method obtains the UserTransaction itself for a caller
	 * 
	 * @return the UserTransaction corresponding to the name parameter
	 */
	public UserTransaction getUserTransaction(String utName)
			throws ServiceLocatorException {
		return (UserTransaction) ic.lookup(utName);
	}

	/**
	 * @return the URL value corresponding to the env entry name.
	 */
	public URL getUrl(String envName) throws ServiceLocatorException {
		return (URL) ic.lookup(envName);
	}

	/**
	 * @return the boolean value corresponding to the env entry such as
	 *         SEND_CONFIRMATION_MAIL property.
	 */
	public boolean getBoolean(String envName) throws ServiceLocatorException {
		return ((Boolean) ic.lookup(envName)).booleanValue();
	}

	/**
	 * @return the String value corresponding to the env entry name.
	 */
	public String getString(String envName) throws ServiceLocatorException {
		return (String) ic.lookup(envName);
	}
}
