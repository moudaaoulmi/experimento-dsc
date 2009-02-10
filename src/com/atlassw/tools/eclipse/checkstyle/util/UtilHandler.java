package com.atlassw.tools.eclipse.checkstyle.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;

public class UtilHandler extends GeneralException{
    
    private static DocumentBuilderFactory sDocBuilderFactory = DocumentBuilderFactory.newInstance();
    
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
    
}
