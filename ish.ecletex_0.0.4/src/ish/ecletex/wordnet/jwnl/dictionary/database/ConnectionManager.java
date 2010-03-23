package ish.ecletex.wordnet.jwnl.dictionary.database;

import ish.ecletex.wordnet.jwnl.JWNLException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	private String _driverClass;
	private String _url;
	private String _userName;
	private String _password;
	private boolean _registered;

	public ConnectionManager(String driverClass, String url, String userName,
			String password) {
		_driverClass = driverClass;
		_url = url;
		_userName = userName;
		_password = password;
	}

	public Query getQuery(String sql) throws SQLException, JWNLException {
		return new Query(sql, getConnection());
	}

	public Connection getConnection() throws SQLException, JWNLException {
		registerDriver();
		if (_userName == null) {
			return DriverManager.getConnection(_url);
		} else {
			return DriverManager.getConnection(_url, _userName,
					(_password != null) ? _password : "");
		}
	}

	private void registerDriver() throws JWNLException {
		if (!_registered) {
			internalRegisterDriver();
		}
	}

	private void internalRegisterDriver() throws JWNLException {
		Driver driver = (Driver) Class.forName(_driverClass).newInstance();
		DriverManager.registerDriver(driver);
		_registered = true;
	}
}