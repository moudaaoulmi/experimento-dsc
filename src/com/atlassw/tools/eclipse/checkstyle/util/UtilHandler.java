package com.atlassw.tools.eclipse.checkstyle.util;


import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

public class UtilHandler{
    
    private static DocumentBuilderFactory sDocBuilderFactory = DocumentBuilderFactory.newInstance();
    
    public void verifyTextHandler(boolean doit){
        doit = false;
    }
    
    //catch (Exception e1)
    public Point shellActivatedHandler(){
        return new Point(0, 0);
    }

    public Rectangle shellActivatedHandler2(Shell shell){
        return shell.getBounds();
    }
    
    public DocumentBuilder getDocumentBuilderHandler() throws ParserConfigurationException{
        return sDocBuilderFactory.newDocumentBuilder();
    }
    
    public void getHandler(IOException e) throws CheckstylePluginException{
        CheckstylePluginException.rethrow(e);
    }
}
