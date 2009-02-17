package healthwatcher.model.complaint;

import healthwatcher.model.address.Address;
import healthwatcher.model.complaint.state.SpecialComplaintState;
import healthwatcher.model.employee.Employee;
import lib.util.Date;

public class SpecialComplaint extends Complaint {

    private SpecialComplaintState state;

    //construtor vazio
    public SpecialComplaint() {
    	super();
    }
    public SpecialComplaint(String solicitante, String descricao,
                         String observacao, String email,
                         Employee atendente,
                         int situacao,
                         Date dataParecer, Date dataQueixa,
                         Address enderecoSolicitante, short idade,
                         String instrucao, String ocupacao,
                         Address enderecoOcorrencia) {

        //inicializar tambem o tipo da queixa
        super(solicitante, descricao, observacao,email, atendente, situacao, dataParecer,dataQueixa, enderecoSolicitante);
    }

    public Address getEnderecoOcorrencia() {
        return state.getEnderecoOcorrencia();
    }
    public short getIdade() {
        return state.getIdade();
    }
    public String getInstrucao() {
        return state.getInstrucao();
    }
    public String getOcupacao() {
        return state.getOcupacao();
    }
    public void setEnderecoOcorrencia(Address newEnderecoOcorrencia) {
		state.setEnderecoOcorrencia(newEnderecoOcorrencia);
	}
	public void setIdade(short newIdade) {
		state.setIdade(newIdade);
	}
	public void setInstrucao(java.lang.String newInstrucao) {
		state.setInstrucao(newInstrucao);
	}
	public void setOcupacao(java.lang.String newOcupacao) {
		state.setOcupacao(newOcupacao);
	}
	public void setSituacao(int newStatus) {
		super.setSituacao(newStatus);
		state.setStatus(newStatus, this);
	}
	public void setComplaintState(SpecialComplaintState _state){
    	state= _state;
    }
}