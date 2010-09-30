package com.sun.j2ee.blueprints.customer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.sun.j2ee.blueprints.customer.Account;
import com.sun.j2ee.blueprints.customer.ContactInformation;
import com.sun.j2ee.blueprints.util.dao.DAOSystemException;
import com.sun.j2ee.blueprints.util.dao.DAOUtils;

//@ExceptionHandler
public privileged aspect DaoHandler {

	Map locationMap = new HashMap();
	Map stmtMap = new HashMap();
	Map dbMap = new HashMap();
	Map resultMap = new HashMap();


	// Pointcuts

	pointcut internalSelectAccountHandler(): execution(private Account PointbaseAccountDAO.internalSelectAccount(String,PreparedStatement , ResultSet , Connection ));

	pointcut internalUserExistsHandler(): execution(private boolean PointbaseAccountDAO.internalUserExists(String, boolean , PreparedStatement ,	ResultSet ,  Connection ));

	pointcut internalInsertAccountHandler(): execution(private void PointbaseAccountDAO.internalInsertAccount(Account , PreparedStatement ,	ContactInformation , Connection ));

	declare soft: SQLException: internalInsertAccountHandler() || internalUserExistsHandler() || internalSelectAccountHandler();

	Account around(String userId) throws DAOSystemException,
			AccountDAOFinderException :  internalSelectAccountHandler() && args(userId, ..)
			{
		try {
			return proceed(userId);
		} catch (SQLException se) {
			String user = (String) this.locationMap.get(Thread.currentThread()
					.getName());
			if (user == null)
				user = userId;
			throw new DAOSystemException("SQLException while getting "
					+ "account; id = " + user
					+ " :\n", se);
		} finally {
			DAOUtils.closeResultSet((ResultSet) this.resultMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeConnection((Connection) this.dbMap.get(Thread
					.currentThread().getName()));
		}
	}	
	
	boolean around(String userId) throws DAOSystemException  : internalUserExistsHandler() && args(userId, ..){
		try {
			return proceed(userId);
		} catch (SQLException se) {
			String user = (String) this.locationMap
					.get(Thread.currentThread().getName());
			if (user == null)
				user = userId;

			throw new DAOSystemException("SQLException while checking for an"
					+ " existing user - id -> " + user + "\n", se);
		} finally {
			DAOUtils.closeResultSet((ResultSet) this.resultMap.get(Thread.currentThread()
					.getName()));
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread.currentThread()
					.getName()));
			DAOUtils.closeConnection((Connection)this.dbMap.get(Thread.currentThread()
					.getName()));
		}
	}


	void around(Account details) throws DAOSystemException,
			AccountDAODBUpdateException : internalInsertAccountHandler() && args(details,..){
		try {
			proceed(details);
		} catch (SQLException se) {
			throw new DAOSystemException("SQLException while inserting new "
					+ "account; id = " + details.getUserId() + "\n", se);
		} finally {
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread
					.currentThread().getName()));
			DAOUtils.closeConnection((Connection) this.dbMap.get(Thread
					.currentThread().getName()));
		}
	}

	// Pointucts Auxiliares
	pointcut getLocationHandler(): call(public String ResultSet.getString(int));

	pointcut getStmtHandler(): call(public PreparedStatement Connection.prepareStatement(String));

	pointcut getDbConnectionHandler(): call(public Connection DAOUtils.getDBConnection(String));

	pointcut getResultHandler(): call(public ResultSet PreparedStatement.executeQuery());

	String around(): (
				withincode(private boolean PointbaseAccountDAO.internalUserExists(String, boolean , PreparedStatement ,	ResultSet ,  Connection ))
				|| withincode(private Account internalSelectAccountHandler(String,PreparedStatement , ResultSet , Connection ))
			)&& getLocationHandler(){
		String location = proceed();
		this.locationMap.put(Thread.currentThread().getName(), location);
		return location;
	}

	PreparedStatement around(): (
	withincode(private void PointbaseAccountDAO.internalInsertAccount(Account , PreparedStatement ,	ContactInformation , Connection ))
	|| withincode(private boolean PointbaseAccountDAO.internalUserExists(String, boolean , PreparedStatement ,	ResultSet ,  Connection ))
	|| withincode(private Account internalSelectAccountHandler(String,PreparedStatement , ResultSet , Connection ))
	)&& getStmtHandler(){
		PreparedStatement stmt = proceed();
		this.stmtMap.put(Thread.currentThread().getName(), stmt);
		return stmt;
	}

	Connection around(): (
	withincode(private void PointbaseAccountDAO.internalInsertAccount(Account , PreparedStatement ,	ContactInformation , Connection ))
	|| withincode(private boolean PointbaseAccountDAO.internalUserExists(String, boolean , PreparedStatement ,	ResultSet ,  Connection ))
	|| withincode(private Account internalSelectAccountHandler(String,PreparedStatement , ResultSet , Connection ))
	)&& getDbConnectionHandler(){
		Connection con = proceed();
		this.dbMap.put(Thread.currentThread().getName(), con);
		return con;
	}

	ResultSet around(): (
	withincode(private boolean PointbaseAccountDAO.internalUserExists(String, boolean , PreparedStatement ,	ResultSet ,  Connection ))		
	|| withincode(private Account internalSelectAccountHandler(String,PreparedStatement , ResultSet , Connection ))
)&& getResultHandler(){
		ResultSet result = proceed();
		this.resultMap.put(Thread.currentThread().getName(), result);
		return result;
	}

}
