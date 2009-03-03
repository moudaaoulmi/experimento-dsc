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

package com.sun.j2ee.blueprints.petstore.tools.populate;

import javax.naming.*;
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import com.sun.j2ee.blueprints.signon.user.ejb.*;


public class UserPopulator {
  public static final String JNDI_USER_HOME = "java:comp/env/ejb/User";
  public static final String XML_USERS = "Users";
  private static final String XML_USER = "User";
  private static final String XML_ID = "User/@id";
  private static final String XML_PASSWORD = "Password";
  private UserLocalHome userHome = null;
  private String rootTag;


  public UserPopulator() {
    this(XML_USERS);
    return;
  }

  public UserPopulator(String rootTag) {
    this.rootTag = rootTag;
    return;
  }

  public XMLFilter setup(XMLReader reader) throws PopulateException {
    return new XMLDBHandler(reader, rootTag, XML_USER) {

      public void update() throws PopulateException {}

      public void create() throws PopulateException {
        createUser(getValue(XML_ID), getValue(XML_PASSWORD));
        return;
      }
    };

  }

  /**
   * EH - Refactored to aspect PetstoreToolsHandler.
   */            
  public boolean check() throws PopulateException {
      InitialContext context = new InitialContext();
      UserLocalHome userHome = (UserLocalHome) context.lookup(JNDI_USER_HOME);
      Collection users = userHome.findAllUsers();
      if ((users == null) || (users.size() == 0)) {
      	return false;
      }
      return true;
  }

  /**
   * EH - Refactored to aspect PetstoreToolsHandler.
   */            
  private UserLocal createUser(String id, String password) throws PopulateException {
      if (userHome == null) {
        InitialContext context = new InitialContext();
        userHome = (UserLocalHome) context.lookup(JNDI_USER_HOME);
      }
      UserLocal user = null;
      user = internalRemoveExistingUser(id, user);
      user = userHome.create(id, password);
      return user;
  }
  
  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect PetstoreToolsHandler.
   * Decided to separate the internal try-catch block from createUser method  
   * into another method. The reason is that it is not possible 
   * to use an advice "around" with pointcut mixing within(method createUser) 
   * and call(internal block code). 
   * Atention: its necessary to return UserLocal user, 
   * since it created a new object 
   * 
   */                
  private UserLocal internalRemoveExistingUser(String id, UserLocal user) {
    user = userHome.findByPrimaryKey(id);
    user.remove();
    return user;
  }

  /**
   * EH - Refactored to aspect PetstoreToolsHandler.
   */            
  public static void main(String[] args) {
    if (args.length <= 1) {
      String fileName = args.length > 0 ? args[0] : "User.xml";
      UserPopulator userPopulator = new UserPopulator();
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();
      parserFactory.setValidating(true);
      userPopulator.setup(parserFactory.newSAXParser().getXMLReader()).parse(new InputSource(fileName));
      System.exit(0);
    }
    System.err.println("Usage: " + UserPopulator.class.getName() + " [file-name]");
    System.exit(1);
  }
}

