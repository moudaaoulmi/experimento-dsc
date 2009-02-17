package healthwatcher.model.complaint;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.state.ComplaintState;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

//classe queixa eh uma classe basica que tem subclasses
public abstract class Complaint {

	public static final int QUEIXA_ALIMENTAR = 1;

	public static final int QUEIXA_ANIMAL = 2;

	public static final int QUEIXA_DIVERSA = 3;

	protected ComplaintState state;

	public Complaint() {
	}

	public Complaint(String solicitante, String descricao, String observacao, String email,
			Employee atendente, int situacao, Date dataParecer, Date dataQueixa,
			Address enderecoSolicitante) {

	}

	public Employee getAtendente() {
		return state.getAttendant();
	}

	public void setAtendente(Employee atendente) {
		state.setAttendant(atendente);
	}

	public int getCodigo() {
		return state.getCode();
	}

	public void setCodigo(int codigo) {
		state.setCode(codigo);
	}

	public Date getDataParecer() {
		return state.getMedicalOpinionDate();
	}

	public void setDataParecer(Date dataParecer) {
		state.setMedicalOpinionDate(dataParecer);
	}

	public Date getDataQueixa() {
		return state.getComplaintDate();
	}

	public void setDataQueixa(Date dataQueixa) {
		state.setComplaintDate(dataQueixa);
	}

	public String getDescricao() {
		return state.getDescription();
	}

	public void setDescricao(String descricao) {
		state.setDescription(descricao);
	}

	public String getEmail() {
		return state.getEmail();
	}

	public void setEmail(String email) {
		state.setEmail(email);
	}

	public Address getEnderecoSolicitante() {
		return state.getComplainerAddress();
	}

	public void setEnderecoSolicitante(Address enderecoSolicitante) {
		state.setComplainerAddress(enderecoSolicitante);
	}

	public String getObservacao() {
		return state.getObservation();
	}

	public void setObservacao(String observacao) {
		state.setObservation(observacao);
	}

	public int getSituacao() {
		return state.getStatus();
	}

	public void setSituacao(int situacao) {
		state.setStatus(situacao,this);
	}

	public String getSolicitante() {
		return state.getComplainer();
	}

	public void setSolicitante(String solicitante) {
		state.setComplainer(solicitante);
	}
	
	public void setComplaintState(ComplaintState _state) {
		state = _state;
	} 
}