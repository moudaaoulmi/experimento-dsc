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

package com.sun.j2ee.blueprints.xmldocuments;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


public class CustomEntityResolver implements EntityResolver {
  public static final boolean TRACE = false;
  public static final String ENTITY_CATALOG
  = "/com/sun/j2ee/blueprints/xmldocuments/rsrc/EntityCatalog.properties";
  /**
   * Updated during EH - Refactoring to aspect XmlDocumentsHandler.
   */                       
  public Properties entityCatalog = null;
  private EntityResolver parentResolver = null;


  public CustomEntityResolver() {
    this((EntityResolver) null);
    return;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                     
  public CustomEntityResolver(EntityResolver parentResolver) {
    this.parentResolver = parentResolver;
    entityCatalog = new Properties();
    InputStream stream = getClass().getResourceAsStream(ENTITY_CATALOG);
    if (stream != null) {
      if (TRACE) {
        System.err.println("Load resource from: " + getClass().getResource(ENTITY_CATALOG));
      }
      entityCatalog.load(stream);
    } else {
      if (TRACE) {
        System.err.println("Can't access resource: " + ENTITY_CATALOG);
      }
    }
    return;
  }

  public CustomEntityResolver(URL entityCatalogURL) {
    this(entityCatalogURL, null);
    return;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                       
  public CustomEntityResolver(URL entityCatalogURL, EntityResolver parentResolver) {
    this(parentResolver);
    if (entityCatalogURL != null) {
      entityCatalog = new Properties(entityCatalog);
      entityCatalog.load(entityCatalogURL.openStream());
    }
    return;
  }

  public CustomEntityResolver(Properties entityCatalog) {
    this(entityCatalog, null);
    return;
  }

  public CustomEntityResolver(Properties entityCatalog, EntityResolver parentResolver) {
    this(parentResolver);
    this.entityCatalog = new Properties(this.entityCatalog);
    this.entityCatalog.putAll(entityCatalog);
    return;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                         
  private InputSource resolveEntityFromURL(String entityURL) throws IOException {
    URL entityURLURL = null;
    InputStream entityURLStream = null;
    // Is it a wellformed URL?
    entityURLURL = new URL(entityURL);
    if (entityURLURL != null) { // Is a wellformed URL
    	 // Try to open the URL
    	entityURLStream = internalOpenStream(entityURL, entityURLURL);  
    }
    if (entityURLStream == null) { // Not a URL or could not be open
      if (TRACE) {
        System.err.print("entityURL: " + entityURL + " is it a resource? ");
      }
      //is it a resource path?      
      entityURLStream = internalGetResourceAsStream(entityURL, entityURLURL);
    }
    if (entityURLStream != null) { // Is a readable URL or resource
      InputSource source = new InputSource(entityURLStream);
      source.setSystemId(entityURL);
      return source;
    }
    return null;
  }
  
  /**
   * Created during EH - Refactoring to aspect XmlDocumentsHandler.
   */                           
  private InputStream internalOpenStream(String entityURL, URL entityURLURL) {
  	return entityURLURL.openStream(); // Try to open the URL
  }

  /**
   * Created during EH - Refactoring to aspect XmlDocumentsHandler.
   */                             
  private InputStream internalGetResourceAsStream(String entityURL, URL entityURLURL) {
  	InputStream entityURLStream = null;
    String resourcePath = entityURLURL != null ? entityURLURL.getPath() : entityURL;
    entityURLStream = getClass().getResourceAsStream(resourcePath);
    if (TRACE) {
      System.err.println(entityURLStream != null ? "Yes" : "No");
      if (entityURLStream != null) {
        System.err.println("Load resource from: " + getClass().getResource(resourcePath));
      }
    }        
    return entityURLStream;
  }
  
  public String mapEntityURI(String entityURI) {
    if (TRACE) {
      System.err.println("mapEntityURI: " + entityURI);
    }
    if (entityCatalog != null) {
      if (entityURI != null) {
        String entityURL = entityCatalog.getProperty(entityURI);
        if (TRACE) {
          System.err.println("mapEntityURI: " + entityURI + " mapped to: " + entityURL);
        }
        return entityURL;
      }
    }
    return null;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                           
  public InputSource resolveEntity(String entityURI, String entityURL) /*throws IOException, SAXException*/ {
    if (TRACE) {
      System.err.println("Resolving: " + entityURI + " " + entityURL);
    }
    InputSource source = null;
    source = internalResolveEntity(entityURI, entityURL);    
    // Try first to map its URI using the entity catalog
    if (entityURI != null) {
      String mappedEntityURL = mapEntityURI(entityURI);
      if (mappedEntityURL != null) {
        source = resolveEntityFromURL(mappedEntityURL);
        if (source != null) {
          return source;
        }
      }
    }
    // Try then to access the entity using its URL
    if (entityURL != null) {
      source = resolveEntityFromURL(entityURL);
      if (source != null) {
        return source;
      }
    }
    return null; // Let the default entity resolver take care of it
  }
  
  /**
   * Created during EH - Refactoring to aspect XmlDocumentsHandler.
   */                               
  private InputSource internalResolveEntity(String entityURI, String entityURL) {
  	InputSource source = null;
    if (parentResolver != null) {
      source = parentResolver.resolveEntity(entityURI, entityURL);
      if (source != null) {
        if (TRACE) {
          System.err.println("Entity resolved by parent resolver: " + entityURI + " " + entityURL + ": " + source);
        }
        return source;
      }
    }
    return source;
  }
}

