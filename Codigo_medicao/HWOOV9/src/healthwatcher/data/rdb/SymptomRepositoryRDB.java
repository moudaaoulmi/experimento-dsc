package healthwatcher.data.rdb;

import healthwatcher.data.ISymptomRepository;
import healthwatcher.model.complaint.Symptom;

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





public class SymptomRepositoryRDB implements ISymptomRepository {

	private IPersistenceMechanism mp;
    private ResultSet resultSet;

	public SymptomRepositoryRDB(IPersistenceMechanism mp) {
		this.mp = mp;
	}

	public void insert(Symptom symptom) throws ObjectNotValidException, ObjectAlreadyInsertedException,
		ObjectNotValidException, RepositoryException {
		
        if (symptom != null) {
        	String sql=null;
            try {
                Statement stmt = (Statement) this.mp.getCommunicationChannel();
                sql = "insert into SCBS_sintoma (codigo,DESCRICAO) values ('";
                sql += symptom.getCode() + "','";
                sql += symptom.getDescription() + "')";               
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
	
	public Symptom search(int symptomCode) throws ObjectNotFoundException, RepositoryException {
		Symptom symptom = null;
		String sql=null;
		Statement stmt=null;
        try {
	        // Query montada para recuperar o sintoma
	        // usando o identificador da sintoma informado como
	        // parametro do metodo
            sql = "select * from SCBS_sintoma where "
                + "codigo = '" + symptomCode + "'";

            stmt = (Statement)this.mp.getCommunicationChannel();
            resultSet  = stmt.executeQuery(sql);


            if (resultSet.next()) {
            	symptom = new Symptom(resultSet.getString("descricao"));
            	symptom.setCode((new Integer(resultSet.getString("codigo"))).intValue());

            } else {
                throw new ObjectNotFoundException(ExceptionMessages
                    .EXC_FALHA_PROCURA);
            }
            resultSet.close();
            stmt.close();

        } catch (PersistenceMechanismException e) {
            throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
        } catch (SQLException e) {
        	System.out.println(sql);
            e.printStackTrace();
            throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
        }finally {
            try {
                mp.releaseCommunicationChannel();
            } catch (PersistenceMechanismException e) {
                throw new PersistenceSoftException(e);
            }
        }

        return symptom;
	}
	
	public IteratorDsk getSymptomList() throws ObjectNotFoundException, RepositoryException {
        List listaSymptom = new ArrayList();
        
		// Query para selecionar os codigos de todas unidades de saude
		// existentes no sistema
        String    sql = "SELECT * FROM SCBS_sintoma";
        ResultSet rs  = null;
        Symptom symptom;

       	try {
            Statement stmt = (Statement)this.mp.getCommunicationChannel();
            rs = stmt.executeQuery(sql);

			// O resultado da query eh testado para saber
			// da existencia de unidades de saude cadastradas.
			// Caso nao existam uma excecao eh lancada.
            if (rs.next()) {
                symptom = search((new Integer(rs.getString("codigo"))).intValue());
                listaSymptom.add(symptom);
            } else {
                throw new ObjectNotFoundException(ExceptionMessages
                    .EXC_FALHA_PROCURA);
            }

            // O resultado da query eh navegado, e cada
            // codigo ehinformado a um metodo (procura) que
            // monta uma unidade de aude a partir do codigo.
            while (rs.next()) {                
                symptom = search((new Integer(rs.getString("codigo"))).intValue());
                listaSymptom.add(symptom);
            }
            rs.close();
            stmt.close();
        } catch (PersistenceMechanismException e) {
        	e.printStackTrace();
            throw new RepositoryException(ExceptionMessages.EXC_FALHA_PROCURA);
        } catch (SQLException e) {
        	System.out.println(sql);
        	e.printStackTrace();
            throw new RepositoryException(ExceptionMessages.EXC_FALHA_PROCURA);
        } finally {
            try {
                mp.releaseCommunicationChannel();
            } catch (PersistenceMechanismException e) {
                throw new PersistenceSoftException(e);
            }
        }
        // O retorno desse metodo eh uma estrutura que permite a
        // iteracao nos elementos
        return new ConcreteIterator(listaSymptom);

	}
	public void update(Symptom symptom) throws ObjectNotValidException, ObjectNotFoundException,
		ObjectNotValidException, RepositoryException {
		
		if (symptom != null) {
			String sql=null;
			try {
				Statement stmt = (Statement) this.mp.getCommunicationChannel();
				sql = "update SCBS_sintoma set " +
                "descricao='" + symptom.getDescription() + "'" +
                " where codigo = '"+symptom.getCode()+"'";
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
            sql = "select * from SCBS_sintoma where " + "codigo = '" + code + "'";


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
    
	public void remove(int code) throws ObjectNotFoundException, RepositoryException {
	}

}