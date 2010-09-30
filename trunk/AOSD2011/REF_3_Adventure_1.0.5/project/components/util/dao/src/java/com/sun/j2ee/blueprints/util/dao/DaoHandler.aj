package com.sun.j2ee.blueprints.util.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;

//@ExceptionHandler
public privileged aspect DaoHandler {
	
	String[] msg = {"Statement : \n", "Result Set : \n" , "DB connection : \n", "DB connection : \n"};
	
	pointcut closeStatementHandler(): execution(public static void DAOUtils.closeStatement(PreparedStatement));
	pointcut closeResultSetHandler(): execution(public static void DAOUtils.closeResultSet(ResultSet));
	pointcut closeConnectionHandler(): execution(public static void DAOUtils.closeConnection(Connection));
	pointcut getDBConnectionHandler(): execution(public static Connection DAOUtils.getDBConnection(String));
	pointcut getDAOHandler(): execution(public static Object DAOFactory.getDAO(String));
	
		
	declare soft: NamingException: getDAOHandler();
	declare soft: Exception: getDAOHandler();
	declare soft: SQLException: getDBConnectionHandler() || closeConnectionHandler() || closeResultSetHandler() || closeStatementHandler();
	
	//Inserir o this.msg[thisEnclosingJoinPointStaticPart.getId()]
	
	Object around() throws DAOSystemException : closeStatementHandler() || closeResultSetHandler() ||  closeConnectionHandler() ||  getDBConnectionHandler()  {
        try {
            return proceed();
        } catch (SQLException se) {
            throw new DAOSystemException("SQL Exception while closing "
            +  this.msg[0]+ se);
        }
    }	
	
	Object around(String daoEnvEntry) throws DAOSystemException  : getDAOHandler() && args(daoEnvEntry) {
        try {
            return proceed(daoEnvEntry);
        } catch (NamingException ne) {
            throw new DAOSystemException("DAOFactory.getDAO(" + daoEnvEntry +"):  NamingException while getting DAO type : \n" + ne.getMessage());
        } catch (Exception se) {
            throw new DAOSystemException("DAOFactory.getDAO(" + daoEnvEntry +"):  Exception while getting DAO type : \n" + se.getMessage());
        }
    }

}
