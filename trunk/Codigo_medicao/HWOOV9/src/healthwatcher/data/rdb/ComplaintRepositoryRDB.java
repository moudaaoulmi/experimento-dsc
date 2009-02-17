package healthwatcher.data.rdb;

import healthwatcher.data.IComplaintRepository;
import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.AnimalComplaint;
import healthwatcher.model.complaint.Complaint;
import healthwatcher.model.complaint.FoodComplaint;
import healthwatcher.model.complaint.SpecialComplaint;
import healthwatcher.model.employee.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lib.exceptions.ExceptionMessages;
import lib.exceptions.InvalidDateException;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.PersistenceMechanismException;
import lib.exceptions.PersistenceSoftException;
import lib.exceptions.RepositoryException;
import lib.persistence.IPersistenceMechanism;
import lib.persistence.PersistenceMechanism;
import lib.util.ConcreteIterator;
import lib.util.Date;
import lib.util.IteratorDsk;


public class ComplaintRepositoryRDB implements IComplaintRepository {

	private IPersistenceMechanism mp;

	protected ResultSet resultSet; // Para consultas apenas

	private AddressRepositoryRDB addressRep;

	private EmployeeRepositoryRDB employeeRep;

	private static final int FOOD_COMPLAINT = 1;

	private static final int ANIMAL_COMPLAINT = 2;

	private static final int SPECIAL_COMPLAINT = 3;

	public ComplaintRepositoryRDB(IPersistenceMechanism mp) {
		this.mp = mp;
		addressRep = new AddressRepositoryRDB(mp);
		employeeRep = new EmployeeRepositoryRDB(mp);
	}

	private FoodComplaint accessFood(int code) throws RepositoryException, ObjectNotFoundException {

		FoodComplaint complaint;
		String sql = null;
		try {
			complaint = new FoodComplaint();

			// fazer join para acessar as duas tabelas
			sql = "select * from SCBS_queixa q,SCBS_queixaalimentar qa where q.codigo=qa.codigo and q."
					+ "codigo = '" + code + "';";

			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			resultSet = stmt.executeQuery(sql);

			if (resultSet.next()) {
				try {
					accessComplaint(resultSet, complaint);
				} catch (ObjectNotFoundException e) {
					e.printStackTrace();
				}
				complaint.setQtdeComensais(resultSet.getShort("qtdeComensais"));
				complaint.setQtdeDoentes(resultSet.getShort("qtdeDoentes"));
				complaint.setQtdeInternacoes(resultSet.getShort("qtdeInternacoes"));
				complaint.setQtdeObitos(resultSet.getShort("qtdeObitos"));
				complaint.setLocalAtendimento(resultSet.getString("localAtendimento"));
				complaint.setRefeicaoSuspeita(resultSet.getString("refeicaoSuspeita"));

				String endDoente = resultSet.getString("enderecodoente");
				// System.out.println("endereço doente = "+endDoente);
				Address endDo = addressRep.search((new Integer(endDoente)).intValue());
				complaint.setEnderecoDoente(endDo);
			} else {
				throw new ObjectNotFoundException(ExceptionMessages.EXC_FALHA_PROCURA);
			}
			resultSet.close();
			stmt.close();
		} catch (PersistenceMechanismException e) {
			e.printStackTrace();
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}
		return complaint;
	}

	/**
	 * Métodos para recuperar uma queixa animal a partir do seu código
	 * 
	 * @param codigo
	 *            código da queixa animal
	 * @return queixa animal encontrada
	 * @exception RepositoryException
	 *                erro no acesso ao repositorio de dados
	 * @exception ObjectNotFoundException
	 *                caso não seja encontrada uma queixa com o código
	 *                especificado.
	 */
	private AnimalComplaint accessAnimal(int codigo) throws RepositoryException,
			ObjectNotFoundException {

		AnimalComplaint complaint;
		String sql = null;
		try {

			complaint = new AnimalComplaint();

			// Query sql que recupera todos os dados de uma complaint animal
			// para isso realiza um join para acessar as duas tabelas
			// A primeira tabela contém os dados genéricos a todas as
			// queixas, a segunda tabela contém os dados específicos
			// a complaint animal
			sql = "select * from SCBS_queixa q,SCBS_queixaanimal qa where q.codigo=qa.codigo and q."
					+ "codigo = '" + codigo + "'";

			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			resultSet = stmt.executeQuery(sql);

			if (resultSet.next()) {
				accessComplaint(resultSet, complaint);

				complaint.setAnimalQuantity(resultSet.getShort("qtdeAnimais"));
				String date = resultSet.getString("dataIncomodo");
				java.util.Date d = null;
				if (!date.equals("NULL")) {
					StringTokenizer token = new StringTokenizer(date, "/");
					int day = (new Integer(token.nextToken())).intValue();
					int month = (new Integer(token.nextToken())).intValue();
					int year = (new Integer(token.nextToken())).intValue();
					d = new java.util.Date(year, month, day);
				}
				if (d != null) {
					try {
						complaint.setInconvenienceDate(new Date(d.getDate(), d.getMonth() + 1, d
								.getYear() + 1900));
					} catch (InvalidDateException ex) {
					}
				} else {
					complaint.setInconvenienceDate(null);
				}

				complaint.setAnimal(resultSet.getString("animal"));

				String endAnimal = resultSet.getString("enderecolocalocorrencia");
				// System.out.println("endereco animal = "+ endAnimal);
				Address endLO = addressRep.search((new Integer(endAnimal)).intValue());
				complaint.setOccurenceLocalAddress(endLO);

			} else {
				throw new ObjectNotFoundException(ExceptionMessages.EXC_FALHA_PROCURA);
			}
			resultSet.close();
			stmt.close();
		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}
		return complaint;
	}

	private void accessComplaint(ResultSet resultSet, Complaint complaint) throws SQLException,
			ObjectNotFoundException, RepositoryException {
		try {
			complaint.setCodigo((new Integer(resultSet.getString("codigo"))).intValue());
			complaint.setSolicitante(resultSet.getString("solicitante"));
			complaint.setDescricao(resultSet.getString("descricao"));
			complaint.setObservacao(resultSet.getString("observacao"));
			complaint.setEmail(resultSet.getString("email"));

			String funcionario = resultSet.getString("funcionario");
			Employee employee = null;
			if (funcionario != null && !funcionario.equalsIgnoreCase("null")) {
				employee = employeeRep.search(funcionario);
			}
			complaint.setAtendente(employee);

			complaint.setSituacao((new Integer(resultSet.getString("situacao"))).intValue());

			try {
				java.util.Date d = null;
				String date = resultSet.getString("dataParecer");
				if (!date.equals("NULL")) {
					StringTokenizer token = new StringTokenizer(date, "/");
					int day = (new Integer(token.nextToken())).intValue();
					int month = (new Integer(token.nextToken())).intValue();
					int year = (new Integer(token.nextToken())).intValue();
					d = new java.util.Date(year, month, day);
				}
				if (d != null) {
					try {
						complaint.setDataParecer(new Date(d.getDate(), d.getMonth() + 1, d
								.getYear() + 1900));
					} catch (InvalidDateException ex) {
					}
				} else {
					complaint.setDataParecer(null);
				}
				date = resultSet.getString("dataQueixa");
				if (!date.equals("NULL")) {

					StringTokenizer token = new StringTokenizer(date, "/");
					int day = (new Integer(token.nextToken())).intValue();
					int month = (new Integer(token.nextToken())).intValue();
					int year = (new Integer(token.nextToken())).intValue();
					d = new java.util.Date(year, month, day);
				} else
					d = null;
				if (d != null) {
					try {
						complaint.setDataQueixa(new Date(d.getDate(), d.getMonth() + 1,
								d.getYear() + 1900));
					} catch (InvalidDateException ex) {
					}
				} else {
					complaint.setDataQueixa(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			int codEndereco = resultSet.getInt("enderecosolicitante");
			// System.out.println("endereco solicitante" + codEndereco);
			Address endSol = addressRep.search(codEndereco);
			complaint.setEnderecoSolicitante(endSol);

		} catch (RepositoryException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (ObjectNotFoundException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (SQLException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}

	}

	/**
	 * Métodos para recuperar uma queixa diversa a partir do seu código
	 * 
	 * @param code
	 *            código da queixa diversa
	 * @return queixa diversa encontrada
	 * @exception RepositoryException
	 *                erro no acesso ao repositorio de dados
	 * @exception ObjectNotFoundException
	 *                caso não seja encontrada uma queixa com o código
	 *                especificado.
	 */
	private SpecialComplaint accessSpecial(int code) throws RepositoryException,
			ObjectNotFoundException {
		SpecialComplaint complaint;
		String sql = null;
		try {

			complaint = new SpecialComplaint();

			// fazer join para acessar as duas tabelas
			sql = "select * from SCBS_queixa q, SCBS_queixadiversa qd where q.codigo=qd.codigo and q."
					+ "codigo = '" + code + "'";

			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			resultSet = stmt.executeQuery(sql);

			if (resultSet.next()) {

				accessComplaint(resultSet, complaint);

				complaint.setTimestamp((new Long(resultSet.getString("ts"))).longValue());
				complaint.setIdade(resultSet.getShort("idade"));
				complaint.setInstrucao(resultSet.getString("instrucao"));
				complaint.setOcupacao(resultSet.getString("ocupacao"));

				Address endO = addressRep.search((new Integer(resultSet
						.getString("enderecoocorrencia")).intValue()));
				complaint.setEnderecoOcorrencia(endO);
			} else {
				throw new ObjectNotFoundException(ExceptionMessages.EXC_FALHA_PROCURA);
			}
			resultSet.close();
			stmt.close();
		} catch (PersistenceMechanismException e) {
			e.printStackTrace();
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}
		return complaint;
	}

	public void update(Complaint complaint) throws RepositoryException, ObjectNotFoundException,
			ObjectNotValidException {
		synchronized (this) {
			long timestamp;
			try {
				if (complaint != null) {
					String sql = null;
					try {
						Statement stmt = (Statement) this.mp.getCommunicationChannel();
						// vendo se a versão do objeto é a mesma no BD
						sql = "select ts from SCBS_queixa " + " where codigo='"
								+ complaint.getCodigo() + "'";
						ResultSet resultSet = stmt.executeQuery(sql);
						if (resultSet.next()) {
							timestamp = (new Long(resultSet.getString("ts"))).longValue();
							if (timestamp != complaint.getTimestamp()) {
								throw new RepositoryException(
										ExceptionMessages.EXC_FALHA_ATUALIZACAO_COPIA);
							} else {
								complaint.incTimestamp();
							}
						} else {
							throw new ObjectNotFoundException(
									ExceptionMessages.EXC_FALHA_ATUALIZACAO);
						}
						resultSet.close();
						stmt.close();
						stmt = (Statement) this.mp.getCommunicationChannel();
						sql = "update SCBS_queixa set " + "observacao='"
								+ complaint.getObservacao() + "', " + "situacao= '"
								+ complaint.getSituacao() + "', ts= '"
								+ complaint.getTimestamp() + "'";

						if (complaint.getAtendente() != null) {
							sql += ", funcionario= '" + complaint.getAtendente().getLogin() + "'";
						}
						if (complaint.getDataParecer() != null) {
							sql += ", dataparecer= '" + complaint.getDataParecer() + "'";
						}
						sql += " where codigo = '" + complaint.getCodigo() + "'";

						int response = stmt.executeUpdate(sql);
						if (response == 0) {
							throw new ObjectNotFoundException(
									ExceptionMessages.EXC_FALHA_ATUALIZACAO);
						}
						stmt.close();
					} catch (SQLException e) {
						System.out.println(sql);
						throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
					}
				} else {
					throw new ObjectNotValidException(ExceptionMessages.EXC_NULO);
				}
			} catch (PersistenceMechanismException e) {
				throw new RepositoryException(ExceptionMessages.EXC_FALHA_ATUALIZACAO);
			}
			updateTimestamp(complaint.getTimestamp() + "", "SCBS_queixa", complaint.getCodigo()
					+ "");
		}
	}

	/*
	 * public IComplaintRepository searchComplexComplaint(int tipo,util.Date
	 * data1,util.Date data2,int situacao) throws
	 * RepositoryException,ObjectNotFoundException,DataInvalidException { /*
	 * String sql=null; Queixa q = null; int codigo = 0; IRepositorioQueixa
	 * repQueixa = null;
	 * 
	 * try { sql = "SELECT * FROM QUEIXA";
	 * 
	 * if (tipo != 0) { sql += "tipoqueixa= "+tipo; if (data1 != null || data2 !=
	 * null || situacao !=0) sql += " AND "; } if (data1 != null) { sql +=
	 * "DATAQUEIXA > '"+Data.format(data1,Data.FORMATO1)+"'"; if (data2 != null ||
	 * situacao !=0) sql += " AND "; } if (data2 != null) { sql += "DATAQUEIXA <
	 * '"+Data.format(data2,Data.FORMATO1)+"'"; if (situacao !=0) sql += " AND "; }
	 * if (situacao != 0) sql += "SITUACAOQUEIXA = '"+situacao+"'"; sql += ";";
	 * 
	 * java.sql.Statement stmt = (java.sql.Statement)
	 * this.mp.getCommunicationChannel(); ResultSet rs = stmt.executeQuery(sql);
	 * repQueixa = new RepositorioQueixaArray(); if (!rs.next()) throw new
	 * ObjectNotFoundException(ExceptionMessages.EXC_FALHA_PROCURA); else {
	 * codigo = rs.getInt("codigoQUEIXA"); tipo = rs.getInt("tipoqueixa");
	 * 
	 * switch(tipo) { case Queixa.QUEIXA_ALIMENTAR: q = new QueixaAlimentar();
	 * q.setWhereClause("codigo = '"+codigo+"'"); q.deepAccess(this.mp); break;
	 * case Queixa.QUEIXA_DOENCA: q = new QueixaDoenca();
	 * q.setWhereClause("codigo = '"+codigo+"'"); q.deepAccess(this.mp); break;
	 * case Queixa.QUEIXA_ANIMAL: q = new QueixaAnimal();
	 * q.setWhereClause("codigo = '"+codigo+"'"); q.deepAccess(this.mp); break;
	 * case Queixa.QUEIXA_DIVERSA: q = new QueixaDiversa();
	 * q.setWhereClause("codigo = '"+codigo+"'"); q.deepAccess(this.mp); break; }
	 * repQueixa.insere(q); } while (rs.next()) { codigo =
	 * rs.getInt("codigoQUEIXA"); tipo = rs.getInt("tipoqueixa");
	 * 
	 * switch(tipo) { case Queixa.QUEIXA_ALIMENTAR: q = new QueixaAlimentar();
	 * q.setWhereClause("codigo = '"+codigo+"'"); q.deepAccess(this.mp); break;
	 * case Queixa.QUEIXA_DOENCA: q = new QueixaDoenca();
	 * q.setWhereClause("codigo = '"+codigo+"'"); q.deepAccess(this.mp); break;
	 * case Queixa.QUEIXA_ANIMAL: q = new QueixaAnimal();
	 * q.setWhereClause("codigo = '"+codigo+"'"); q.deepAccess(this.mp); break;
	 * case Queixa.QUEIXA_DIVERSA: q = new QueixaDiversa();
	 * q.setWhereClause("codigo = '"+codigo+"'"); q.deepAccess(this.mp); break; }
	 * repQueixa.insere(q); }
	 * 
	 * rs.close(); stmt.close(); } catch(PersistenceMechanismException e) {
	 * throw new RepositoryException(ExceptionMessages.EXC_FALHA_PROCURA); }
	 * catch(NaoHaTransacaoEmAndamentoException e) { throw new
	 * RepositoryException(ExceptionMessages.EXC_FALHA_TRANSACAO); }
	 * catch(SQLException e) { throw new
	 * RepositoryException(ExceptionMessages.EXC_FALHA_PROCURA); }
	 * catch(ObjetoJaExistenteException e) {} catch(ObjectNotValidException e) {}
	 * return repQueixa;
	 * 
	 * return null; }
	 */
	private void deepInsertFood(FoodComplaint complaint) throws PersistenceMechanismException,
			RepositoryException, ObjectAlreadyInsertedException {
		if (complaint.getEnderecoDoente() != null) {
			try {
				addressRep.insert(complaint.getEnderecoDoente());
			} catch (ObjectNotValidException e) {
			}
		}
		insertFood(complaint);
	}

	private void deepInsertAnimal(AnimalComplaint complaint) throws PersistenceMechanismException,
			RepositoryException, ObjectAlreadyInsertedException {
		if (complaint.getOccurenceLocalAddress() != null) {
			try {
				addressRep.insert(complaint.getOccurenceLocalAddress());
			} catch (ObjectNotValidException e) {
			}
		}
		insertAnimal(complaint);
	}

	private void deepInsertCommon(Complaint complaint) throws ObjectAlreadyInsertedException,
			PersistenceMechanismException, RepositoryException {
		try {
			if (complaint.getEnderecoSolicitante() != null) {
				try {
					addressRep.insert(complaint.getEnderecoSolicitante());
				} catch (ObjectNotValidException e) {
				}
			}

			int complaintType = -1;
			if (complaint instanceof SpecialComplaint) {
				complaintType = SPECIAL_COMPLAINT;
			} else if (complaint instanceof FoodComplaint) {
				complaintType = FOOD_COMPLAINT;
			} else if (complaint instanceof AnimalComplaint) {
				complaintType = ANIMAL_COMPLAINT;
			}

			String sql = "INSERT INTO SCBS_queixa (codigo,tipoqueixa,solicitante,descricao,observacao,email,funcionario,situacao,dataparecer,dataqueixa,enderecosolicitante,ts) VALUES(";
			sql += "'" + complaint.getCodigo() + "'" + ",";
			sql += "'" + complaintType + "'" + ",";
			sql += "'" + complaint.getSolicitante() + "',";
			sql += "'" + complaint.getDescricao() + "',";
			sql += "'" + complaint.getObservacao() + "',";
			sql += "'" + complaint.getEmail() + "',";

			if (complaint.getAtendente() != null) {
				sql += "'" + complaint.getAtendente().getLogin() + "','";
			} else {
				sql += "'NULL',";
			}

			sql += "'" + complaint.getSituacao() + "'" + ",";

			if (complaint.getDataParecer() != null) {
				sql += "'" + Date.format(complaint.getDataParecer(), Date.FORMATO1) + "'";
			} else {
				sql += "'NULL',";
			}

			if (complaint.getDataQueixa() != null) {
				sql += "'" + Date.format(complaint.getDataQueixa(), Date.FORMATO1) + "',";
			} else {
				sql += "'',";
			}

			if (complaint.getEnderecoSolicitante() != null) {
				sql += "'" + complaint.getEnderecoSolicitante().getCode() + "'";
			} else {
				sql += "NULL";
			}
			sql += ",'" + complaint.getTimestamp() + "');";

			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (SQLException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}
	}

	private void deepInsertSpecial(SpecialComplaint complaint)
			throws PersistenceMechanismException, RepositoryException,
			ObjectAlreadyInsertedException {
		if (complaint.getEnderecoOcorrencia() != null) {
			try {
				addressRep.insert(complaint.getEnderecoOcorrencia());
			} catch (ObjectNotValidException e) {
			}
		}
		insertSpecial(complaint);
	}

	public boolean exists(int code) {
		boolean response = false;
		String consulta = null;
		try {
			consulta = "select codigo from SCBS_queixa where codigo='" + code + "'";

			Statement stmt = (Statement) mp.getCommunicationChannel();
			resultSet = stmt.executeQuery(consulta);

			response = resultSet.next();

			resultSet.close();
			stmt.close();
		} catch (PersistenceMechanismException e) {
			throw new PersistenceSoftException(e);
		} catch (SQLException e) {
			System.out.println(consulta);
			throw new PersistenceSoftException(e);
		}
		return response;
	}

	public int insert(Complaint complaint) throws ObjectAlreadyInsertedException,
			RepositoryException, ObjectNotValidException {
		String consulta = null;
		try {
			if (complaint != null) {
				consulta = "select * FROM SCBS_queixa";

				Statement stmt = (Statement) mp.getCommunicationChannel();
				resultSet = stmt.executeQuery(consulta);

				int count = 0;
				while (resultSet.next()) {
					count++;
				}
				complaint.setCodigo(count + 1);

				deepInsertCommon(complaint);

				if (complaint instanceof SpecialComplaint) {
					SpecialComplaint special = (SpecialComplaint) complaint;
					deepInsertSpecial(special);
				} else if (complaint instanceof FoodComplaint) {
					FoodComplaint food = (FoodComplaint) complaint;
					deepInsertFood(food);
				} else if (complaint instanceof AnimalComplaint) {
					AnimalComplaint animal = (AnimalComplaint) complaint;
					deepInsertAnimal(animal);
				}
			} else {
				throw new ObjectNotValidException(ExceptionMessages.EXC_NULO);
			}
		} catch (PersistenceMechanismException e) {
			e.printStackTrace();
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_INCLUSAO);
		} catch (SQLException e) {
			System.out.println(consulta);
			e.printStackTrace();
		}
		updateTimestamp(complaint.getTimestamp() + "", "SCBS_queixa", complaint.getCodigo() + "");
		return complaint.getCodigo();
	}

	private void insertFood(FoodComplaint complaint) throws RepositoryException {
		String sql = null;
		try {
			sql = "insert into SCBS_queixaalimentar (codigo,qtdecomensais,qtdedoentes,qtdeinternacoes,qtdeobitos,localatendimento,refeicaosuspeita,enderecodoente) values (";
			sql += "'" + complaint.getCodigo() + "','";
			sql += complaint.getQtdeComensais() + "','";
			sql += complaint.getQtdeDoentes() + "','";
			sql += complaint.getQtdeInternacoes() + "',";
			sql += complaint.getQtdeObitos() + ",";
			sql += "'" + complaint.getLocalAtendimento() + "',";
			sql += "'" + complaint.getRefeicaoSuspeita() + "','";

			if (complaint.getEnderecoDoente() != null) {
				sql += complaint.getEnderecoDoente().getCode() + "')";
			} else {
				sql += "NULL')";
			}

			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (SQLException e) {
			System.out.println(sql);
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}
	}

	private void insertAnimal(AnimalComplaint complaint) throws RepositoryException {
		String sql = null;
		try {
			// Inserir na tabela agora
			sql = "insert into SCBS_queixaanimal (codigo,qtdeanimais,dataincomodo,animal,enderecolocalocorrencia) values (";
			sql += "'" + complaint.getCodigo() + "','";
			sql += complaint.getAnimalQuantity() + "',";
			sql += "'" + complaint.getInconvenienceDate() + "',";
			sql += "'" + complaint.getAnimal() + "','";

			if (complaint.getOccurenceLocalAddress() != null) {
				sql += complaint.getOccurenceLocalAddress().getCode() + "')";
			} else {
				sql += "NULL')";
			}

			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (SQLException e) {
			System.out.println(sql);
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}
	}

	private void insertSpecial(SpecialComplaint complaint) throws RepositoryException {
		String sql = null;
		try {
			// Inserir na tabela agora
			sql = "insert into SCBS_queixadiversa (codigo,idade,ocupacao,instrucao,enderecoocorrencia) values (";
			sql += "'" + complaint.getCodigo() + "','";
			sql += complaint.getIdade() + "',";
			sql += "'" + complaint.getOcupacao() + "',";
			sql += "'" + complaint.getInstrucao() + "','";

			if (complaint.getEnderecoOcorrencia() != null) {
				sql += complaint.getEnderecoOcorrencia().getCode() + "')";
			} else {
				sql += "NULL')";
			}
			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (SQLException e) {
			System.out.println(sql);
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}
	}

	public Complaint search(int code) throws RepositoryException, ObjectNotFoundException {
		// Query montada para obtenção do tipo de queixa referente ao
		// código informado
		String sql = "SELECT tipoqueixa FROM SCBS_queixa WHERE codigo = '" + code + "'";
		int tipoQueixa;
		Complaint q = null;

		try {
			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				tipoQueixa = (new Integer(rs.getString("tipoqueixa"))).intValue();
			} else {
				throw new ObjectNotFoundException(ExceptionMessages.EXC_FALHA_PROCURA + " code: "
						+ code);
			}
			rs.close();
			stmt.close();
		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_PROCURA);
		} catch (SQLException e) {
			System.out.println(sql);
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_PROCURA);
		}

		// Dependendo do tipo da queixa o acesso aos dados é feito
		// por um método específico
		switch (tipoQueixa) {

		case Complaint.QUEIXA_ALIMENTAR:
			q = accessFood(code);
			break;

		case Complaint.QUEIXA_ANIMAL:
			q = accessAnimal(code);
			break;

		case Complaint.QUEIXA_DIVERSA:
			q = accessSpecial(code);
			break;

		default:
			throw new IllegalArgumentException();
		}
		long timestamp = searchTimestamp("SCBS_queixa", q.getCodigo() + "");
		q.setTimestamp(timestamp);
		return q;
	}

	public void remove(int codigo) throws RepositoryException, ObjectNotFoundException {
	}

	private void updateTimestamp(String value, String tableName, String id) {
		Statement stmt = null;
		int result = 0;
		try {
			String sql = "update " + tableName + " set ts='" + value + "' where codigo='" + id
					+ "'";
			stmt = (Statement) this.mp.getCommunicationChannel();
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

	public IteratorDsk getComplaintList() throws ObjectNotFoundException, RepositoryException {
		List cList = new ArrayList();
		String sql = "SELECT * FROM SCBS_queixa";
		ResultSet rs = null;
		Complaint complaint = null;
		try {
			Statement stmt = (Statement) this.mp.getCommunicationChannel();
			rs = stmt.executeQuery(sql);

			if (!rs.next()) {
				throw new ObjectNotFoundException(ExceptionMessages.EXC_FALHA_PROCURA);
			}
			do {
				int tipoQueixa = (new Integer(rs.getString("tipoqueixa"))).intValue();
				int code = (new Integer(rs.getString("codigo"))).intValue();
				switch (tipoQueixa) {
				case SPECIAL_COMPLAINT:
					complaint = accessSpecial(code);
					break;

				case FOOD_COMPLAINT:
					complaint = accessFood(code);
					break;

				case ANIMAL_COMPLAINT:
					complaint = accessAnimal(code);
					break;

				default:
					throw new IllegalArgumentException();
				}
				cList.add(complaint);
			} while (rs.next());

			rs.close();
			stmt.close();
		} catch (PersistenceMechanismException e) {
			e.printStackTrace();
			throw new RepositoryException("PersistenceMechanismException: " + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException("SQLException: " + e.getMessage());
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new RepositoryException("SQLException: " + e.getMessage());
		}

		return new ConcreteIterator(cList);
	}
}
