package healthwatcher.aspects.concurrency;

import healthwatcher.aspects.patterns.UpdateStateObserver;
import healthwatcher.data.rdb.ComplaintRepositoryRDB;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.view.command.CommandServlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lib.exceptions.ExceptionMessages;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.PersistenceMechanismException;
import lib.exceptions.RepositoryException;
import lib.patterns.CommandReceiver;
import lib.persistence.PersistenceMechanism;

/**
 * Timestamping for the Complaint classes.
 * Introduces the needed methods and creates advice to check inserts, updates and searches
 * 
 */
public aspect HWTimestamp {

	// Declares the fields and methods necessary for timestamps in Complaints
	private long Complaint.timestamp; // para tratamento de concorrencia (scbs)
	
	public long Complaint.getTimestamp() {
		return timestamp;
	}

	public void Complaint.setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void Complaint.incTimestamp() {
		this.timestamp = timestamp + 1;
	}
	
	// After inserting, update the DB
	after(Complaint complaint) : execution(* ComplaintRepositoryRDB.insert(Complaint)) && args(complaint) {
		updateTimestamp(complaint.getTimestamp() + "", "SCBS_queixa", complaint.getCodigo() + "");
	}
	
	// After searching, load from the DB
	after(Complaint complaint) : execution(* ComplaintRepositoryRDB.search(int)) && args(complaint) {
		long timestamp = searchTimestamp("SCBS_queixa", complaint.getCodigo() + "");
		complaint.setTimestamp(timestamp);
	}
	
	// When updating, check the TS before and update DB after
	void around (Complaint complaint, ComplaintRepositoryRDB repository) : 
		 execution(* ComplaintRepositoryRDB.update(Complaint)) && args(complaint) && this(repository){
		
		synchronized (this) {
			String sql = null;
			try {
				long timestamp;
				Statement stmt = (Statement) PersistenceMechanism.getInstance().getCommunicationChannel();
				// vendo se a versão do objeto é a mesma no BD
				sql = "select ts from SCBS_queixa " + " where codigo='"
						+ complaint.getCodigo() + "'";
				ResultSet resultSet = stmt.executeQuery(sql);
				if (resultSet.next()) {
					timestamp = (new Long(resultSet.getString("ts"))).longValue();
					if (timestamp != complaint.getTimestamp()) {
							new RepositoryException(
								ExceptionMessages.EXC_FALHA_ATUALIZACAO_COPIA);
					} else {
						complaint.incTimestamp();
					}
				} else {
					new ObjectNotFoundException(
						ExceptionMessages.EXC_FALHA_ATUALIZACAO);
				}
				resultSet.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(sql);
					new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
			} catch (PersistenceMechanismException e) {
					new RepositoryException(ExceptionMessages.EXC_FALHA_ATUALIZACAO);
			}
		try {
			proceed(complaint, repository);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
			updateTimestamp(complaint.getTimestamp() + "", "SCBS_queixa", complaint.getCodigo()
					+ "");
		}
	}
	
	// Auxiliary Methods
	private void updateTimestamp(String value, String tableName, String id) {
		Statement stmt = null;
		int result = 0;
		try {
			String sql = "update " + tableName + " set ts='" + value + "' where codigo='" + id
					+ "'";
			stmt = (Statement) PersistenceMechanism.getInstance().getCommunicationChannel();
			result = stmt.executeUpdate(sql);
			if (result == 0) {
				throw new RuntimeException("ERRO no aspecto TimestampAspectHealthWatcher ##2");
			}
		} catch (Exception ex) {

			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private long searchTimestamp(String tableName, String id) {
		Statement stmt = null;
		ResultSet resultSet = null;
		long answer = 0;
		try {
			String sql = "SELECT ts FROM " + tableName + " WHERE codigo='" + id + "'";

			PersistenceMechanism pm = PersistenceMechanism.getInstance();
			stmt = (Statement) pm.getCommunicationChannel();
			resultSet = stmt.executeQuery(sql);
			if (resultSet.next()) {
				answer = resultSet.getLong(1);
			} else {
				throw new RuntimeException("ERRO no aspecto TimestampAspectHealthWatcher ##2");
			}
			return answer;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
	
	/**
	 * This advice is needed because of distribution. When the client side makes an update, the
	 * server side complaint is updated, but the client side is not. Thus, we need also to update
	 * the client side object (hence the cflow) and increment it.
	 * Note that this was not needed before the Observer pattern, because we would update the object
	 * just once per request. Now we update it more than once and this synchronization is needed.
	 */
	after(Complaint c) : execution(void UpdateStateObserver.updateObserver(..)) && args(c, ..) &&
		cflow(execution(* CommandServlet+.executeCommand(CommandReceiver))) {
		
		c.incTimestamp();
	}
}
