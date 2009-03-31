/*
 * Copyright 2004-2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package com.sun.j2ee.blueprints.servicelocator.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.net.URL;


import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.QueueConnectionFactory;
import javax.jms.Queue;
import javax.jms.TopicConnectionFactory;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

/**
 * This class is an implementation of the Service Locator pattern. It is
 * used to looukup resources such as EJBHomes, JMS Destinations, etc.
 * This implementation uses the "singleton" strategy and also the "caching"
 * strategy.
 * This implementation is intended to be used on the web tier and
 * not on the ejb tier.
 */
public class ServiceLocator {

    private InitialContext ic;
    private Map cache; //used to hold references to EJBHomes/JMS Resources for re-use

    private static ServiceLocator me;

    /**
     * EH - Refactored to aspect ServiceLocatorHandler.
     */       
    static {
   		me = new ServiceLocator();
    }
    
    /**
     * EH - Refactored to aspect ServiceLocatorHandler.
     */       
    private ServiceLocator() { //throws ServiceLocatorException  {
        ic = new InitialContext();
        cache = Collections.synchronizedMap(new HashMap());
    }

    static public ServiceLocator getInstance() {
      return me;
    }



    /**
     * will get the ejb Local home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public EJBLocalHome getLocalHome(String jndiHomeName) throws ServiceLocatorException {
        EJBLocalHome home = null;
        if (cache.containsKey(jndiHomeName)) {
            home = (EJBLocalHome) cache.get(jndiHomeName);
        } else {
            home = (EJBLocalHome) ic.lookup(jndiHomeName);
            cache.put(jndiHomeName, home);
        }
        return home;
    }

   /**
     * will get the ejb Remote home factory. If this ejb home factory has already been
     * clients need to cast to the type of EJBHome they desire
     *
     * @return the EJB Home corresponding to the homeName
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public EJBHome getRemoteHome(String jndiHomeName, Class className) throws ServiceLocatorException {
        EJBHome home = null;
        if (cache.containsKey(jndiHomeName)) {
            home = (EJBHome) cache.get(jndiHomeName);
        } else {
            Object objref = ic.lookup(jndiHomeName);
            Object obj = PortableRemoteObject.narrow(objref, className);
            home = (EJBHome)obj;
            cache.put(jndiHomeName, home);
        }
        return home;
    }


    /**
     * @return the factory for the factory to get queue connections from
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public  QueueConnectionFactory getQueueConnectionFactory(String qConnFactoryName)
                                                 throws ServiceLocatorException {
        QueueConnectionFactory factory = null;
        if (cache.containsKey(qConnFactoryName)) {
            factory = (QueueConnectionFactory) cache.get(qConnFactoryName);
        } else {
            factory = (QueueConnectionFactory) ic.lookup(qConnFactoryName);
            cache.put(qConnFactoryName, factory);
        }
        return factory;
    }


    /**
     * @return the Queue Destination to send messages to
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public  Queue getQueue(String queueName) throws ServiceLocatorException {
        Queue queue = null;
        if (cache.containsKey(queueName)) {
            queue = (Queue) cache.get(queueName);
        } else {
            queue =(Queue)ic.lookup(queueName);
            cache.put(queueName, queue);
        }
        return queue;
    }


   /**
     * This method helps in obtaining the topic factory
     * @return the factory for the factory to get topic connections from
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public  TopicConnectionFactory getTopicConnectionFactory(String topicConnFactoryName) throws ServiceLocatorException {
        TopicConnectionFactory factory = null;
        if (cache.containsKey(topicConnFactoryName)) {
            factory = (TopicConnectionFactory) cache.get(topicConnFactoryName);
        } else {
            factory = (TopicConnectionFactory) ic.lookup(topicConnFactoryName);
            cache.put(topicConnFactoryName, factory);
        }
        return factory;
    }

    /**
     * This method obtains the topc itself for a caller
     * @return the Topic Destination to send messages to
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public  Topic getTopic(String topicName) throws ServiceLocatorException {
        Topic topic = null;
        if (cache.containsKey(topicName)) {
            topic = (Topic) cache.get(topicName);
        } else {
            topic = (Topic)ic.lookup(topicName);
            cache.put(topicName, topic);
        }
        return topic;
    }

    /**
     * This method obtains the datasource itself for a caller
     * @return the DataSource corresponding to the name parameter
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public DataSource getDataSource(String dataSourceName) throws ServiceLocatorException {
        DataSource dataSource = null;
        if (cache.containsKey(dataSourceName)) {
           dataSource = (DataSource) cache.get(dataSourceName);
        } else {
            dataSource = (DataSource)ic.lookup(dataSourceName);
            cache.put(dataSourceName, dataSource );
        }
        return dataSource;
    }

    /**
     * @return the URL value corresponding
     * to the env entry name.
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public URL getUrl(String envName) throws ServiceLocatorException {
      URL url = null;
      url = (URL)ic.lookup(envName);
      return url;
    }

    /**
     * @return the boolean value corresponding
     * to the env entry such as SEND_CONFIRMATION_MAIL property.
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public boolean getBoolean(String envName) throws ServiceLocatorException {
      Boolean bool = null;
      bool = (Boolean)ic.lookup(envName);
      return bool.booleanValue();
    }

    /**
     * @return the String value corresponding
     * to the env entry name.
     * 
     * EH - Refactored to aspect ServiceLocatorHandler.
     */
    public String getString(String envName) throws ServiceLocatorException {
      String envEntry = null;
      envEntry = (String)ic.lookup(envName);
      return envEntry ;
    }

}

