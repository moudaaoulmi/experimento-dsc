package healthwatcher.model.complaint;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.state.AnimalComplaintState;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public class AnimalComplaint extends Complaint {

	private AnimalComplaintState state;

	// construtor vazio
	public AnimalComplaint() {
		super();
	}

	public AnimalComplaint(String solicitante, String descricao,
			String observacao, String email, Employee atendente,
			int situacao, Date dataParecer,
			Date dataQueixa, Address enderecoSolicitante, short qtdeAnimais,
			Date dataIncomodo, String animal, Address enderecoLocalOcorrencia) {
		// inicializar tipo da queixa
		super(solicitante, descricao, observacao,
				email, atendente, situacao, dataParecer,
				dataQueixa, enderecoSolicitante);
	}
	public java.lang.String getAnimal() {
		return state.getAnimal();
	}
	public Date getInconvenienceDate() {
		return state.getDataIncomodo();
	}
	public Address getOccurenceLocalAddress() {
		return state.getEnderecoLocalOcorrencia();
	}
	public short getAnimalQuantity() {
		return state.getQtdeAnimais();
	}
	public void setAnimal(String newAnimal) {
		state.setAnimal(newAnimal);
	}
	public void setInconvenienceDate(Date newDataIncomodo) {
		state.setDataIncomodo(newDataIncomodo);
	}
	public void setOccurenceLocalAddress(Address newEnderecoLocalOcorrencia) {
		state.setEnderecoLocalOcorrencia(newEnderecoLocalOcorrencia);
	}
	public void setAnimalQuantity(short newQtdeAnimais) {
		state.setQtdeAnimais(newQtdeAnimais);
	}

	public void setStatus(int newStatus) {
		super.setSituacao(newStatus);
		state.setStatus(newStatus, this);
	}
	
	public void setComplaintState(AnimalComplaintState _state){
    	state= _state;
    }
}