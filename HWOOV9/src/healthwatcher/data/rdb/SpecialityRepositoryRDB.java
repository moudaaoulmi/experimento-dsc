package healthwatcher.data.rdb;


import healthwatcher.data.ISpecialityRepository;
import healthwatcher.model.healthguide.MedicalSpeciality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lib.exceptions.ExceptionMessages;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.PersistenceMechanismException;
import lib.exceptions.PersistenceSoftException;
import lib.exceptions.RepositoryException;
import lib.persistence.IPersistenceMechanism;
import lib.util.ConcreteIterator;
import lib.util.IteratorDsk;





public class SpecialityRepositoryRDB implements ISpecialityRepository {

	private IPersistenceMechanism mp;

	protected ResultSet resultSet;

	public SpecialityRepositoryRDB(IPersistenceMechanism mp) {
		this.mp = mp;
	}

	public void update(MedicalSpeciality esp) throws RepositoryException, ObjectNotFoundException,
			ObjectNotValidException {
		
		if (esp != null) {
			String sql=null;
			try {
				Statement stmt = (Statement) this.mp.getCommunicationChannel();
				sql = "update SCBS_especialidade set " +
                "descricao='" + esp.getDescricao() + "'" +
                " where codigo = '"+esp.getCodigo()+"'";
				stmt.executeUpdate(sql);
				stmt.close();
			} catch (SQLException sqlException) {
				System.out.println(sql);
				throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
			} catch (PersistenceMechanismException mpException) {
				throw new RepositoryException(ExceptionMessages.EXC_FALHA_ATUALIZACAO);
			}finally {
                try {
                    mp.releaseCommunicationChannel();
                } catch (PersistenceMechanismException e) {
                    throw new PersistenceSoftException(e);
                }
            }
		} else {
			throw new ObjectNotValidException(ExceptionMessages.EXC_NULO);
		}
	}

	public boolean exists(int code) throws RepositoryException {
		boolean response = false;
        String sql=null;
        try {
        	sql = "select * from SCBS_especialidade where "
                + "codigo = '" + code + "'";

            Statement stmt = (Statement) this.mp.getCommunicationChannel();
            resultSet  = stmt.executeQuery(sql);
            response = resultSet.next();
            resultSet.close();
            stmt.close();            
        } catch (PersistenceMechanismException e) {
            throw new PersistenceSoftException(e);
        } catch (SQLException e) {
        	System.out.println(sql);
            throw new PersistenceSoftException(e);
        }
        return response;
	}

	public IteratorDsk getSpecialityList() throws RepositoryException, ObjectNotFoundException {

		List listaEsp = new ArrayList();
		String sql = "SELECT * FROM SCBS_especialidade";
		ResultSet rs = null;

		try {
			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			rs = stmt.executeQuery(sql);

			if (!rs.next()) {
				throw new ObjectNotFoundException("");
			}
			do {
				MedicalSpeciality esp = search((new Integer(rs.getString("codigo"))).intValue());
				listaEsp.add(esp);
			} while (rs.next());

			rs.close();
			stmt.close();
		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_PROCURA);
		} catch (SQLException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_PROCURA);

		} finally {
			try {
				mp.releaseCommunicationChannel();
			} catch (PersistenceMechanismException e) {
				throw new PersistenceSoftException(e);
			}
		}

		return new ConcreteIterator(listaEsp);
	}

	public void insert(MedicalSpeciality spec) throws RepositoryException,
			ObjectAlreadyInsertedException, ObjectNotValidException {

		if (spec != null) {
			String sql = null;
			try {
				Statement stmt = (Statement) mp.getCommunicationChannel();
				sql = "insert into SCBS_especialidade (codigo,descricao) values (";
				sql += spec.getCodigo() + ",'";
				sql += spec.getDescricao() + "')";

				stmt.executeUpdate(sql);
				stmt.close();
			} catch (SQLException e) {
				System.out.println(sql);
				throw new PersistenceSoftException(e);
			} catch (PersistenceMechanismException e) {
				throw new PersistenceSoftException(e);
			} finally {
				try {
					mp.releaseCommunicationChannel();
				} catch (PersistenceMechanismException e) {
					throw new PersistenceSoftException(e);
				}
			}
		} else {
			throw new ObjectNotValidException(ExceptionMessages.EXC_NULO);
		}
	}

	public MedicalSpeciality search(int code) throws RepositoryException, ObjectNotFoundException {

		MedicalSpeciality esp = null;
		String sql = null;
		try {
			sql = "select * from SCBS_especialidade where " + "codigo = '" + code + "'";

			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			resultSet = stmt.executeQuery(sql);

			if (resultSet.next()) {
				esp = new MedicalSpeciality(resultSet.getString("descricao"));
				esp.setCodigo((new Integer(resultSet.getString("codigo"))).intValue());
			} else {
				throw new ObjectNotFoundException(ExceptionMessages.EXC_FALHA_PROCURA);
			}
			resultSet.close();
			stmt.close();
		} catch (PersistenceMechanismException e) {
			e.printStackTrace();
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (java.sql.SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} finally {
			try {
				mp.releaseCommunicationChannel();
			} catch (PersistenceMechanismException e) {
				throw new PersistenceSoftException(e);
			}
		}

		return esp;
	}

	public void remove(int code) throws RepositoryException, ObjectNotFoundException {
	}
}
