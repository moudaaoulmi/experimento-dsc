package com.sun.j2ee.blueprints.signon.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.sun.j2ee.blueprints.util.dao.DAOSystemException;
import com.sun.j2ee.blueprints.util.dao.DAOUtils;

//@ExceptionHandler
public privileged aspect DaoHandler {

	Map stmtMap = new HashMap();
	Map dbMap = new HashMap();
	Map resultMap = new HashMap();

	// Pointucts Auxiliares
	pointcut getStmtHandler(): call(public PreparedStatement Connection.prepareStatement(String));
	pointcut getDbConnectionHandler(): call(public Connection DAOUtils.getDBConnection(String));
	pointcut getResultHandler(): call(public ResultSet PreparedStatement.executeQuery());

	// Pointcuts

	pointcut internalCreateUserHandler(): execution(private void PointbaseUserDAO.internalCreateUser(String , String , Connection , PreparedStatement ));
	pointcut internalMatchPasswordHandler(): execution(private void PointbaseUserDAO.internalMatchPassword(String, String ,	Connection , PreparedStatement , ResultSet));
	
	
	declare soft: SQLException: internalCreateUserHandler() || internalMatchPasswordHandler() ;
	
	void around() throws DAOSystemException, InvalidPasswordException,
	SignOnDAOFinderException: internalMatchPasswordHandler()
			 {
		try {
           proceed();
        } catch (SQLException se) {
            throw new DAOSystemException(se);
        } finally {
        	DAOUtils.closeResultSet((ResultSet)this.resultMap.get(Thread.currentThread()
					.getName()));
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread.currentThread()
					.getName()));
			DAOUtils.closeConnection((Connection)this.dbMap.get(Thread.currentThread()
					.getName()));
        }
	}

	void around() throws DAOSystemException, SignOnDAODupKeyException: internalCreateUserHandler()  {
		try {
			proceed();
		} catch (SQLException se) {
			throw new DAOSystemException(se);
		} finally {
			DAOUtils.closeStatement((PreparedStatement) this.stmtMap.get(Thread.currentThread()
					.getName()));
			DAOUtils.closeConnection((Connection) this.dbMap.get(Thread.currentThread()
					.getName()));
		}
	}
	
	PreparedStatement around(): (
			withincode(private void PointbaseUserDAO.internalCreateUser(String , String , Connection , PreparedStatement ))
			|| withincode(private void PointbaseUserDAO.internalMatchPassword(String, String ,	Connection , PreparedStatement , ResultSet))
			)&& getStmtHandler(){
		PreparedStatement stmt = proceed();
		this.stmtMap.put(Thread.currentThread().getName(), stmt);
		return stmt;
	}

	Connection around(): (
			withincode(private void PointbaseUserDAO.internalCreateUser(String , String , Connection , PreparedStatement ))
			|| withincode(private void PointbaseUserDAO.internalMatchPassword(String, String ,	Connection , PreparedStatement , ResultSet))
			)&& getDbConnectionHandler(){
		Connection con = proceed();
		this.dbMap.put(Thread.currentThread().getName(), con);
		return con;
	}

	ResultSet around(): (
			withincode(private void PointbaseUserDAO.internalMatchPassword(String, String ,	Connection , PreparedStatement , ResultSet))
			)&& getResultHandler(){
		ResultSet result = proceed();
		this.resultMap.put(Thread.currentThread().getName(), result);
		return result;
	}

}
