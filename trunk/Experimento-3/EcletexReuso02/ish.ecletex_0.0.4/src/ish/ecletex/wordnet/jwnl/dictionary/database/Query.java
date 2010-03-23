package ish.ecletex.wordnet.jwnl.dictionary.database;

import ish.ecletex.wordnet.jwnl.JWNLRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
	private Connection _connection;
	private PreparedStatement _statement;
	private ResultSet _results;
	private String _sql;

	public Query(String sql, Connection conn) {
		_connection = conn;
		_sql = sql;
	}

	public ResultSet execute() throws SQLException {
		if (isExecuted()) {
			throw new JWNLRuntimeException("DICTIONARY_EXCEPTION_025");
		}
		return (_results = (getStatement().execute()) ? getStatement()
				.getResultSet() : null);
	}

	public boolean isExecuted() {
		return (_results != null);
	}

	public Connection getConnection() {
		return _connection;
	}

	public PreparedStatement getStatement() throws SQLException {
		if (_statement == null) {
			_statement = _connection.prepareStatement(_sql);
		}
		return _statement;
	}

	public ResultSet getResults() {
		return _results;
	}

	public void close() {
		if (_results != null) {
			internalClose();
		}
		if (_statement != null) {
			internalClose1();
		}
		if (_connection != null) {
			internalClose2();
		}
	}

	private void internalClose2() {
		_connection.close();
		_connection = null;
	}

	private void internalClose1() {
		_statement.close();
		_statement = null;
	}

	private void internalClose() {
		_results.close();
		_results = null;
	}
}