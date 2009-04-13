package com.sun.j2ee.blueprints.supplier.tools.populate;



public class PopulateHandler {

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
	
}
