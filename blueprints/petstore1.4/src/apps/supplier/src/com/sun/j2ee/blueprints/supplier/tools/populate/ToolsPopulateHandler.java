package com.sun.j2ee.blueprints.supplier.tools.populate;

import java.io.IOException;
import java.net.URL;



public class ToolsPopulateHandler {

	public boolean checkHandler(){
		return false;
	}
	
	public void createInventoryHandler(Exception e){
		
	}
	
	public void createInventory2Handler(Exception e) throws PopulateException{
		  throw new PopulateException ("Could not create: " + e.getMessage(), e);	
	}
	
	public void populate1Handler(PopulateException e){
		
	}
	
	public void populate2Handler(Exception e) throws PopulateException{
		throw new PopulateException(e);		
	}
	
	public String getResourceHandler(String path, PopulateServlet populateServlet) throws IOException {

        String url;

        URL u = populateServlet.getServletContext().getResource(path);

        url = u != null ? u.toString() : path;

        return url;

  }
}
