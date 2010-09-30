package com.sun.j2ee.blueprints.catalog;

import java.util.ArrayList;
import java.util.Locale;

import com.sun.j2ee.blueprints.catalog.dao.CatalogDAOException;

@ExceptionHandler
public privileged aspect CatalogHandler {

	pointcut getLodgingsHandler(): execution(public ArrayList CatalogFacade.getLodgings(String , Locale));

	pointcut getLodgingHandler(): execution(public Lodging CatalogFacade.getLodging(String , Locale));

	pointcut getAdventurePackageHandler(): execution(public AdventurePackage CatalogFacade.getAdventurePackage(String, Locale));

	pointcut getTransportationsHandler(): execution(public ArrayList CatalogFacade.getTransportations(String , String, Locale));

	pointcut getTransportationHandler(): execution(public Transportation CatalogFacade.getTransportation(String, Locale));

	pointcut getActivitiesHandler(): execution(public ArrayList CatalogFacade.getActivities(String, Locale));

	pointcut getActivityHandler(): execution(public Activity CatalogFacade.getActivity(String , Locale));

	Object around() throws CatalogException : getLodgingsHandler() || getLodgingHandler() 
	|| getAdventurePackageHandler() || getTransportationsHandler() 
	||  getTransportationHandler() || getActivitiesHandler() || getActivityHandler(){
		try {
			return proceed();
		} catch (CatalogDAOException cdos) {
			throw new CatalogException("Catalog Exception", cdos);
		}
	}	
}
