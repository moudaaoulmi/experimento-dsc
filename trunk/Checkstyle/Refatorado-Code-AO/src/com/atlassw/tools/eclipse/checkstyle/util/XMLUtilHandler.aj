/*
 * 25th, November, 2008
 */
package com.atlassw.tools.eclipse.checkstyle.util;

import java.util.EmptyStackException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author juliana
 *
 */
public privileged aspect XMLUtilHandler {
    
  pointcut getDocumentBuilderHandler(): execution(* XMLUtil.getDocumentBuilder(..)) ;
  
  DocumentBuilder around() throws ParserConfigurationException : getDocumentBuilderHandler() {
      DocumentBuilder c = null;
          try{
              c = proceed();
          }catch (EmptyStackException e)
          {
              XMLUtil obj = (XMLUtil) thisJoinPoint.getThis();
              c =   obj.createDocumentBuilder();
          }
          return c;
  }

}
