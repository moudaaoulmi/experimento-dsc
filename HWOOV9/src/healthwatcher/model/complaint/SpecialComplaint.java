package healthwatcher.model.complaint;



import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.state.SpecialComplaintState;
import healthwatcher.model.complaint.state.SpecialComplaintStateClosed;
import healthwatcher.model.complaint.state.SpecialComplaintStateOpen;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public class SpecialComplaint extends Complaint {

    private SpecialComplaintState state;

	//construtor vazio
    public SpecialComplaint() {
    	super();
    	state= new SpecialComplaintStateOpen();
    }

	public SpecialComplaint(String solicitante, String descricao, String observacao, String email,
			Employee atendente, int situacao, Date dataParecer, Date dataQueixa,
			Address enderecoSolicitante, short idade, String instrucao, String ocupacao,
			Address enderecoOcorrencia) {

		//inicializar tambem o tipo da queixa
	     super(solicitante, descricao, observacao,email, atendente, situacao, dataParecer,dataQueixa, enderecoSolicitante,0);

	        if(situacao==Situation.QUEIXA_ABERTA)
	            state= new SpecialComplaintStateOpen(idade,instrucao, ocupacao,enderecoOcorrencia);
	            else if(situacao==Situation.QUEIXA_FECHADA)
	            	state= new SpecialComplaintStateClosed(idade,instrucao, ocupacao,enderecoOcorrencia);
	}

	public void setSituacao(int situacao) {
		super.setSituacao(situacao);
		state.setStatus(situacao, this);
	}
	
	public Address getEnderecoOcorrencia() {
		return state.getEnderecoOcorrencia();
	}

	public void setEnderecoOcorrencia(Address enderecoOcorrencia) {
		state.setEnderecoOcorrencia(enderecoOcorrencia);
	}

	public short getIdade() {
		return state.getIdade();
	}

	public void setIdade(short idade) {
		state.setIdade(idade);
	}

	public String getInstrucao() {
		return state.getInstrucao();
	}

	public void setInstrucao(String instrucao) {
		state.setInstrucao(instrucao);
	}

	public String getOcupacao() {
		return state.getOcupacao();
	}

	public void setOcupacao(String ocupacao) {
		state.setOcupacao(ocupacao);
	}
	public void setComplaintState(SpecialComplaintState _state){
    	state= _state;
    }
}