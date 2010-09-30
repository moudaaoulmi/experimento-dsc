/* Copyright 2004 Sun Microsystems, Inc.  All rights reserved.  You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: 
 http://adventurebuilder.dev.java.net/LICENSE.txt
 $Id: PointbaseCatalogDAO.java,v 1.2 2004/05/26 00:06:15 inder Exp $ */

package com.sun.j2ee.blueprints.catalog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.Locale;
import java.util.ArrayList;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.util.dao.*;
import com.sun.j2ee.blueprints.catalog.*;

/**
 * This class implements CatalogDAO for Pointbase database. This class
 * encapsulates all the JDBC calls . The logic of
 * inserting/fetching/updating/deleting the data in relational database tables
 * is implemented here.
 */
public class PointbaseCatalogDAO implements CatalogDAO {

	private final static String SELECT_LODGINGS_QUERY_STR = "SELECT "
			+ "lodgingid, name, description, price, location" + "  FROM "
			+ DatabaseNames.LODGING_TABLE
			+ " WHERE location = ? AND locale = ?";
	private final static String SELECT_LODGING_QUERY_STR = "SELECT "
			+ "lodgingid, name, description, price, location" + "  FROM "
			+ DatabaseNames.LODGING_TABLE
			+ " WHERE lodgingid = ? AND locale = ?";
	private final static String SELECT_TRANSPORTATIONS_QUERY_STR = "SELECT "
			+ "transportationid, name, description, imageuri, price, origin, destination,carrier,departuretime, arrivaltime, class"
			+ " FROM " + DatabaseNames.TRANSPORTATION_TABLE
			+ " WHERE origin = ? AND destination = ? AND locale = ?";
	private final static String SELECT_TRANSPORTATION_QUERY_STR = "SELECT "
			+ "transportationid, name, description, imageuri, price, origin, destination,carrier,departuretime, arrivaltime, class"
			+ " FROM " + DatabaseNames.TRANSPORTATION_TABLE
			+ " WHERE transportationid = ? AND locale = ?";
	private final static String SELECT_ACTIVITIES_QUERY_STR = "SELECT "
			+ "activityid, name, description, price, location " + " FROM "
			+ DatabaseNames.ACTIVITY_TABLE
			+ " WHERE location = ? AND locale = ?";
	private final static String SELECT_ACTIVITY_QUERY_STR = "SELECT "
			+ "activityid, name, description, price, location " + " FROM "
			+ DatabaseNames.ACTIVITY_TABLE
			+ " WHERE activityid = ? AND locale = ?";
	private final static String SELECT_ADVENTURE_PACKAGE_QUERY_STR = "SELECT "
			+ "name, description, price, location, lodgingid  " + " FROM "
			+ DatabaseNames.PACKAGE_TABLE
			+ " WHERE packageid = ? AND locale = ?";
	private final static String SELECT_ACTIVITYLIST_QUERY_STR = "SELECT "
			+ "activityid" + " FROM " + DatabaseNames.ACTIVITYLIST_TABLE
			+ " WHERE packageid = ? AND locale = ?";

	public PointbaseCatalogDAO() {
	}

	public ArrayList getLodgings(String location, Locale locale)
			throws CatalogDAOException, DAOSystemException {

		PreparedStatement stmt = null;
		ResultSet result = null;
		Connection dbConnection = null;

		ArrayList lodgings = new ArrayList();
		return internalGetLodgings(location, locale, stmt, result,
				dbConnection, lodgings);
	}

	private ArrayList internalGetLodgings(String location, Locale locale,
			PreparedStatement stmt, ResultSet result, Connection dbConnection,
			ArrayList lodgings) throws DAOSystemException, CatalogDAOException {
		dbConnection = DAOUtils.getDBConnection(JNDINames.CATALOG_DATASOURCE);
		stmt = dbConnection.prepareStatement(SELECT_LODGINGS_QUERY_STR);
		stmt.setString(1, location.trim());
		stmt.setString(2, locale.toString().trim());
		result = stmt.executeQuery();
		if (!result.next())
			throw new CatalogDAOException("No data found for  " + location
					+ " , " + locale.toString());
		do {
			int i = 1;
			String lodgingId = result.getString(i++);
			String name = result.getString(i++);
			String description = result.getString(i++);
			double price = result.getDouble(i++);
			location = result.getString(i++);
			lodgings.add(new Lodging(lodgingId, name, description, price,
					location));
		} while (result.next());

		return (lodgings);
	}

	public Lodging getLodging(String id, Locale locale)
			throws CatalogDAOException, DAOSystemException {

		PreparedStatement stmt = null;
		ResultSet result = null;
		Connection dbConnection = null;
		return internalGetLodging(id, locale, stmt, result, dbConnection);
	}

	private Lodging internalGetLodging(String id, Locale locale,
			PreparedStatement stmt, ResultSet result, Connection dbConnection)
			throws DAOSystemException, CatalogDAOException {
		dbConnection = DAOUtils.getDBConnection(JNDINames.CATALOG_DATASOURCE);
		stmt = dbConnection.prepareStatement(SELECT_LODGING_QUERY_STR);
		stmt.setString(1, id.trim());
		stmt.setString(2, locale.toString().trim());
		result = stmt.executeQuery();
		if (!result.next())
			throw new CatalogDAOException("No data found for  " + id + " , "
					+ locale.toString());
		int i = 1;
		String lodgingId = result.getString(i++);
		String name = result.getString(i++);
		String description = result.getString(i++);
		double price = result.getDouble(i++);
		String location = result.getString(i++);
		return new Lodging(lodgingId, name, description, price, location);
	}

	public AdventurePackage getAdventurePackage(String packageId, Locale locale)
			throws CatalogDAOException, DAOSystemException {

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet result = null;
		ResultSet result2 = null;
		Connection dbConnection = null;

		ArrayList transportations = new ArrayList();
		return internalGetAdventurePackage(packageId, locale, stmt, stmt2,
				result, result2, dbConnection);
	}

	private AdventurePackage internalGetAdventurePackage(String packageId,
			Locale locale, PreparedStatement stmt, PreparedStatement stmt2,
			ResultSet result, ResultSet result2, Connection dbConnection)
			throws DAOSystemException, CatalogDAOException {
		dbConnection = DAOUtils.getDBConnection(JNDINames.CATALOG_DATASOURCE);
		stmt = dbConnection
				.prepareStatement(SELECT_ADVENTURE_PACKAGE_QUERY_STR);
		stmt.setString(1, packageId.trim());
		stmt.setString(2, locale.toString().trim());
		result = stmt.executeQuery();
		if (!result.next())
			throw new CatalogDAOException("No data found for  " + packageId
					+ " , " + locale.toString());
		int i = 1;
		String name = result.getString(i++).trim();
		String description = result.getString(i++).trim();
		double price = result.getDouble(i++);
		String location = result.getString(i++).trim();
		String lodgingId = result.getString(i++).trim();
		ArrayList activities = new ArrayList();
		// Getting the activites
		stmt2 = dbConnection.prepareStatement(SELECT_ACTIVITYLIST_QUERY_STR);
		stmt2.setString(1, packageId);
		stmt2.setString(2, locale.toString().trim());
		result2 = stmt2.executeQuery();

		while (result2.next()) {
			String activityId = result2.getString(1).trim();
			activities.add(activityId);
		}
		AdventurePackage ap = new AdventurePackage(packageId, name,
				description, location, lodgingId, price, activities);
		return (ap);
	}

	public ArrayList getTransportations(String origin, String destination,
			Locale locale) throws CatalogDAOException, DAOSystemException {

		PreparedStatement stmt = null;
		ResultSet result = null;
		Connection dbConnection = null;

		ArrayList transportations = new ArrayList();
		return internalGetTransportations(origin, destination, locale, stmt,
				result, dbConnection, transportations);
	}

	private ArrayList internalGetTransportations(String origin,
			String destination, Locale locale, PreparedStatement stmt,
			ResultSet result, Connection dbConnection, ArrayList transportations)
			throws DAOSystemException, CatalogDAOException {
		dbConnection = DAOUtils.getDBConnection(JNDINames.CATALOG_DATASOURCE);
		stmt = dbConnection.prepareStatement(SELECT_TRANSPORTATIONS_QUERY_STR);
		stmt.setString(1, origin.trim());
		stmt.setString(2, destination.trim());
		stmt.setString(3, locale.toString().trim());
		result = stmt.executeQuery();
		if (!result.next())
			throw new CatalogDAOException("No data found for  " + origin
					+ " , " + destination + " , " + locale.toString());
		do {
			int i = 1;
			String transportId = result.getString(i++);
			String name = result.getString(i++);
			String description = result.getString(i++);
			String imageURI = result.getString(i++);
			double price = result.getDouble(i++);
			origin = result.getString(i++);
			destination = result.getString(i++);
			String carrier = result.getString(i++);
			String departureTime = result.getString(i++);
			String arrivalTime = result.getString(i++);
			String travelClass = result.getString(i++);
			transportations.add(new Transportation(transportId, name,
					description, imageURI, price, origin, destination, carrier,
					departureTime, arrivalTime, travelClass));
		} while (result.next());

		return (transportations);
	}

	public Transportation getTransportation(String id, Locale locale)
			throws CatalogDAOException, DAOSystemException {

		PreparedStatement stmt = null;
		ResultSet result = null;
		Connection dbConnection = null;

		return internalGetTransportation(id, locale, stmt, result, dbConnection);
	}

	private Transportation internalGetTransportation(String id, Locale locale,
			PreparedStatement stmt, ResultSet result, Connection dbConnection)
			throws DAOSystemException, CatalogDAOException {
		dbConnection = DAOUtils.getDBConnection(JNDINames.CATALOG_DATASOURCE);
		stmt = dbConnection.prepareStatement(SELECT_TRANSPORTATION_QUERY_STR);
		stmt.setString(1, id.trim());
		stmt.setString(2, locale.toString().trim());
		result = stmt.executeQuery();
		if (!result.next())
			throw new CatalogDAOException("No data found for  " + id + " , "
					+ locale.toString());
		int i = 1;
		String transportId = result.getString(i++);
		String name = result.getString(i++);
		String description = result.getString(i++);
		String imageURI = result.getString(i++);
		double price = result.getDouble(i++);
		String origin = result.getString(i++);
		String destination = result.getString(i++);
		String carrier = result.getString(i++);
		String departureTime = result.getString(i++);
		String arrivalTime = result.getString(i++);
		String travelClass = result.getString(i++);
		return new Transportation(transportId, name, description, imageURI,
				price, origin, destination, carrier, departureTime,
				arrivalTime, travelClass);
	}

	public ArrayList getActivities(String location, Locale locale)
			throws CatalogDAOException, DAOSystemException {

		PreparedStatement stmt = null;
		ResultSet result = null;
		Connection dbConnection = null;

		ArrayList activities = new ArrayList();
		return internalGetActivities(location, locale, stmt, result,
				dbConnection, activities);
	}

	private ArrayList internalGetActivities(String location, Locale locale,
			PreparedStatement stmt, ResultSet result, Connection dbConnection,
			ArrayList activities) throws DAOSystemException,
			CatalogDAOException {
		dbConnection = DAOUtils.getDBConnection(JNDINames.CATALOG_DATASOURCE);
		stmt = dbConnection.prepareStatement(SELECT_ACTIVITIES_QUERY_STR);
		stmt.setString(1, location.trim());
		stmt.setString(2, locale.toString().trim());
		result = stmt.executeQuery();
		if (!result.next())
			throw new CatalogDAOException("No data dound for  " + location
					+ " , " + locale.toString());
		do {
			int i = 1;
			String activityId = result.getString(i++);
			String name = result.getString(i++);
			String description = result.getString(i++);
			double price = result.getDouble(i++);
			location = result.getString(i++);
			activities.add(new Activity(activityId, name, description, price,
					location));
		} while (result.next());

		return (activities);
	}

	public Activity getActivity(String id, Locale locale)
			throws CatalogDAOException, DAOSystemException {

		PreparedStatement stmt = null;
		ResultSet result = null;
		Connection dbConnection = null;

		return internalGetActivity(id, locale, stmt, result, dbConnection);
	}

	private Activity internalGetActivity(String id, Locale locale,
			PreparedStatement stmt, ResultSet result, Connection dbConnection)
			throws DAOSystemException, CatalogDAOException {
		dbConnection = DAOUtils.getDBConnection(JNDINames.CATALOG_DATASOURCE);
		stmt = dbConnection.prepareStatement(SELECT_ACTIVITY_QUERY_STR);
		stmt.setString(1, id.trim());
		stmt.setString(2, locale.toString().trim());
		result = stmt.executeQuery();
		if (!result.next())
			throw new CatalogDAOException("No data dound for  " + id + " , "
					+ locale.toString());
		int i = 1;
		String activityId = result.getString(i++);
		String name = result.getString(i++);
		String description = result.getString(i++);
		double price = result.getDouble(i++);
		String location = result.getString(i++);
		return new Activity(activityId, name, description, price, location);
	}
}
