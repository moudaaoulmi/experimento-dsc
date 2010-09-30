package com.sun.j2ee.blueprints.catalog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.sun.j2ee.blueprints.catalog.Activity;
import com.sun.j2ee.blueprints.catalog.AdventurePackage;
import com.sun.j2ee.blueprints.catalog.Lodging;
import com.sun.j2ee.blueprints.catalog.Transportation;
import com.sun.j2ee.blueprints.util.dao.DAOSystemException;
import com.sun.j2ee.blueprints.util.dao.DAOUtils;

@ExceptionHandler
public privileged aspect DaoHandler {

	private int flag = 0;
	private int flag2 = 0;

	Map locationMap = new HashMap();
	Map stmtMap = new HashMap();
	Map dbMap = new HashMap();
	Map resultMap = new HashMap();

	String[] msg = { "Activity details; id = ", "Transportation details; id = ", "lodging details; id = " };

	pointcut internalGetActivityHandler(): execution(private Activity PointbaseCatalogDAO.internalGetActivity(String , Locale , PreparedStatement , ResultSet , Connection ));

	pointcut internalGetTransportationHandler(): execution(private Transportation PointbaseCatalogDAO.internalGetTransportation(String , Locale ,PreparedStatement , ResultSet , Connection ));

	pointcut internalGetLodgingHandler(): execution(private Lodging PointbaseCatalogDAO.internalGetLodging(String, Locale, PreparedStatement, ResultSet, Connection));
		
	pointcut internalGetLodgingsHandler(): execution(private ArrayList PointbaseCatalogDAO.internalGetLodgings(String , Locale , PreparedStatement, ResultSet, Connection ,ArrayList) );	

	pointcut internalGetAdventurePackageHandler(): execution(private AdventurePackage PointbaseCatalogDAO.internalGetAdventurePackage(String, Locale, PreparedStatement , PreparedStatement ,ResultSet , ResultSet , Connection ));

	pointcut internalGetTransportationsHandler(): execution(private ArrayList PointbaseCatalogDAO.internalGetTransportations(String ,String , Locale , PreparedStatement ,	ResultSet, Connection, ArrayList));

	pointcut internalGetActivitiesHandler(): execution(private ArrayList PointbaseCatalogDAO.internalGetActivities(String , Locale ,PreparedStatement , ResultSet , Connection ,ArrayList ));

	declare soft: SQLException: internalGetLodgingsHandler() || internalGetLodgingHandler() || internalGetAdventurePackageHandler() || internalGetTransportationsHandler() || internalGetTransportationHandler() || internalGetActivitiesHandler() || internalGetActivityHandler();

	
	//Alterar depois
	
	Object around(String id, Locale locale) throws DAOSystemException,
			CatalogDAOException  : ( internalGetActivityHandler() || internalGetTransportationHandler() || internalGetLodgingHandler())&& args(id, locale,..)
			{
		try {
			return proceed(id, locale);
		} catch (SQLException se) {
			throw new DAOSystemException("SQLException while getting "					
					+ this.msg[0] + id
					+ " and locale = " + locale.toString() + " \n", se);
		} finally {
			DAOUtils.closeResultSet((ResultSet) this.resultMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeConnection((Connection) this.dbMap.get(Thread
					.currentThread().getName()));
			this.flag = 0;
			this.flag2 = 0;
		}
	}

	ArrayList around(String location, Locale locale) throws DAOSystemException,
			CatalogDAOException  :  (internalGetActivitiesHandler()|| internalGetLodgingsHandler()) && args(location, locale, ..) {
		try {
			return proceed(location, locale);
		} catch (SQLException se) {
			String loc = (String) this.locationMap.get(Thread.currentThread()
					.getName());
			throw new DAOSystemException("SQLException while getting "
					+ "Activity details; location = " + loc + " and locale = "
					+ locale.toString() + " \n", se);
		} finally {
			DAOUtils.closeResultSet((ResultSet) this.resultMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeConnection((Connection) this.dbMap.get(Thread
					.currentThread().getName()));
			this.flag = 0;
			this.flag2 = 0;
		}
	}

	ArrayList around(String origin, String destination, Locale locale)
			throws DAOSystemException, CatalogDAOException : internalGetTransportationsHandler() && args(origin,	destination, locale){
		try {
			return proceed(origin, destination, locale);
		} catch (SQLException se) {
			String dest = (String) this.locationMap.get(Thread.currentThread()
					.getName());
			throw new DAOSystemException("SQLException while getting "
					+ "Transportation details; origin = " + origin + " , "
					+ "destination = " + dest + " and locale = "
					+ locale.toString() + " \n", se);
		} finally {
			DAOUtils.closeResultSet((ResultSet) this.resultMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeConnection((Connection) this.dbMap.get(Thread
					.currentThread().getName()));
			this.flag = 0;
			this.flag2 = 0;
		}
	}

	AdventurePackage around(String packageId, Locale locale)
			throws DAOSystemException, CatalogDAOException  : internalGetAdventurePackageHandler() && args(packageId,locale, ..){
		try {
			return proceed(packageId, locale);
		} catch (SQLException se) {
			throw new DAOSystemException("SQLException while getting "
					+ "AdventurePackage details; origin = " + packageId
					+ ", and locale = " + locale.toString() + " \n", se);
		} finally {
			DAOUtils.closeResultSet((ResultSet) this.resultMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeResultSet((ResultSet) this.resultMap.get(Thread
					.currentThread().getName()
					+ "2"));
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread
					.currentThread().getName()
					+ "2"));
			DAOUtils.closeConnection((Connection) this.dbMap.get(Thread
					.currentThread().getName()));
			this.flag = 0;
			this.flag2 = 0;
		}
	}

	pointcut getLocationHandler(): call(public String ResultSet.getString(int));

	pointcut getStmtHandler(): call(public PreparedStatement Connection.prepareStatement(String));

	pointcut getDbConnectionHandler(): call(public Connection DAOUtils.getDBConnection(String));

	pointcut getResultHandler(): call(public ResultSet PreparedStatement.executeQuery());

	String around(): (
						withincode(private ArrayList PointbaseCatalogDAO.internalGetLodgings(String , Locale , PreparedStatement, ResultSet, Connection ,ArrayList))
						|| withincode(private ArrayList PointbaseCatalogDAO.internalGetTransportations(String ,String , Locale , PreparedStatement ,ResultSet, Connection, ArrayList))
						|| withincode(private ArrayList PointbaseCatalogDAO.internalGetActivities(String , Locale ,PreparedStatement , ResultSet , Connection ,ArrayList ))
					)&& getLocationHandler(){
		String location = proceed();
		this.locationMap.put(Thread.currentThread().getName(), location);
		return location;
	}

	PreparedStatement around(): (
			withincode(private ArrayList PointbaseCatalogDAO.internalGetLodgings(String , Locale , PreparedStatement, ResultSet, Connection ,ArrayList))
			|| withincode(private Lodging PointbaseCatalogDAO.internalGetLodging(String, Locale, PreparedStatement, ResultSet, Connection))
			|| withincode(private AdventurePackage PointbaseCatalogDAO.internalGetAdventurePackage(String, Locale, PreparedStatement , PreparedStatement ,ResultSet , ResultSet , Connection ))
			|| withincode(private ArrayList PointbaseCatalogDAO.internalGetTransportations(String ,String , Locale , PreparedStatement ,ResultSet, Connection, ArrayList))
			|| withincode(private Transportation PointbaseCatalogDAO.internalGetTransportation(String , Locale ,PreparedStatement , ResultSet , Connection ))
			|| withincode(private ArrayList PointbaseCatalogDAO.internalGetActivities(String , Locale ,PreparedStatement , ResultSet , Connection ,ArrayList ))
			|| withincode(private Activity PointbaseCatalogDAO.internalGetActivity(String , Locale , PreparedStatement , ResultSet , Connection ))
		)&& getStmtHandler(){
		PreparedStatement stmt = proceed();
		if (this.flag == 0)
			this.stmtMap.put(Thread.currentThread().getName(), stmt);
		else
			this.stmtMap.put(Thread.currentThread().getName() + "2", stmt);
		this.flag2++;
		return stmt;
	}

	Connection around(): (
			withincode(private ArrayList PointbaseCatalogDAO.internalGetLodgings(String , Locale , PreparedStatement, ResultSet, Connection ,ArrayList))
			|| withincode(private Lodging PointbaseCatalogDAO.internalGetLodging(String, Locale, PreparedStatement, ResultSet, Connection))
			|| withincode(private AdventurePackage PointbaseCatalogDAO.internalGetAdventurePackage(String, Locale, PreparedStatement , PreparedStatement ,ResultSet , ResultSet , Connection ))
			|| withincode(private ArrayList PointbaseCatalogDAO.internalGetTransportations(String ,String , Locale , PreparedStatement ,ResultSet, Connection, ArrayList))
			|| withincode(private Transportation PointbaseCatalogDAO.internalGetTransportation(String , Locale ,PreparedStatement , ResultSet , Connection ))
			|| withincode(private ArrayList PointbaseCatalogDAO.internalGetActivities(String , Locale ,PreparedStatement , ResultSet , Connection ,ArrayList ))
			|| withincode(private Activity PointbaseCatalogDAO.internalGetActivity(String , Locale , PreparedStatement , ResultSet , Connection ))
			)&& getDbConnectionHandler(){
		Connection con = proceed();
		this.dbMap.put(Thread.currentThread().getName(), con);
		return con;
	}

	ResultSet around(): (
			withincode(private ArrayList PointbaseCatalogDAO.internalGetLodgings(String , Locale , PreparedStatement, ResultSet, Connection ,ArrayList))
			|| withincode(private Lodging PointbaseCatalogDAO.internalGetLodging(String, Locale, PreparedStatement, ResultSet, Connection))
			|| withincode(private AdventurePackage PointbaseCatalogDAO.internalGetAdventurePackage(String, Locale, PreparedStatement , PreparedStatement ,ResultSet , ResultSet , Connection ))
			|| withincode(private ArrayList PointbaseCatalogDAO.internalGetTransportations(String ,String , Locale , PreparedStatement ,ResultSet, Connection, ArrayList))
			|| withincode(private Transportation PointbaseCatalogDAO.internalGetTransportation(String , Locale ,PreparedStatement , ResultSet , Connection ))
			|| withincode(private ArrayList PointbaseCatalogDAO.internalGetActivities(String , Locale ,PreparedStatement , ResultSet , Connection ,ArrayList ))
			|| withincode(private Activity PointbaseCatalogDAO.internalGetActivity(String , Locale , PreparedStatement , ResultSet , Connection ))
		)&& getResultHandler(){
		ResultSet result = proceed();
		if (this.flag == 0)
			this.resultMap.put(Thread.currentThread().getName(), result);
		else
			this.resultMap.put(Thread.currentThread().getName() + "2", result);
		this.flag++;
		return result;
	}
}
